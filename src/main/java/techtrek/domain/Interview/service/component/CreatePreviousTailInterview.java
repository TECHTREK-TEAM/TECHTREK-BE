package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.Interview.service.common.NumberCountProvider;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.UUID;

// 두번째 이후 연계 질문
@Component
@RequiredArgsConstructor
public class CreatePreviousTailInterview {
    private static final String PROMPT_PATH_TAIL = "prompts/tail_question_prompt.txt";

    private final RedisTemplate<String, String> redisTemplate;
    private final UserValidator userValidator;
    private final NumberCountProvider numberCountProvider;
    private final Gpt gpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    public InterviewResponse.TailQuestion exec(String sessionId, String previousId, CustomUserDetails userDetails) {
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        // 키, 변수 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String previousKey =  sessionKey + tailPrefix + previousId;
        String tailKey =  sessionKey + tailPrefix + fieldId;
        String previousQuestion, previousAnswer, parentQuestionNumber;

        // previous 필드 값 조회
        if (redisTemplate.hasKey(previousKey)) {
            previousQuestion = (String) redisTemplate.opsForHash().get(previousKey, "question");
            previousAnswer = (String) redisTemplate.opsForHash().get(previousKey, "answer");
            parentQuestionNumber = (String) redisTemplate.opsForHash().get(previousKey, "parentQuestionNumber");
        } else {
            throw new CustomException(ErrorCode.PREVIOUS_FIELD_NOT_FOUND);
        }

        // gpt 연계질문 생성
        InterviewParserResponse.ChatResult result = gpt.exec(PROMPT_PATH_TAIL, new Object[]{previousQuestion, previousAnswer}, InterviewParserResponse.ChatResult.class);

        // 꼬리 질문, 질문 번호
        String tailCountKey = sessionKey + ":count:" + parentQuestionNumber;
        Long tailNumber = redisTemplate.opsForValue().increment(tailCountKey);
        String tailQuestionNumber= String.valueOf(tailNumber);
        String questionNumber = parentQuestionNumber + "-" + tailQuestionNumber;

        // count 계산
        InterviewParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);

        // redis에 저장
        redisTemplate.opsForHash().put(tailKey, "question", result.getQuestion());
        redisTemplate.opsForHash().put(tailKey, "correctAnswer", result.getCorrectAnswer());
        redisTemplate.opsForHash().put(tailKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(tailKey, "currentCount", numberCount.getCurrentCount());
        redisTemplate.opsForHash().put(tailKey, "parentQuestionNumber", parentQuestionNumber);
        redisTemplate.opsForHash().put(tailKey, "tailQuestionNumber", tailQuestionNumber);

        return InterviewResponse.TailQuestion.builder()
                .fieldId(fieldId)
                .question(result.getQuestion())
                .parentQuestionNumber(parentQuestionNumber)
                .tailQuestionNumber(tailQuestionNumber)
                .build();
    }
}
