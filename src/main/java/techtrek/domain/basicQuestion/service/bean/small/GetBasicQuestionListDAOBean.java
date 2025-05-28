package techtrek.domain.basicQuestion.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetBasicQuestionListDAOBean {

    private final BasicQuestionRepository basicQuestionRepository;

    // cs 키워드로 질문 리스트 불러오기
    public List<BasicQuestion> exec(CSCategory cs){
        List<BasicQuestion> questions = basicQuestionRepository.findByCSCategory(cs);
        if (questions == null || questions.isEmpty()) {
            throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);
        }

        return questions;

    }
}
