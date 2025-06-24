package techtrek.domain.basicQuestion.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CsCategory;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GetBasicQuestionDAO {

    private final BasicQuestionRepository basicQuestionRepository;

    // cs로 램덤 질문 조회
    public String exec(CsCategory cs){

        // 리스트 조회
        List<BasicQuestion> questions = basicQuestionRepository.findByCsCategory(cs);
        if (questions == null || questions.isEmpty()) throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);

        // 랜덤 질문 조회
        String question = questions.get(new Random().nextInt(questions.size())).getQuestion();

        return question;

    }
}
