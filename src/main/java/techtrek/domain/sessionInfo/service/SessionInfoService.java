package techtrek.domain.sessionInfo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
@Service
public class SessionInfoService {

    private final UserRepository userRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;

    //면접 시작하기
    public SessionInfoResponse.Start createInterview(String enterpriseName, EnterpriseType enterpriseType) {

        // 사용자 예외처리
        //  User user = userRepository.findById(UserId)
        User user = userRepository.findById("1")
                .orElseThrow(() -> new GlobalException(ResponseCode.USER_NOT_FOUND));

        // 1. 기본 설정
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;

        // 2. 질문 가져오기
        BasicQuestion getQuestion = basicQuestionRepository.findRandomQuestion()
                .orElseThrow(() -> new GlobalException(ResponseCode.BASIC_QUESTION_NOT_FOUND));
        String basicQuestion = getQuestion.getQuestion();

        // 3. 기본 + 이력서 질문 번호 계산 (기본질문과 이력서 질문 번호를 따로 관리)
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 4. 전체 질문 개수 계산 (기본 + 이력서 + 꼬리질문 포함)
        Long currentTotalCount = redisTemplate.opsForHash().size(sessionKey);
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 5. 묶기
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> qaData = new HashMap<>();
        qaData.put("fieldId", fieldId);
        qaData.put("question", basicQuestion);
        qaData.put("answer", "");
        qaData.put("questionNumber", questionNumber);
        qaData.put("count", "1");
        qaData.put("phase", "basic");
        qaData.put("totalQuestionCount", totalQuestionCount);

       // 6. JSON 문자열로 변환
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(qaData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }

       // 7. Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);

        // 8. 세션정보 테이블에 값 저장
        SessionInfo sessionInfo = SessionInfo.builder()
                .id(UUID.randomUUID().toString())
                .sessionId(sessionId)
                .enterpriseName(enterpriseName)
                .enterpriseType(enterpriseType)
                .user(user)
                .build();

        sessionInfoRepository.save(sessionInfo);

        return new SessionInfoResponse.Start(sessionId,fieldId,basicQuestion,questionNumber);
    }


    //기본 질문 불러오기
    public SessionInfoResponse.NewQuestion getNewInterview(String sessionId) {

        // 1. 기본 설정
        String sessionKey = "interview:session:" + sessionId;
        String fieldId = UUID.randomUUID().toString();

        // 2. Redis에서 최근 질문 가져오기 (가장 최근 질문을 가져오기 위해 리스트 끝에서 하나를 꺼냄)
        List<String> jsonStrings = redisTemplate.opsForList().range(sessionKey + ":new", -1, -1);
        String lastJsonString = jsonStrings.isEmpty() ? null : jsonStrings.get(0);

        // 초기화
        String question = "";
        String phase;
        int count = 0;

        // 2. Redis에서 최근 질문 가져오기
        if (lastJsonString != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> lastQaData = objectMapper.readValue(lastJsonString, Map.class);

                // 이전 phase와 count 가져오기
                phase = lastQaData.get("phase");
                count = Integer.parseInt(lastQaData.get("count"));

                // count가 5 이상이면 phase 전환
                if (count >= 5) {
                    phase = phase.equals("basic") ? "resume" : "basic";
                    count = 0;
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 파싱 실패", e);
            }
        } else {
            // 첫 질문인 경우
            phase = "basic";
            count = 0;
        }

// phase에 따른 질문 설정
        if (phase.equals("basic")) {
            BasicQuestion getQuestion = basicQuestionRepository.findRandomQuestion()
                    .orElseThrow(() -> new GlobalException(ResponseCode.BASIC_QUESTION_NOT_FOUND));
            question = getQuestion.getQuestion();
        } else {
            question = "이력서 기반 질문"; // 예시
        }

        // 3. 기본 + 이력서 질문 번호 계산
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 4. 전체 질문 개수 계산 (기본 + 이력서 + 꼬리질문 포함)
        Long currentTotalCount = redisTemplate.opsForList().size(sessionKey + ":new") + redisTemplate.opsForList().size(sessionKey + ":tail") ;
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 5. 묶기
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> qaData = new HashMap<>();
        qaData.put("fieldId", fieldId);
        qaData.put("question", question);
        qaData.put("answer", "");
        qaData.put("questionNumber", questionNumber);
        qaData.put("count", String.valueOf(count + 1));
        qaData.put("phase", phase);
        qaData.put("totalQuestionCount", totalQuestionCount);
        qaData.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });

        // 6. JSON 문자열로 변환
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(qaData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }

        // 7. Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);


        // 8. 기본 질문 반환
        return new SessionInfoResponse.NewQuestion(fieldId, question, questionNumber);
    }


    //답변하기
    public Boolean createAnswer(String sessionId, String fieldId, String basicQuestion) {

        // 2. 세션 안에 첫 질문/답변 저장
        String sessionKey = "interview:session:" + sessionId;
        redisTemplate.opsForHash().put(sessionKey, fieldId + ":answer", basicQuestion);


        return true;
    }
}


