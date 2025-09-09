package techtrek.domain.session.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.BasicQuestionResponse;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.prompt.Prompt;
import techtrek.global.gpt.prompt.PromptTemplate;
import techtrek.global.gpt.prompt.JsonRead;

import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class BasicQuestion {
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final PromptTemplate promptTemplate;
    private final Prompt prompt;
    private final JsonRead jsonRead;

    // 기본 질문 생성
    public BasicQuestionResponse.BasicQuestionResult exec(Enterprise enterprise){
        // true GPT, false DB
        Random random = new Random();
        boolean useGpt = random.nextBoolean();

        if (!useGpt) {
            // 기본 질문 중 랜덤 1개 가져오기
            InterviewQuestion interviewQuestion = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));

            return new BasicQuestionResponse.BasicQuestionResult(interviewQuestion.getQuestion(), interviewQuestion.getCorrectAnswer());
        } else {
            // 프롬프트 생성, GPT로 질문 생성
            String focusCS = COMPANY_CS.get(enterprise.getName());

            String template = promptTemplate.exec("prompts/basic_question_prompt.txt");
            String format = String.format(template, enterprise.getName(), focusCS);
            String gptResponse = prompt.exec(format);

            // JSON → DTO
            BasicQuestionResponse.BasicQuestion questionResponse = jsonRead.exec(gptResponse, BasicQuestionResponse.BasicQuestion.class);
            return new BasicQuestionResponse.BasicQuestionResult(questionResponse.getQuestion(), questionResponse.getCorrectAnswer());
        }
    }

    // 회사별 CS 영역 Map
    private static final Map<String, String> COMPANY_CS = Map.of(
            "SAMSUNG", "운영체제, 네트워크, 자료구조, 알고리즘"
    );


}
