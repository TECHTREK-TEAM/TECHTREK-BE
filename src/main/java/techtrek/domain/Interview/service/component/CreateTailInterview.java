//package techtrek.domain.Interview.service.component;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import techtrek.domain.Interview.dto.InterviewResponse;
//import techtrek.domain.Interview.dto.SessionParserResponse;
//import techtrek.domain.Interview.service.small.CreateTailDTO;
//import techtrek.global.common.code.ErrorCode;
//import techtrek.global.common.exception.CustomException;
//import techtrek.global.redis.service.small.*;
//import techtrek.global.openAI.chat.service.component.Chat;
//import techtrek.global.openAI.chat.service.common.Prompt;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class CreateTailInterview {
//    // 상수 정의
//    private static final String PROMPT_PATH_TAIL = "prompts/tail_question_prompt.txt";
//
//    private final RedisTemplate<String, String> redisTemplate;
//    private final Prompt createPromptTemplateUtil;
//    private final Chat createPromptUtil;
//
//    private final GetRedisTotalKeyCountDAO getRedisTotalKeyCountDAO;
//    private final CheckRedisKeyDAO checkRedisKeyDAO;
//    private final SaveTailQuestionDAO saveTailQuestionDAO;
//    private final GetTailNumberDAO getTailNumberDAO;
//    private final GetRedisDAO getRedisDAO;
//    private final CreateTailDTO createTailDTO;
//
//    @Value("${custom.redis.prefix.interview}")
//    private String interviewPrefix;
//
//    @Value("${custom.redis.prefix.basic}")
//    private String basicPrefix;
//
//    @Value("${custom.redis.prefix.resume}")
//    private String resumePrefix;
//
//    @Value("${custom.redis.prefix.tail}")
//    private String tailPrefix;
//
//    // 꼬리질문 생성
//    public InterviewResponse.TailQuestion exec(String sessionId, String parentId, String previousFieldId) {
//        // 키 생성
//        String fieldId = UUID.randomUUID().toString();
//        String sessionKey = interviewPrefix + sessionId;
//        String fieldKey =  sessionKey + tailPrefix + fieldId;
//
//        // parentId 키, previousFieldId 키 존재 확인
//        if (!redisTemplate.hasKey(sessionKey + basicPrefix + parentId) && !redisTemplate.hasKey(sessionKey + resumePrefix + parentId)) throw new CustomException(ErrorCode.PARENT_FIELD_NOT_FOUND);
//        if (!redisTemplate.hasKey(sessionKey + tailPrefix + previousFieldId)) { throw new CustomException(ErrorCode.PREVIOUS_FIELD_NOT_FOUND);}
//
//        // 부모 필드 조회
//        SessionParserResponse.FieldData parentData = getRedisDAO.exec(sessionKey + ":new:"+ parentId);
//        String parentQuestionNumber = parentData.getQuestionNumber();
//
//        // 질문, 답변 조회 (부모 or 이전)
//        SessionParserResponse.FieldData previousData = getPreviousData(sessionKey, parentId, previousFieldId);
//        String previousQuestion = previousData.getQuestion();
//        String previousAnswer = previousData.getAnswer();
//
//        // 총 질문 개수 조회
//        int newCount = getRedisTotalKeyCountDAO.exec(sessionKey + ":new");
//        int tailCount = getRedisTotalKeyCountDAO.exec(sessionKey + ":tail");
//        String totalQuestionNumber= String.valueOf(newCount + tailCount + 1);
//
//        // 프롬프트 생성 후, 꼬리질문 생성
//        String promptTemplate = createPromptTemplateUtil.exec(PROMPT_PATH_TAIL);
//        String prompt = String.format(promptTemplate, previousQuestion, previousAnswer);
//        String question = createPromptUtil.exec(prompt);
//
//        // 꼬리질문 개수, 질문 번호
//       String tailQuestionNumber = getTailNumberDAO.exec(sessionKey, parentQuestionNumber);
//       String questionNumber = parentQuestionNumber + "-" + tailQuestionNumber;
//
//        // redis에 저장
//        saveTailQuestionDAO.exec(fieldKey, question, parentQuestionNumber,tailQuestionNumber,questionNumber, totalQuestionNumber);
//
//        return createTailDTO.exec(fieldId, question, parentQuestionNumber, tailQuestionNumber, totalQuestionNumber);
//    }
//
//    // 이전 질문 데이터 조회
//    private SessionParserResponse.FieldData getPreviousData(String sessionKey, String parentId, String previousFieldId) {
//        if (previousFieldId != null) {
//            return getRedisDAO.exec(sessionKey+":tail:"+previousFieldId);
//        }
//        return getRedisDAO.exec(sessionKey+":new:"+parentId);
//    }
//}
