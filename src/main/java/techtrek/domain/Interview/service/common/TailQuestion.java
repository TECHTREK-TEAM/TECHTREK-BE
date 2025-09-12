package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.global.openAI.chat.service.common.Gpt;

// 연계 질문 생성 (부모/이전 질문, 답변)
@Component
@RequiredArgsConstructor
public class TailQuestion {
    private static final String PROMPT_PATH_TAIL = "prompts/tail_question_prompt.txt";

    private final Gpt createGpt;

    public InterviewParserResponse.ChatResult exec(String question, String answer){
        // gpt 질문 생성
        InterviewParserResponse.ChatResult result = createGpt.exec(
                PROMPT_PATH_TAIL,
                new Object[]{question, answer},
                InterviewParserResponse.ChatResult.class
        );

        return new InterviewParserResponse.ChatResult(result.getQuestion(), result.getCorrectAnswer());
    }

}


