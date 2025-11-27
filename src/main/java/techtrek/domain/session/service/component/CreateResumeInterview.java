package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.common.CompanyCSProvider;
import techtrek.domain.session.service.common.NumberCountProvider;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.user.entity.User;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.UUID;

// 이력서 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateResumeInterview {
    private static final String PROMPT_PATH_RESUME = "prompts/resume_question_prompt.txt";

    private final UserValidator userValidator;
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CompanyCSProvider companyCSProvider;
    private final NumberCountProvider numberCountProvider;
    private final Gpt gpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    public SessionResponse.Question exec(String sessionId, CustomUserDetails userDetails){
        // key 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String resumeKey = sessionKey + resumePrefix+ fieldId;

        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 세션 유효성 확인
        if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // 이력서 불러오기, 예외처리
        String resume = user.getResume();
        if (resume == null || resume.isBlank()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // enterpriseName 조회 및 유효성 검증
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 기업뱔 중요 CS
        String focusCS = companyCSProvider.exec(enterprise.getName());

        // gpt 질문 생성
        SessionParserResponse.ChatResult result = gpt.exec(PROMPT_PATH_RESUME, new Object[]{resume,enterprise.getName(), focusCS}, SessionParserResponse.ChatResult.class);

        // questionNumber, count 계산
        SessionParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);

        // redis 저장
        redisTemplate.opsForHash().put(resumeKey, "question",  result.getQuestion());
        redisTemplate.opsForHash().put(resumeKey, "correctAnswer", result.getCorrectAnswer());
        redisTemplate.opsForHash().put(resumeKey, "questionNumber", numberCount.getQuestionNumber());
        redisTemplate.opsForHash().put(resumeKey, "currentCount", numberCount.getCurrentCount());

        return SessionResponse.Question.builder()
                //.fieldId(fieldId)
                .question(result.getQuestion())
                .questionNumber(numberCount.getQuestionNumber())
                .build();
    }

}
