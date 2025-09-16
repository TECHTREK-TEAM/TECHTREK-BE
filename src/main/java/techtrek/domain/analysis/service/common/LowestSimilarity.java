package techtrek.domain.analysis.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LowestSimilarity {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    // 최소 유사도 필드 추출 (겹치면 랜덤)
    public AnalysisParserResponse.LowestSimilarity exec(String sessionKey) {
        List<String> patterns = List.of(sessionKey + basicPrefix + "*", sessionKey + resumePrefix + "*", sessionKey + tailPrefix + "*");
        List<AnalysisParserResponse.LowestSimilarity> allFields = new ArrayList<>();

        for (String pattern : patterns) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys == null) continue;

            for (String key : keys) {
                Object simObj = redisTemplate.opsForHash().get(key, "similarity");
                if (simObj == null) continue;

                try {
                    double sim = Double.parseDouble(simObj.toString());
                    String question = (String) redisTemplate.opsForHash().get(key, "question");
                    String answer = (String) redisTemplate.opsForHash().get(key, "answer");
                    String questionNumber = (String) redisTemplate.opsForHash().get(key, "questionNumber");

                    allFields.add(new AnalysisParserResponse.LowestSimilarity(question, answer, sim, questionNumber));
                } catch (NumberFormatException e) {
                    throw new CustomException(ErrorCode.INVALID_SIMILARITY_VALUE);
                } catch (Exception e) {
                    throw new CustomException(ErrorCode.REDIS_HASH_ACCESS_FAIL);
                }
            }
        }

        if (allFields.isEmpty()) return null;

        double minSim = allFields.stream().mapToDouble(AnalysisParserResponse.LowestSimilarity::getSimilarity).min().orElse(0.0);
        List<AnalysisParserResponse.LowestSimilarity> minSimFields = allFields.stream()
                .filter(f -> f.getSimilarity() == minSim)
                .collect(Collectors.toList());

        Collections.shuffle(minSimFields);
        return minSimFields.get(0);
    }
}

