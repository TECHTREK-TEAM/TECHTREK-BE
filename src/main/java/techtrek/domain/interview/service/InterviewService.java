package techtrek.domain.interview.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import techtrek.domain.interview.dto.InterviewResponse;
import techtrek.domain.interview.entity.BasicQuestion;
import techtrek.domain.interview.repository.BasicQuestionRepository;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;

import java.util.UUID;

@RequiredArgsConstructor
@Builder
@Service
public class InterviewService {

    private final UserRepository userRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;

    //면접 시작하기
    public InterviewResponse.Start startInterview(String enterpriseName, EnterpriseType enterpriseType) {

        //  User user = userRepository.findById(UserId)
        User user = userRepository.findById("1")
                .orElseThrow(() -> new GlobalException(ResponseCode.USER_NOT_FOUND));

        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();

        // 1. 데이터베이스에서 랜덤으로 하나의 질문을 가져옴
        BasicQuestion getQuestion = basicQuestionRepository.findRandomQuestion()
                .orElseThrow(() -> new GlobalException(ResponseCode.BASIC_QUESTION_NOT_FOUND));

        String basicQuestion = getQuestion.getQuestion();

        // 2. 세션 안에 첫 질문/답변 저장
        String sessionKey = "interview:session:" + sessionId;
        redisTemplate.opsForHash().put(sessionKey, fieldId + ":question", basicQuestion);
        redisTemplate.opsForHash().put(sessionKey, fieldId + ":answer", "");

        // 3. 세션정보 테이블에 값 저장
        SessionInfo sessionInfo = SessionInfo.builder()
                .id(UUID.randomUUID().toString())
                .sessionId(sessionId)
                .enterpriseName(enterpriseName)
                .enterpriseType(enterpriseType)
                .user(user)
                .build();

        sessionInfoRepository.save(sessionInfo);

        return new InterviewResponse.Start(sessionId,fieldId,basicQuestion);
    }

}
