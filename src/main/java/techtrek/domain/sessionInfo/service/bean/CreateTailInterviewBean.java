package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.bean.small.CheckSessionInfoDAOBean;
import techtrek.domain.sessionInfo.service.bean.small.CreateRedisDTOBean;
import techtrek.domain.sessionInfo.service.bean.small.GetRedisSessionListDAOBean;
import techtrek.global.bean.manager.CreateJsonWriteManager;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.bean.manager.CreatePromptManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptTemplateManager;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final RedisTemplate<String, String> redisTemplate;

    private final CreatePromptTemplateManager createPromptTemplateManager;
    private final CreatePromptManager createPromptManager;
    private final CreateJsonWriteManager createJsonWriteManager;

    private final CreateRedisDTOBean createRedisDTOBean;
    private final CheckSessionInfoDAOBean checkSessionInfoDAOBean;
    private final GetRedisSessionListDAOBean getRedisSessionListDAOBean;

    // 꼬리질문 생성
    public SessionInfoResponse.NewQuestion exec(String sessionId, String parentId) {
        // 세션 존재 확인
        checkSessionInfoDAOBean.exec(sessionId);

        // 세션키, 꼬리키 생성
        String sessionKey = "interview:session:" + sessionId + ":new";
        String tailSessionKey = "interview:session:" + sessionId + ":tail";
        String tailCountKey = "interview:session:" + sessionId + ":tailCount:" + parentId;
        String fieldId = UUID.randomUUID().toString();

        // 세션 리스트 불러오기
        List<String> sessionData = getRedisSessionListDAOBean.exec(sessionKey);

        // parenId에 해당하는 질문 객체 찾기
        JSONObject parentQuestionObj = null;
        for (String data : sessionData) {
            JSONObject obj = new JSONObject(data);
            if (parentId.equals(obj.getString("fieldId"))) {
                parentQuestionObj = obj;
                break;
            }
        }
        if (parentQuestionObj == null) throw new CustomException(ErrorCode.PREVIOUS_QUESTION_NOT_FOUND);

        // 4. 부모 질문의 questionNumber 가져오기
        String parentQuestionNumber = parentQuestionObj.optString("questionNumber");
        String parentQuestion = parentQuestionObj.optString("question");
        String parentAnswer = parentQuestionObj.optString("answer");

        // 프롬프트 생성 후, 꼬리질문 생성
        String promptTemplate = createPromptTemplateManager.exec("prompts/tail_question_prompt.txt");
        String prompt = String.format(promptTemplate, parentQuestion, parentAnswer);
        String question = createPromptManager.exec(prompt);

        // 꼬리질문 번호 생성
        String tailCountStr = redisTemplate.opsForValue().get(tailCountKey);
        int tailCount = tailCountStr == null ? 0 : Integer.parseInt(tailCountStr);
        tailCount++; // 새 꼬리질문 번호 증가
        redisTemplate.opsForValue().set(tailCountKey, String.valueOf(tailCount));

        String newTailCount = parentQuestionNumber + "-" + tailCount;

        // redis에 저장
        // 꼬리질문 개수 조회 (꼬리질문 리스트 크기)
        Long newCount = redisTemplate.opsForList().size(sessionKey);
        Long tailCountCurrent = redisTemplate.opsForList().size(tailSessionKey);
        tailCountCurrent = (tailCountCurrent == null) ? 0 : tailCountCurrent;

        // 전체 질문 개수 (기본 질문 + 꼬리질문)
        Long totalCount = (newCount == null ? 0 : newCount) + tailCountCurrent + 1;

        // 꼬리질문 저장용 DTO 생성
        String totalQuestionCount = String.valueOf(totalCount);
        RedisRequest.TailQuestion dto = createRedisDTOBean.exec(fieldId, question, newTailCount, totalQuestionCount);

        // JSON 직렬화
        String jsonString = createJsonWriteManager.exec(dto);

        // Redis 꼬리질문 리스트에 저장
        redisTemplate.opsForList().rightPush(tailSessionKey, jsonString);

        return new SessionInfoResponse.NewQuestion(fieldId, question, newTailCount, dto.getTotalQuestionCount());
    }
}