// 꼬리질문 참고
//public SessionInfoResponse.Start createInterview(String enterpriseName, EnterpriseType enterpriseType) {
//
//    // 사용자 예외처리
//    User user = userRepository.findById("1")
//            .orElseThrow(() -> new GlobalException(ResponseCode.USER_NOT_FOUND));
//
//    // 1. 기본 설정
//    String sessionId = UUID.randomUUID().toString();
//    String fieldId = UUID.randomUUID().toString();
//    String sessionKey = "interview:session:" + sessionId;
//
//    // 2. 질문 가져오기
//    BasicQuestion getQuestion = basicQuestionRepository.findRandomQuestion()
//            .orElseThrow(() -> new GlobalException(ResponseCode.BASIC_QUESTION_NOT_FOUND));
//    String basicQuestion = getQuestion.getQuestion();
//
//    // 3. 기본 + 이력서 질문 번호 계산 (기본질문과 이력서 질문 번호를 따로 관리)
//    Long currentBasicQuestionCount = redisTemplate.opsForHash().size(sessionKey + ":basic") / 6; // 기본 + 이력서 질문은 6개 항목 기준
//    String basicQuestionNumber = String.valueOf(currentBasicQuestionCount + 1);
//
//    // 4. 꼬리질문 번호 계산
//    String parentId = basicQuestionNumber; // 부모 질문 번호가 기본 질문 번호
//    String tailKey = "interview:session:" + sessionId + ":tail:" + parentId;
//    Long currentTailCount = redisTemplate.opsForValue().get(tailKey) != null
//            ? Long.valueOf(redisTemplate.opsForValue().get(tailKey))
//            : 0L;
//
//    // 꼬리질문 번호 (부모-꼬리질문 형식, 예: 1-1, 1-2, 2-1, ...)
//    String tailQuestionNumber = parentId + "-" + (currentTailCount + 1);
//
//    // 5. 전체 질문 개수 계산 (기본 + 이력서 + 꼬리질문 포함)
//    Long currentTotalCount = redisTemplate.opsForHash().size(sessionKey) / 6; // 전체 질문 개수 (기본 + 이력서 + 꼬리질문 포함)
//    String totalQuestionCount = String.valueOf(currentTotalCount + 1);
//
//    // 6. Redis에 질문 및 관련 정보 저장
//    redisTemplate.opsForHash().put(sessionKey + ":basic", fieldId + ":question", basicQuestion);
//    redisTemplate.opsForHash().put(sessionKey + ":basic", fieldId + ":questionNumber", basicQuestionNumber);
//    redisTemplate.opsForHash().put(sessionKey + ":basic", fieldId + ":totalQuestionCount", totalQuestionCount);
//
//    // 꼬리질문 번호 저장
//    redisTemplate.opsForHash().put(sessionKey + ":tail", fieldId + ":questionNumber", tailQuestionNumber);
//
//    // 꼬리질문 카운트 증가
//    redisTemplate.opsForValue().set(tailKey, String.valueOf(currentTailCount + 1));
//
//    // 7. 세션정보 테이블에 세션 정보 저장
//    SessionInfo sessionInfo = SessionInfo.builder()
//            .id(UUID.randomUUID().toString())
//            .sessionId(sessionId)
//            .enterpriseName(enterpriseName)
//            .enterpriseType(enterpriseType)
//            .user(user)
//            .build();
//
//    sessionInfoRepository.save(sessionInfo);
//
//    // 8. 응답 반환
//    return new SessionInfoResponse.Start(sessionId, fieldId, basicQuestion);
//}
