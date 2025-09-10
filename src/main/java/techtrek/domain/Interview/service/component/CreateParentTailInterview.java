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

// 첫번째 연계질문
@Component
@RequiredArgsConstructor
public class CreateParentTailInterview {
    private final RedisTemplate<String, String> redisTemplate;
    private final TailQuestion tailQuestion;
    private final NumberCountProvider numberCountProvider;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    public InterviewResponse.TailQuestion exec(String sessionId, String parentId) {
        // 키, 변수 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String basicKey =  sessionKey + basicPrefix + parentId;
        String resumeKey =  sessionKey + resumePrefix + parentId;
        String tailKey =  sessionKey + tailPrefix + fieldId;
        String parentQuestion, parentAnswer, parentQuestionNumber;

        // parent 필드 값 조회
        if (redisTemplate.hasKey(basicKey)) {
            parentQuestion = (String) redisTemplate.opsForHash().get(basicKey, "question");
            parentAnswer = (String) redisTemplate.opsForHash().get(basicKey, "answer");
            parentQuestionNumber = (String) redisTemplate.opsForHash().get(basicKey, "questionNumber");
        } else if (redisTemplate.hasKey(resumeKey)) {
            parentQuestion = (String) redisTemplate.opsForHash().get(resumeKey, "question");
            parentAnswer = (String) redisTemplate.opsForHash().get(resumeKey, "answer");
            parentQuestionNumber = (String) redisTemplate.opsForHash().get(resumeKey, "questionNumber");
        } else {
            throw new CustomException(ErrorCode.PARENT_FIELD_NOT_FOUND);
        }

        // 연계질문 생성
        ParserResponse.ChatResult questionResult= tailQuestion.exec(parentQuestion,parentAnswer);

        // 꼬리 질문, 질문 번호
        String tailCountKey = sessionKey + ":count:" + parentQuestionNumber;
        Long tailNumber = redisTemplate.opsForValue().increment(tailCountKey);
        String tailQuestionNumber= String.valueOf(tailNumber);
        String questionNumber = parentQuestionNumber + "-" + tailQuestionNumber;

        // totalQuestionNumber 계산
        ParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);

        // redis에 저장
        redisTemplate.opsForHash().put(tailKey, "question", questionResult.getQuestion());
        redisTemplate.opsForHash().put(tailKey, "correctAnswer", questionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(tailKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(tailKey, "totalCount", numberCount.getTotalCount());
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
