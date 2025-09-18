package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.user.entity.User;
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
    private final Embedding embedding;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 답변하기
    public Boolean exec(String sessionId, String fieldId, String type, String answer, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        // 키 생성
        String fieldKey = interviewPrefix + sessionId + ":" + type + ":"+ fieldId;

        // 해당 키 존재 확인
        if (Boolean.FALSE.equals(redisTemplate.hasKey(fieldKey))) throw new CustomException(ErrorCode.FIELD_NOT_FOUND);

        // 유사도 검사
        String correctAnswer = (String) redisTemplate.opsForHash().get(fieldKey, "correctAnswer");
        List<Double> vec1 = embedding.getEmbedding(answer);
        List<Double> vec2 = embedding.getEmbedding(correctAnswer);
        double similarity = embedding.cosineSimilarity(vec1, vec2);

        // 답변, 유사도 저장
        redisTemplate.opsForHash().put(fieldKey, "answer", answer);
        redisTemplate.opsForHash().put(fieldKey, "similarity", String.valueOf(similarity));

        return true;
    };
}
