package techtrek.domain.analysis.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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

    public List<AnalysisParserResponse.RedisAnalysisResult> exec(String sessionId) {
        String sessionKeyPrefix = interviewPrefix + sessionId;
        List<String> interviewTypes = List.of(basicPrefix, resumePrefix, tailPrefix);

        // 1. 키 목록 생성
        List<String> keys = new ArrayList<>();
        for (String type : interviewTypes) {
            // 와일드카드 대신 구체적 키 패턴 수집
            Set<String> matchedKeys = redisTemplate.keys(sessionKeyPrefix + type + "*");
            if (matchedKeys != null) keys.addAll(matchedKeys);
        }

        // 2. 파이프라인 사용해서 한 번에 모든 Hash 가져오기
        List<Object> hashResults = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                RedisOperations<String, String> stringOps = (RedisOperations<String, String>) operations;
                for (String key : keys) {
                    stringOps.opsForHash().entries(key);
                }
                return null;
            }
        });


        // 3. 결과 변환
        List<AnalysisParserResponse.RedisAnalysisResult> interviews = new ArrayList<>();
        for (Object obj : hashResults) {
            @SuppressWarnings("unchecked")
            Map<String, String> field = (Map<String, String>) obj;
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

        // totalCount 기준 오름차순 정렬
        interviews.sort(Comparator.comparingInt(AnalysisParserResponse.RedisAnalysisResult::getCurrentCount));

        return interviews;
    }
}
