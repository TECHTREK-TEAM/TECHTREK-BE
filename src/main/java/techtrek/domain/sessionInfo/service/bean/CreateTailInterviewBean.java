package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.service.dao.CheckSessionInfoDAO;
import techtrek.global.gpt.service.bean.util.CreatePromptUtil;
import techtrek.global.gpt.service.bean.util.CreatePromptTemplateUtil;
import techtrek.global.redis.service.dao.GetRedisDAO;
import techtrek.global.redis.service.dao.GetRedisTotalNumberDAO;
import techtrek.global.redis.service.dao.GetTailNumberDAO;
import techtrek.global.redis.service.dao.SaveTailQuestionDAO;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final CreatePromptTemplateUtil createPromptTemplateUtil;
    private final CreatePromptUtil createPromptUtil;

    private final CheckSessionInfoDAO checkSessionInfoDAO;
    private final GetRedisTotalNumberDAO getRedisTotalNumberDAO;
    private final SaveTailQuestionDAO saveTailQuestionDAO;
    private final GetTailNumberDAO getTailNumberDAO;
    private final GetRedisDAO getRedisDAO;

    // 꼬리질문 생성
    public SessionInfoResponse.TailQuestion exec(String sessionId, String parentId, String previousFieldId) {
        // 세션 존재 확인
        checkSessionInfoDAO.exec(sessionId);

        // 키 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String fieldKey =  sessionKey + ":tail:" + fieldId;
        String previousQuestion;
        String previousAnswer;

        // 부모 질문 번호 조회
        SessionParserResponse.FieldData parentData = getRedisDAO.exec(sessionKey + ":new:"+ parentId);
        String parentQuestionNumber = parentData.getQuestionNumber();

        // 질문, 답변 조회
        if(previousFieldId != null){
            SessionParserResponse.FieldData previousData = getRedisDAO.exec(sessionKey+":tail:"+previousFieldId);
            previousQuestion = previousData.getQuestion();
            previousAnswer = previousData.getAnswer();
        } else {
            SessionParserResponse.FieldData previousData = getRedisDAO.exec(sessionKey+":new:"+parentId);
            previousQuestion = previousData.getQuestion();
            previousAnswer = previousData.getAnswer();
        }

        // 총 질문 개수 조회
        int newCount = getRedisTotalNumberDAO.exec(sessionKey + ":new");
        int tailCount = getRedisTotalNumberDAO.exec(sessionKey + ":tail");
        int totalData = newCount + tailCount + 1;
        String totalQuestionNumber= String.valueOf(totalData);

        // 프롬프트 생성 후, 꼬리질문 생성
        String promptTemplate = createPromptTemplateUtil.exec("prompts/tail_question_prompt.txt");
        String prompt = String.format(promptTemplate, previousQuestion, previousAnswer);
        String question = createPromptUtil.exec(prompt);

        // 꼬리질문 개수, 질문 번호
       String tailQuestionNumber = getTailNumberDAO.exec(sessionKey, parentQuestionNumber);
       String questionNumber = parentQuestionNumber + "-" + tailQuestionNumber;

        // redis에 저장
        saveTailQuestionDAO.exec(fieldKey, question, parentQuestionNumber,tailQuestionNumber,questionNumber, totalQuestionNumber);


        return new SessionInfoResponse.TailQuestion(fieldId, question, parentQuestionNumber, tailQuestionNumber, totalQuestionNumber);
    }
}
