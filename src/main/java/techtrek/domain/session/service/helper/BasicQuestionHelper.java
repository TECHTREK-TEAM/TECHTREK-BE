package techtrek.domain.session.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;

import java.util.Random;

// 기본 질문 생성
@Component
@RequiredArgsConstructor
public class BasicQuestionHelper {
    private static final String PROMPT_PATH_BASIC = "prompts/basic_question_prompt.txt";

    private final BasicQuestionRepository interviewQuestionRepository;
    private final CompanyCSHelper companyCSProvider;
    private final Gpt gpt;

    public SessionParserResponse.ChatResult exec(Enterprise enterprise){
        // true GPT, false DB
        Random random = new Random();
        boolean useGpt = random.nextBoolean();

        if (!useGpt) {
            // 기본 질문 중 랜덤 1개 가져오기
            techtrek.domain.basicQuestion.entity.BasicQuestion interviewQuestion = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId() ).orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));

            return new SessionParserResponse.ChatResult(interviewQuestion.getQuestion(), interviewQuestion.getCorrectAnswer());
        } else {
            // 기업별 중요 cs
            String focusCS = companyCSProvider.exec(enterprise.getName());

            // gpt 질문 생성
            SessionParserResponse.ChatResult result = gpt.exec(
                    PROMPT_PATH_BASIC,
                    new Object[]{enterprise.getName(), focusCS},
                    SessionParserResponse.ChatResult.class
            );

            return new SessionParserResponse.ChatResult(result.getQuestion(), result.getCorrectAnswer());
        }
    }


}
