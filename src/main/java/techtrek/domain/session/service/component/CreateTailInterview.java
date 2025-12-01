package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateTailInterview {

    private final QuestionAnswerRepository questionAnswerRepository; // JPA Repository
    private final UserHelper userHelper;
    private final Gpt gpt;

    public SessionResponse.TailQuestion exec(
            Long analysisId,
            int mainNumber,
            int previousSubNumber,
            CustomUserDetails userDetails
    ) {
        // 사용자 검증
        System.out.println("dsdada!!!!!!!!!!!!!!!!!!");
        User user = userHelper.validateUser(userDetails.getId());

        // 이전 QA 조회 (user + analysis + mainNumber + subNumber 조건)
        var previousQa = questionAnswerRepository.findPreviousQa(user.getId(), analysisId, mainNumber, previousSubNumber)
                .orElseThrow(() -> new RuntimeException("이전 QA가 없습니다."));

        // 다음 질문 번호 계산
        int nextSubNumber = previousSubNumber + 1;

        System.out.println("dsdada"+ previousQa.getQuestion());
        // GPT 연계 질문 생성
//        SessionParserResponse.ChatResult result = gpt.exec(
//                "prompts/tail_question_prompt.txt",
//                new Object[]{previousQa.getQuestion(), previousQa.getAnswer()},
//                SessionParserResponse.ChatResult.class
//        );

        // questionAnswer 테이블에 새 QA 저장
        QuestionAnswer newQa = QuestionAnswer.builder()
                .mainNumber(mainNumber)
                .subNumber(nextSubNumber)
                .question("하하")
                .correctAnswer("하하")
                .createdAt(LocalDateTime.now())
                .type("tail")
                .analysis(previousQa.getAnalysis()) // 기존 분석과 연결
                .build();

        questionAnswerRepository.save(newQa);

        return SessionResponse.TailQuestion.builder()
                .question("하하")
                .parentQuestionNumber(String.valueOf(previousQa.getMainNumber()))
                .tailQuestionNumber(String.valueOf(nextSubNumber))
                .build();
    }
}