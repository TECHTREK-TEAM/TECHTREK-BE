package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.bean.small.CheckSessionInfoDAOBean;
import techtrek.global.gpt.service.bean.manager.CreatePromptManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptTemplateManager;
import techtrek.global.redis.service.bean.small.GetRedisDAOBean;
import techtrek.global.redis.service.bean.small.GetRedisTotalNumberDAOBean;
import techtrek.global.redis.service.bean.small.SaveTailQuestionDAOBean;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final RedisTemplate<String, String> redisTemplate;

    private final CreatePromptTemplateManager createPromptTemplateManager;
    private final CreatePromptManager createPromptManager;

    private final CheckSessionInfoDAOBean checkSessionInfoDAOBean;
    private final GetRedisTotalNumberDAOBean getRedisTotalNumberDAOBean;
    private final SaveTailQuestionDAOBean saveTailQuestionDAOBean;
    private final GetRedisDAOBean getRedisDAOBean;

    // 꼬리질문 생성
    public SessionInfoResponse.TailQuestion exec(String sessionId, String parentId, String previousFieldId) {
        // 세션 존재 확인
        checkSessionInfoDAOBean.exec(sessionId);

        // 키 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String fieldKey =  sessionKey + ":tail:" + fieldId;
        String previousQuestion;
        String previousAnswer;

        // 부모 질문 번호 조회
        RedisResponse.FieldData parentData = getRedisDAOBean.exec(sessionKey + ":new:"+ parentId);
        String parentQuestionNumber = parentData.getQuestionNumber();

        // 질문, 답변 조회
        if(previousFieldId != null){
            RedisResponse.FieldData previousData = getRedisDAOBean.exec(sessionKey+":tail:"+previousFieldId);
            previousQuestion = previousData.getQuestion();
            previousAnswer = previousData.getAnswer();
        } else {
            RedisResponse.FieldData previousData = getRedisDAOBean.exec(sessionKey+":new:"+parentId);
            previousQuestion = previousData.getQuestion();
            previousAnswer = previousData.getAnswer();
        }

        // 총 질문 개수 조회
        int newCount = getRedisTotalNumberDAOBean.exec(sessionKey + ":new");
        int tailCount = getRedisTotalNumberDAOBean.exec(sessionKey + ":tail");
        int totalQuestionNumber = newCount + tailCount;
        String resultTotalQuestionNumber= String.valueOf(totalQuestionNumber);

        // 프롬프트 생성 후, 꼬리질문 생성
        String promptTemplate = createPromptTemplateManager.exec("prompts/tail_question_prompt.txt");
        String prompt = String.format(promptTemplate, previousQuestion, previousAnswer);
        String question = createPromptManager.exec(prompt);

        // 꼬리질문 개수
        String tailCountKey = "interview:session:" + sessionId + ":tailCount:" + parentQuestionNumber;
        Long tailQuestionNumber = redisTemplate.opsForValue().increment(tailCountKey);
        String resultTailQuestionNumber= String.valueOf(tailQuestionNumber);

        // redis에 저장
        saveTailQuestionDAOBean.exec(fieldKey, question, parentQuestionNumber,resultTailQuestionNumber,resultTotalQuestionNumber);


        return new SessionInfoResponse.TailQuestion(fieldId, question, parentQuestionNumber, resultTailQuestionNumber, resultTotalQuestionNumber);
    }
}
