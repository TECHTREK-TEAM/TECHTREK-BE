package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.service.common.HashCountProvider;
import techtrek.domain.Interview.service.common.ResumeQuestion;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.UUID;

// 이력서 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateResumeInterview {
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ResumeQuestion resumeQuestion;
    private final HashCountProvider hashCountProvider;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    public InterviewResponse.Question exec(String sessionId){
        // key 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String resumeKey = sessionKey + resumePrefix+ fieldId;

        // TODO: 사용자 조회
        User user = userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 세션 유효성 확인
        Boolean hasSession = redisTemplate.hasKey(sessionKey);
        if (hasSession == null || !hasSession) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // 이력서 불러오기, 예외처리
        String resume = user.getResume();
        if (resume == null || resume.isBlank()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // enterpriseName 조회 및 유효성 검증
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NAME_NOT_FOUND));

        // 이력서 질문 생성
        ParserResponse.BasicQuestionResult basicQuestionResult = resumeQuestion.exec(resume,enterprise);

        // basic + resume 필드 개수 세기
        long basicCount = hashCountProvider.exec(sessionKey + basicPrefix + "*");
        long resumeCount = hashCountProvider.exec(sessionKey + resumePrefix + "*");
        String questionNumber = String.valueOf(basicCount + resumeCount + 1);

        // redis 저장
        redisTemplate.opsForHash().put(resumeKey, "question",  basicQuestionResult.getQuestion());
        redisTemplate.opsForHash().put(resumeKey, "correctAnswer", basicQuestionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(resumeKey, "questionNumber", questionNumber);

        return InterviewResponse.Question.builder()
                .fieldId(fieldId)
                .question(basicQuestionResult.getQuestion())
                .questionNumber(questionNumber)
                .build();
    }

}
