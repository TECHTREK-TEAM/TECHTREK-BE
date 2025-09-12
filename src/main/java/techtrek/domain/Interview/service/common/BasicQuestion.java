package techtrek.domain.Interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;

import java.util.Random;

// 기본 질문 생성
@Component
@RequiredArgsConstructor
public class BasicQuestion {
    private static final String PROMPT_PATH_BASIC = "prompts/basic_question_prompt.txt";

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final CompanyCSProvider companyCSProvider;
    private final Gpt gpt;

    public InterviewParserResponse.ChatResult exec(Enterprise enterprise){
        // true GPT, false DB
        Random random = new Random();
        boolean useGpt = random.nextBoolean();

        if (!useGpt) {
            // 기본 질문 중 랜덤 1개 가져오기
            InterviewQuestion interviewQuestion = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId()).orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));

            return new InterviewParserResponse.ChatResult(interviewQuestion.getQuestion(), interviewQuestion.getCorrectAnswer());
        } else {
            // 기업별 중요 cs
            String focusCS = companyCSProvider.exec(enterprise.getName());

            // gpt 질문 생성
            InterviewParserResponse.ChatResult result = gpt.exec(
                    PROMPT_PATH_BASIC,
                    new Object[]{enterprise.getName(), focusCS},
                    InterviewParserResponse.ChatResult.class
            );

            return new InterviewParserResponse.ChatResult(result.getQuestion(), result.getCorrectAnswer());
        }
    }


}
