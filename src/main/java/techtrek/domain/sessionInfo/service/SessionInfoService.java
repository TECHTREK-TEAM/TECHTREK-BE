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
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import techtrek.global.gpt.service.OpenAiService;


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
    private final OpenAiService openAiService;

    //면접 시작하기
    public SessionInfoResponse.Start createInterview(EnterpriseName enterpriseName, EnterpriseType enterpriseType) {

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


    //새로운 질문 불러오기
    public SessionInfoResponse.NewQuestion getNewInterview(String sessionId) {
        // User user = userRepository.findById(userId)
        User user = userRepository.findById("1")
                .orElseThrow(() ->  new GlobalException(ResponseCode.USER_NOT_FOUND));
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
            SessionInfo sessionInfo = sessionInfoRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new GlobalException(ResponseCode.SESSIONID_NOT_FOUND));

            String resume = user.getResume(); // 사용자 이력서
            String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription(); // 기업 설명

            String prompt = String.format(
                    "다음은 지원자의 이력서입니다:\n%s\n\n" +
                            "해당 지원자는 \"%s\" 성향의 기업 면접을 준비 중입니다.\n" +
                            "이력서를 기반으로 한 **정확하고 구체적인 CS(Computer Science) 기반 기술 면접 질문 1개만** 생성해 주세요.\n\n" +
                            "조건:\n" +
                            "- '언어의 장단점'처럼 일반적인 질문은 피하세요.\n" +
                            "- 반드시 CS 기반 주제 (예: 운영체제, 네트워크, DB, 자료구조, 알고리즘, 트랜잭션, 멀티스레딩 등)여야 합니다.\n" +
                            "- 이전에 생성된 질문과 겹치지 않아야 합니다. 또한 질문의 길이는 3-4문장 이내로 작성하세요.\n" +
                            "- 앞에 번호, 기호 등은 붙이지 말고 **이력서를 보아하니, 이력서를 보니, 확인해본 결과 등의 문장을 섞어,** 질문해주세요." +
                            "- 너무 포괄적이지 않고 면접관이 깊이 파고들 수 있는 질문 형태로 출력하세요.\n\n",
                    resume,
                    enterpriseDescription
            );

            // GPT에게 질문 생성 요청하는 코드에서 사용
            question = openAiService.askToGpt(prompt);
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
    public Boolean createAnswer(String sessionId, String fieldId, String type, String answer) {
        // 1. 세션 키 결정 (new인지 tail인지에 따라 다름)
        String sessionKey = "interview:session:" + sessionId + ":" + type;

        // 2. 해당 Redis 리스트에서 모든 질문/답변 항목 조회
        List<String> jsonList = redisTemplate.opsForList().range(sessionKey, 0, -1);
        ObjectMapper objectMapper = new ObjectMapper();

        // 3. 리스트 순회하며 해당 fieldId를 가진 JSON 항목 찾기 및 수정
        for (int i = 0; i < jsonList.size(); i++) {
            try {
                Map<String, String> qaData = objectMapper.readValue(jsonList.get(i), Map.class);
                if (fieldId.equals(qaData.get("fieldId"))) {
                    // 3-1. answer 값 업데이트
                    qaData.put("answer", answer);

                    // 3-2. 다시 JSON 문자열로 직렬화
                    String updatedJson = objectMapper.writeValueAsString(qaData);

                    // 3-3. 해당 인덱스에 수정된 JSON 저장
                    redisTemplate.opsForList().set(sessionKey, i, updatedJson);

                    return true;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 처리 중 오류 발생", e);
            }
        }

        // 해당 fieldId를 찾지 못한 경우
        return false;
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
