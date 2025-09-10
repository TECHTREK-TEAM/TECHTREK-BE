package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.gpt.prompt.JsonRead;
import techtrek.global.gpt.prompt.Prompt;
import techtrek.global.gpt.prompt.PromptTemplate;

// 이력서 질문 생성
@Component
@RequiredArgsConstructor
public class ResumeQuestion {
    private final CompanyCSProvider companyCSProvider;
    private final PromptTemplate promptTemplate;
    private final Prompt prompt;
    private final JsonRead jsonRead;

    public ParserResponse.BasicQuestionResult exec(String resume, Enterprise enterprise){
        // 프롬프트 생성, GPT로 질문 생성
        String focusCS = companyCSProvider.exec(enterprise.getName());

        String template = promptTemplate.exec("prompts/resume_question_prompt.txt");
        String format = String.format(template, resume, enterprise.getName(), focusCS);
        String gptResponse = prompt.exec(format);

        // JSON → DTO
        ParserResponse.BasicQuestion questionResponse = jsonRead.exec(gptResponse, ParserResponse.BasicQuestion.class);

        return new ParserResponse.BasicQuestionResult(questionResponse.getQuestion(), questionResponse.getCorrectAnswer());
    }

}


