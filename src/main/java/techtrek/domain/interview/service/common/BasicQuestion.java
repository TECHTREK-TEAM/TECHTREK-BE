package techtrek.domain.interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
//import techtrek.domain.basicQuestion.service.small.GetBasicQuestionDAO;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class BasicQuestion {
    //private final GetBasicQuestionDAO getBasicQuestionDAO;
    private final InterviewQuestionRepository interviewQuestionRepository;

    // 기본 질문 생성
    public String exec(Enterprise enterprise){

        // 기본 질문 중 랜덤 1개 가져오기
        InterviewQuestion question = interviewQuestionRepository.findRandomQuestionByEnterpriseId(enterprise.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND));



        



        // if (question == null) throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);

        // (ENUM) 해당 기업의 키워드 목록 불러오기
//        List<String> keywords = enterpriseName.getKeywords();
//        if (keywords.isEmpty()) throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);
//
//        // 랜덤 키워드 불러오기
//        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));
//
//        // (ENUM) 키워드를 이용해 cs 불러오기
//        Category cs = getCSCategoryDAO.exec(selectedKeyword);
//
//        // cs로 랜덤질문 불러오기
//        String question = getBasicQuestionDAO.exec(cs);

        //return question;
        return question.getQuestion();
    }
}
