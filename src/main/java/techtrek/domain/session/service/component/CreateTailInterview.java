package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.securty.service.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateTailInterview {

    private final QuestionAnswerRepository questionAnswerRepository; // JPA Repository
    private final UserHelper userHelper;

    public SessionResponse.TailQuestion exec(
            Long analysisId,
            CustomUserDetails userDetails
    ) {
        // 사용자 검증
        User user = userHelper.validateUser(userDetails.getId());

        // 현재 세션에서 가장 큰 mainNumber, subNumber 조회
        Optional<QuestionAnswer> lastQaOpt = questionAnswerRepository.findTopByAnalysisIdAndAnalysisUserIdOrderByMainNumberDescSubNumberDesc(
                analysisId,
                user.getId()
        );

        int mainNumber;
        int subNumber;
        if (lastQaOpt.isPresent()) {
            mainNumber = lastQaOpt.get().getMainNumber();
            subNumber = lastQaOpt.get().getSubNumber();
        } else {
            mainNumber = 1;
            subNumber = 0;
        }

        int nextSubNumber = subNumber + 1;

        // 새로운 QA 생성
        QuestionAnswer newQa = QuestionAnswer.builder()
                .mainNumber(mainNumber)
                .subNumber(nextSubNumber)
                .question("하하")
                .correctAnswer("하하")
                .createdAt(LocalDateTime.now())
                .type("tail")
                .analysis(lastQaOpt.map(QuestionAnswer::getAnalysis).orElse(null))
                .build();

        questionAnswerRepository.save(newQa);

        return SessionResponse.TailQuestion.builder()
                .question("하하")
                .parentQuestionNumber(String.valueOf(mainNumber))
                .tailQuestionNumber(String.valueOf(nextSubNumber))
                .build();
    }
}
