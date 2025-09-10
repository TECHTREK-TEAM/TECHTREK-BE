package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.Interview.service.common.NumberCountProvider;
import techtrek.domain.Interview.service.common.TailQuestion;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.UUID;

// 두번째 이후 연계 질문
@Component
@RequiredArgsConstructor
public class CreatePreviousTailInterview {
    private final RedisTemplate<String, String> redisTemplate;
    private final TailQuestion tailQuestion;
    private final NumberCountProvider numberCountProvider;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    public InterviewResponse.TailQuestion exec(String sessionId, String previousId) {
        // 키, 변수 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String previousKey =  sessionKey + tailPrefix + previousId;
        String tailKey =  sessionKey + tailPrefix + fieldId;
        String parentQuestion, parentAnswer, parentQuestionNumber;

        // previous 필드 값 조회
        if (redisTemplate.hasKey(previousKey)) {
            parentQuestion = (String) redisTemplate.opsForHash().get(previousKey, "question");
            parentAnswer = (String) redisTemplate.opsForHash().get(previousKey, "answer");
            parentQuestionNumber = (String) redisTemplate.opsForHash().get(previousKey, "parentQuestionNumber");
        } else {
            throw new CustomException(ErrorCode.PREVIOUS_FIELD_NOT_FOUND);
        }

        // 연계질문 생성
        ParserResponse.ChatResult questionResult= tailQuestion.exec(parentQuestion,parentAnswer);

        // 꼬리 질문, 질문 번호
        String tailCountKey = sessionKey + ":count:" + parentQuestionNumber;
        Long tailNumber = redisTemplate.opsForValue().increment(tailCountKey);
        String tailQuestionNumber= String.valueOf(tailNumber);
        String questionNumber = parentQuestionNumber + "-" + tailQuestionNumber;

        // count 계산
        ParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);

        // redis에 저장
        redisTemplate.opsForHash().put(tailKey, "question", questionResult.getQuestion());
        redisTemplate.opsForHash().put(tailKey, "correctAnswer", questionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(tailKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(tailKey, "currentCount", numberCount.getCurrentCount());
        redisTemplate.opsForHash().put(tailKey, "parentQuestionNumber", parentQuestionNumber);
        redisTemplate.opsForHash().put(tailKey, "tailQuestionNumber", tailQuestionNumber);

        return InterviewResponse.TailQuestion.builder()
                .fieldId(fieldId)
                .question(questionResult.getQuestion())
                .parentQuestionNumber(parentQuestionNumber)
                .tailQuestionNumber(tailQuestionNumber)
                .build();
    }
}
