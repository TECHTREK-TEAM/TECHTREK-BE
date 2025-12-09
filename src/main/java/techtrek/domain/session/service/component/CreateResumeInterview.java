package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.helper.CompanyCSHelper;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;

// 이력서 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateResumeInterview {
    private static final String PROMPT_PATH_RESUME = "prompts/resume_question_prompt.txt";

    private final RedisTemplate<String, String> redisTemplate;
    private final SessionRedisHelper sessionRedisHelper;
    private final CompanyCSHelper companyCSHelper;
    private final UserHelper userHelper;
    private final Gpt gpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public SessionResponse.Question exec(String sessionId, CustomUserDetails userDetails){
        // 사용자 조회
        User user = userHelper.validateUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;

        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // Redis 값 조회
        int mainNumber = sessionRedisHelper.getIntField(sessionKey, "mainNumber");
        int currentCount = sessionRedisHelper.getIntField(sessionKey, "currentCount");

        // 다음 질문 번호
        int nextMainNumber = mainNumber + 1;
        int nextCurrentCount = currentCount + 1;

        // QA 키 생성
        String qaKey = sessionRedisHelper.buildQaKey(sessionId, nextMainNumber,0);

        // enterpriseName 조회 및 유효성 검증
        Enterprise enterprise = sessionRedisHelper.getEnterprise(sessionKey);

        // 이력서 불러오기, 예외처리
        String resume = user.getResume();
        if (resume == null || resume.isBlank()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // 기업뱔 중요 CS
        String focusCS = companyCSHelper.exec(enterprise.getName());

        // gpt 질문 생성
        SessionParserResponse.ChatResult result = gpt.exec(PROMPT_PATH_RESUME, new Object[]{resume,enterprise.getName(), focusCS}, SessionParserResponse.ChatResult.class);

        // redis 저장
        redisTemplate.opsForHash().put(sessionKey, "mainNumber",  String.valueOf(nextMainNumber));
        redisTemplate.opsForHash().put(sessionKey, "subNumber", "0");
        redisTemplate.opsForHash().put(sessionKey, "currentCount", String.valueOf(nextCurrentCount));
        redisTemplate.opsForHash().put(qaKey, "question",  result.getQuestion());
        redisTemplate.opsForHash().put(qaKey, "correctAnswer", result.getCorrectAnswer());
        redisTemplate.opsForHash().put(qaKey, "type", "resume");

        return SessionResponse.Question.builder()
                .question(result.getQuestion())
                .questionNumber(String.valueOf(nextMainNumber))
                .build();
    }

}
