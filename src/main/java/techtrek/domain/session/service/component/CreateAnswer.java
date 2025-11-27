package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.global.openAI.Embedding.service.common.Embedding;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAnswer {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserValidator userValidator;
    private final SessionRedisHelper sessionRedisHelper;
    private final Embedding embedding;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 답변하기
    public Boolean exec(String sessionId, String answer, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;

        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // Redis 값 조회
        int mainNumber = sessionRedisHelper.getIntField(sessionKey, "mainNumber");
        int subNumber = sessionRedisHelper.getIntField(sessionKey, "subNumber");

        // QA 키 생성
        String qaKey = sessionRedisHelper.buildQaKey(sessionId, mainNumber, subNumber);

        // 정답 꺼내오기
        String correctAnswer = (String) redisTemplate.opsForHash().get(qaKey, "correctAnswer");
        if (correctAnswer == null) throw new CustomException(ErrorCode.FIELD_NOT_FOUND);

        // 유사도 계산
        double similarity = computeSimilarity(answer, correctAnswer);

        // redis 저장
        redisTemplate.opsForHash().put(qaKey, "answer", answer);
        redisTemplate.opsForHash().put(qaKey, "similarity", String.valueOf(similarity));

        return true;
    };

    // 유사도 계산
    private double computeSimilarity(String answer, String correctAnswer) {
        List<Double> vec1 = embedding.getEmbedding(answer);
        List<Double> vec2 = embedding.getEmbedding(correctAnswer);
        return embedding.cosineSimilarity(vec1, vec2);
    }
}
