package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.global.redis.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.bean.small.CheckSessionInfoDAOBean;
import techtrek.global.gpt.service.bean.util.CreatePromptUtil;
import techtrek.global.gpt.service.bean.util.CreatePromptTemplateUtil;
import techtrek.global.redis.service.bean.small.GetRedisDAOBean;
import techtrek.global.redis.service.bean.small.GetRedisTotalNumberDAOBean;
import techtrek.global.redis.service.bean.small.GetTailNumberDAOBean;
import techtrek.global.redis.service.bean.small.SaveTailQuestionDAOBean;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final CreatePromptTemplateUtil createPromptTemplateUtil;
    private final CreatePromptUtil createPromptUtil;

    private final CheckSessionInfoDAOBean checkSessionInfoDAOBean;
    private final GetRedisTotalNumberDAOBean getRedisTotalNumberDAOBean;
    private final SaveTailQuestionDAOBean saveTailQuestionDAOBean;
    private final GetTailNumberDAOBean getTailNumberDAOBean;
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
        int totalData = newCount + tailCount + 1;
        String totalQuestionNumber= String.valueOf(totalData);

        // 프롬프트 생성 후, 꼬리질문 생성
        String promptTemplate = createPromptTemplateUtil.exec("prompts/tail_question_prompt.txt");
        String prompt = String.format(promptTemplate, previousQuestion, previousAnswer);
        String question = createPromptUtil.exec(prompt);

        // 꼬리질문 개수
       String tailQuestionNumber = getTailNumberDAOBean.exec(sessionKey, parentQuestionNumber);

        // redis에 저장
        saveTailQuestionDAOBean.exec(fieldKey, question, parentQuestionNumber,tailQuestionNumber,totalQuestionNumber);


        return new SessionInfoResponse.TailQuestion(fieldId, question, parentQuestionNumber, tailQuestionNumber, totalQuestionNumber);
    }
}
