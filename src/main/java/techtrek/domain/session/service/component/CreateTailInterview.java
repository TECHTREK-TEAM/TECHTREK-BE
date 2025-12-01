package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;

// 두번째 이후 연계 질문
@Component
@RequiredArgsConstructor
public class CreateTailInterview {
    private static final String PROMPT_PATH_TAIL = "prompts/tail_question_prompt.txt";

    private final RedisTemplate<String, String> redisTemplate;
    private final SessionRedisHelper sessionRedisHelper;
    private final UserHelper userHelper;
    private final Gpt gpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public SessionResponse.TailQuestion exec(String sessionId, CustomUserDetails userDetails) {
        // 사용자 검증
        userHelper.validateUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;

        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // Redis 값 조회
        int mainNumber = sessionRedisHelper.getIntField(sessionKey, "mainNumber");
        int subNumber = sessionRedisHelper.getIntField(sessionKey, "subNumber");
        int currentCount = sessionRedisHelper.getIntField(sessionKey, "currentCount");

        // 다음 질문 번호
        int nextSubNumber = subNumber + 1;
        int nextCurrentCount = currentCount + 1;

        // QA 이전 데이터 조회
        String previousQaKey = sessionRedisHelper.buildQaKey(sessionId, mainNumber,subNumber);
        String previousQuestion = (String) redisTemplate.opsForHash().get(previousQaKey, "question");
        String previousAnswer = (String) redisTemplate.opsForHash().get(previousQaKey, "answer");


        // QA 키 생성
        String qaKey = sessionRedisHelper.buildQaKey(sessionId, mainNumber,nextSubNumber);

        // gpt 연계질문 생성
        //SessionParserResponse.ChatResult result = gpt.exec(PROMPT_PATH_TAIL, new Object[]{previousQuestion, previousAnswer}, SessionParserResponse.ChatResult.class);

        // redis에 저장
        redisTemplate.opsForHash().put(sessionKey, "subNumber", String.valueOf(nextSubNumber));
        redisTemplate.opsForHash().put(sessionKey, "currentCount", String.valueOf(nextCurrentCount));

        redisTemplate.opsForHash().put(qaKey, "question",  "하하");
        redisTemplate.opsForHash().put(qaKey, "correctAnswer","히히");
        redisTemplate.opsForHash().put(qaKey, "type", "tail");

        return SessionResponse.TailQuestion.builder()
                .question("하하")
                .parentQuestionNumber(String.valueOf(mainNumber))
                .tailQuestionNumber(String.valueOf(nextSubNumber))
                .build();
    }
}
