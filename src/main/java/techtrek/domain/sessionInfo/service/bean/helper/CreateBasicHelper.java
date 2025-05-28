package techtrek.domain.sessionInfo.service.bean.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionListDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetCSCategoryDAOBean;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CreateBasicHelper {
    private final GetCSCategoryDAOBean getCSCategoryDAOBean;
    private final GetBasicQuestionListDAOBean getBasicQuestionListDAOBean;
    private final GetBasicQuestionDAOBean getBasicQuestionDAOBean;

    // 기본 질문 생성
    public String exec(EnterpriseName enterpriseName){

        // (ENUM) 해당 기업의 키워드 목록 불러오기
        List<String> keywords = enterpriseName.getKeywords();
        if (keywords.isEmpty()) throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);

        // 랜덤 키워드 불러오기
        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));

        // (ENUM) 키워드를 이용해 cs 불러오기
        CSCategory cs = getCSCategoryDAOBean.exec(selectedKeyword);

        // cs로 질문 리스트 불러오기
        List<BasicQuestion> questions = getBasicQuestionListDAOBean.exec(cs);

        // 랜덤 질문 불러오기
        String question = getBasicQuestionDAOBean.exec(questions);

        return question;
    }
}
