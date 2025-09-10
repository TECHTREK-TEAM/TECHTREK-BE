package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.component.Chat;
import techtrek.global.openAI.chat.service.common.Prompt;
import techtrek.global.openAI.chat.service.common.JsonRead;

import java.util.Random;

// 기본 질문 생성
@Component
@RequiredArgsConstructor
public class BasicQuestion {
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final CompanyCSProvider companyCSProvider;
    private final Prompt prompt;
    private final Chat chatService;
    private final JsonRead jsonRead;

    public ParserResponse.BasicQuestionResult exec(Enterprise enterprise){
        // true GPT, false DB
        Random random = new Random();
        boolean useGpt = random.nextBoolean();

        if (!useGpt) {
            // 기본 질문 중 랜덤 1개 가져오기
            InterviewQuestion interviewQuestion = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));

            return new ParserResponse.BasicQuestionResult(interviewQuestion.getQuestion(), interviewQuestion.getCorrectAnswer());
        } else {
            // 프롬프트, GPT gpt로 질문 생성
            String focusCS = companyCSProvider.exec(enterprise.getName());

            String template = prompt.exec("prompts/basic_question_prompt.txt");
            String format = String.format(template, enterprise.getName(), focusCS);
            String chatResponse = chatService.exec(format);

            // JSON → DTO
            ParserResponse.BasicQuestion questionResponse = jsonRead.exec(chatResponse, ParserResponse.BasicQuestion.class);
            return new ParserResponse.BasicQuestionResult(questionResponse.getQuestion(), questionResponse.getCorrectAnswer());
        }
    }


}
