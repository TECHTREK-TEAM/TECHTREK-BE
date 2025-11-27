package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
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

    @Value("${custom.redis.prefix.qa}")
    private String qa;

    // 답변하기
    public Boolean exec(String sessionId, String answer, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        // 키 생성
        String sessionKey = interviewPrefix + sessionId;

        // 해당 키 존재 확인
        if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) throw new CustomException(ErrorCode.FIELD_NOT_FOUND);

        // mainNumber, subNumber 조회
        Object mainObj = redisTemplate.opsForHash().get(sessionKey, "mainNumber");
        Integer mainNumber = mainObj != null ? Integer.valueOf(mainObj.toString()) : null;

        Object subObj = redisTemplate.opsForHash().get(sessionKey, "subNumber");
        Integer subNumber = subObj != null ? Integer.valueOf(subObj.toString()) : null;


        // QA Key 생성 (subNumber가 있으면 붙이기)
        String qaKey = interviewPrefix + sessionId + ":qa:" + mainNumber;
        if (subNumber != null) qaKey += ":" + subNumber;

        // 유사도 검사
        String correctAnswer = (String) redisTemplate.opsForHash().get(qaKey, "correctAnswer");
        List<Double> vec1 = embedding.getEmbedding(answer);
        List<Double> vec2 = embedding.getEmbedding(correctAnswer);
        double similarity = embedding.cosineSimilarity(vec1, vec2);

        // 답변, 유사도 저장
        redisTemplate.opsForHash().put(qaKey, "answer", answer);
        redisTemplate.opsForHash().put(qaKey, "similarity", String.valueOf(similarity));

        return true;
    };
}
