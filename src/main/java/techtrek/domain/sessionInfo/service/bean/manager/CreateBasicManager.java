package techtrek.domain.sessionInfo.service.bean.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.status.CsCategory;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetCSCategoryDAOBean;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CreateBasicManager {
    private final GetCSCategoryDAOBean getCSCategoryDAOBean;
    private final GetBasicQuestionDAOBean getBasicQuestionDAOBean;

    // 기본 질문 생성
    public String exec(EnterpriseName enterpriseName){

        // (ENUM) 해당 기업의 키워드 목록 불러오기
        List<String> keywords = enterpriseName.getKeywords();
        if (keywords.isEmpty()) throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);

        // 랜덤 키워드 불러오기
        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));

        // (ENUM) 키워드를 이용해 cs 불러오기
        CsCategory cs = getCSCategoryDAOBean.exec(selectedKeyword);

        // cs로 랜덤질문 불러오기
        String question = getBasicQuestionDAOBean.exec(cs);

        return question;
    }
}
