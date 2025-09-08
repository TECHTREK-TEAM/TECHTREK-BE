package techtrek.domain.interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
//import techtrek.domain.basicQuestion.service.small.GetBasicQuestionDAO;
import techtrek.domain.interview.dto.BasicQuestionResponse;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.gpt.prompt.Prompt;
import techtrek.global.gpt.prompt.PromptTemplate;
import techtrek.global.gpt.prompt.JsonRead;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BasicQuestion {
    //private final GetBasicQuestionDAO getBasicQuestionDAO;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final PromptTemplate promptTemplate;
    private final Prompt prompt;
    private final JsonRead jsonRead;

    // 회사별 CS 영역 Map
    private static final Map<String, String> COMPANY_CS = Map.of(
            "SAMSUNG", "운영체제, 네트워크, 자료구조, 알고리즘"
    );

    // 기본 질문 생성
    public String exec(Enterprise enterprise){

        // 기본 질문 중 랜덤 1개 가져오기
//        InterviewQuestion interviewQuestion = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId())
//                .orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));
//        String question = interviewQuestion.getQuestion();


        // 프롬프트 생성, gpt로 질문 생성
        String focusCS = COMPANY_CS.get(enterprise.getName());

        String template = promptTemplate.exec("prompts/basic_question_prompt.txt");
        String format = String.format(template, enterprise.getName(), focusCS);
        String gptResponse = prompt.exec(format);

        // JSON → DTO
        BasicQuestionResponse questionResponse = jsonRead.exec(gptResponse, BasicQuestionResponse.class);
        String question = questionResponse.getQuestion();
        String answer = questionResponse.getAnswer();
        System.out.println(answer);

        // (ENUM) 해당 기업의 키워드 목록 불러오기
//        List<String> keywords = enterpriseName.getKeywords();
//        if (keywords.isEmpty()) throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);
//
//        // 랜덤 키워드 불러오기
//        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));
//
//        // (ENUM) 키워드를 이용해 cs 불러오기
//        Category cs = getCSCategoryDAO.exec(selectedKeyword);
//
//        // cs로 랜덤질문 불러오기
//        String question = getBasicQuestionDAO.exec(cs);

        //return question;
        return question;
    }

}
