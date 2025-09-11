package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.global.openAI.chat.service.common.JsonRead;
import techtrek.global.openAI.chat.service.component.Chat;
import techtrek.global.openAI.chat.service.common.Prompt;

// 연계 질문 생성 (부모/이전 질문, 답변)
@Component
@RequiredArgsConstructor
public class TailQuestion {
    private final Prompt prompt;
    private final Chat chatService;
    private final JsonRead jsonRead;

    public InterviewParserResponse.ChatResult exec(String question, String answer){
        // 프롬프트 생성, GPT gpt로 질문 생성
        String template = prompt.exec("prompts/tail_question_prompt.txt");
        String format = String.format(template, question, answer);
        String chatResponse = chatService.exec(format);

        // JSON → DTO
        InterviewParserResponse.ChatResult questionResponse = jsonRead.exec(chatResponse, InterviewParserResponse.ChatResult.class);

        return new InterviewParserResponse.ChatResult(questionResponse.getQuestion(), questionResponse.getCorrectAnswer());
    }

}


