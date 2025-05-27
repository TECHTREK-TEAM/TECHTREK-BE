package techtrek.domain.sessionInfo.service.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.OpenAiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final CreateStartInterviewBean createInterviewBean;
    private final CreateNewInterviewBean createNewInterview;
    private final UserRepository userRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;
    private final OpenAiService openAiService;

    public SessionInfoResponse.NewQuestion exec(String sessionId, String parentFieldId) {
        // 1. 세션 확인
        sessionInfoRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        String fieldId = UUID.randomUUID().toString();

        // 2. Redis에서 세션 질문 리스트 조회
        String newSessionKey = "interview:session:" + sessionId + ":new";
        List<String> sessionData = redisTemplate.opsForList().range(newSessionKey, 0, -1);
        if (sessionData == null || sessionData.isEmpty()) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        // 3. parentFieldId에 해당하는 질문 객체 찾기
        JSONObject parentQuestionObj = null;
        for (String data : sessionData) {
            JSONObject obj = new JSONObject(data);
            if (parentFieldId.equals(obj.getString("fieldId"))) {
                parentQuestionObj = obj;
                break;
            }
        }
        if (parentQuestionObj == null) {
            throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);
        }

        // 4. 부모 질문의 questionNumber 가져오기
        String parentQuestionNumber = parentQuestionObj.optString("questionNumber");

        // 5. 꼬리질문 번호 생성
        String tailCountKey = "interview:session:" + sessionId + ":tailCount:" + parentFieldId;
        String tailCountStr = redisTemplate.opsForValue().get(tailCountKey);
        int tailCount = tailCountStr == null ? 0 : Integer.parseInt(tailCountStr);
        tailCount++; // 새 꼬리질문 번호 증가
        redisTemplate.opsForValue().set(tailCountKey, String.valueOf(tailCount));

        String newTailQuestionNumber = parentQuestionNumber + "-" + tailCount;

        // 6. 꼬리질문 생성
        String parentQuestion = parentQuestionObj.optString("question");
        String parentAnswer = parentQuestionObj.optString("answer");

        String prompt = String.format(
                "다음은 지원자가 이전에 면접에서 질문을 받고 답변을 한 내용입니다:\n" +
                        "질문: %s\n" +
                        "답변: %s\n\n" +
                        "이 내용을 바탕으로, 해당 지원자에게 적합한 **CS(Computer Science) 기반 기술 꼬리질문 1개만** 만들어 주세요.\n\n" +
                        "조건:\n" +
                        "- 반드시 CS 관련 주제여야 합니다 (예: 운영체제, 네트워크, 데이터베이스, 자료구조, 알고리즘, 트랜잭션, 멀티스레딩 등).\n" +
                        "- 일반적인 언어나 개념 질문은 피하고, 깊이 있는 구체적 질문이어야 합니다.\n" +
                        "- 질문은 3~4문장 이내로 간결하게 작성하세요.\n" +
                        "- 앞에 번호, 기호 등은 붙이지 마세요.\n" +
                        "- 이전에 나온 질문과 중복되지 않도록 해주세요.\n",
                parentQuestion,
                parentAnswer
        );

        // GPT에게 질문 생성 요청하는 코드에서 사용
        String question = openAiService.askToGpt(prompt);


        // 7. Redis에 꼬리질문 저장 (JSON으로 변환 후 저장)
        try {
            // 꼬리질문 리스트 키
            String tailSessionKey = "interview:session:" + sessionId + ":tail";

            // 꼬리질문 개수 조회 (꼬리질문 리스트 크기)
            Long newCount = redisTemplate.opsForList().size(newSessionKey);
            Long tailCountCurrent = redisTemplate.opsForList().size(tailSessionKey);
            tailCountCurrent = (tailCountCurrent == null) ? 0 : tailCountCurrent;

            // 전체 질문 개수 (기본 질문 + 꼬리질문)
            Long totalCount = (newCount == null ? 0 : newCount) + tailCountCurrent + 1;

            // 꼬리질문 저장용 Map 생성
            Map<String, String> qaData = new HashMap<>();
            qaData.put("fieldId", fieldId);
            qaData.put("question", question);
            qaData.put("answer", "");
            qaData.put("questionNumber", newTailQuestionNumber);
            qaData.put("totalQuestionCount", String.valueOf(totalCount));

            // JSON 문자열 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(qaData);

            // Redis 꼬리질문 리스트에 저장
            redisTemplate.opsForList().rightPush(tailSessionKey, jsonString);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }

        // 9. NewQuestion 타입 응답으로 반환 (필요에 따라 변환 혹은 새 응답 클래스 생성)
        return new SessionInfoResponse.NewQuestion(fieldId, question, newTailQuestionNumber);
    }
}
