package techtrek.domain.basicQuestion.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class GetBasicQuestionDAOBean {

    // 랜덤 질문 반환
    public String exec(List<BasicQuestion> questions) {
        if (questions.isEmpty()) {
            throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);
        }
        return questions.get(new Random().nextInt(questions.size())).getQuestion();
    }
}
