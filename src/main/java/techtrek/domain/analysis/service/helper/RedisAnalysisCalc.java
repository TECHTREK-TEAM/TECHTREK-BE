package techtrek.domain.analysis.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RedisAnalysisCalc {
    private final StringRedisTemplate redisTemplate;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    // Redis에서 면접 내용 조회
    public List<AnalysisParserResponse.RedisAnalysisResult> exec(String sessionId) {
        String sessionKeyPrefix = interviewPrefix + sessionId;
        List<String> interviewTypes = List.of(basicPrefix, resumePrefix, tailPrefix);

        List<AnalysisParserResponse.RedisAnalysisResult> interviews = new ArrayList<>();

        for (String type : interviewTypes) {
            String redisKeyPattern = sessionKeyPrefix + type + "*";

            Map<String, Map<String, String>> hashData = getAllHash(redisKeyPattern);

            for (Map<String, String> field : hashData.values()) {
                int currentCount = Integer.parseInt(field.getOrDefault("currentCount", "0"));

                interviews.add(
                        AnalysisParserResponse.RedisAnalysisResult.builder()
                                .question(field.getOrDefault("question", ""))
                                .answer(field.getOrDefault("answer", ""))
                                .questionNumber(field.getOrDefault("questionNumber", ""))
                                .currentCount(currentCount)
                                .build()
                );
            }
        }

        // totalCount 오름차순 정렬
        interviews.sort(Comparator.comparingInt(AnalysisParserResponse.RedisAnalysisResult::getCurrentCount));

        return interviews;
    }

    // Redis Hash 여러 field(key) 전체 가져오기
    public Map<String, Map<String, String>> getAllHash(String keyPattern) {
        Map<String, Map<String, String>> result = new HashMap<>();

        // redis에 있는 key 전체 스캔
        Set<String> keys = redisTemplate.keys(keyPattern + "*");
        if (keys == null) return result;

        for (String key : keys) {
            Map<String, String> hash = redisTemplate.<String, String>opsForHash().entries(key);
            result.put(key, hash);
        }

        return result;
    }

}
