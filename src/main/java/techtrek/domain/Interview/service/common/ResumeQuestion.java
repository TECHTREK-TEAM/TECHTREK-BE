package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.openAI.chat.service.common.Gpt;

// 이력서 질문 생성
@Component
@RequiredArgsConstructor
public class ResumeQuestion {
    private static final String PROMPT_PATH_RESUME = "prompts/resume_question_prompt.txt";

    private final CompanyCSProvider companyCSProvider;
    private final Gpt createGpt;

    public InterviewParserResponse.ChatResult exec(String resume, Enterprise enterprise){
        // 프롬프트 생성, GPT gpt로 질문 생성
        String focusCS = companyCSProvider.exec(enterprise.getName());

        // gpt 질문 생성
        InterviewParserResponse.ChatResult result = createGpt.exec(
                PROMPT_PATH_RESUME,
                new Object[]{resume,enterprise.getName(), focusCS},
                InterviewParserResponse.ChatResult.class
        );

        return new InterviewParserResponse.ChatResult(result.getQuestion(), result.getCorrectAnswer());
    }

}


