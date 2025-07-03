package techtrek.domain.sessionInfo.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.status.CsCategory;
import techtrek.domain.basicQuestion.service.small.GetBasicQuestionDAO;
import techtrek.domain.basicQuestion.service.small.GetCSCategoryDAO;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CreateBasicUtil {
    private final GetCSCategoryDAO getCSCategoryDAO;
    private final GetBasicQuestionDAO getBasicQuestionDAO;

    // 기본 질문 생성
    public String exec(EnterpriseName enterpriseName){

        // (ENUM) 해당 기업의 키워드 목록 불러오기
        List<String> keywords = enterpriseName.getKeywords();
        if (keywords.isEmpty()) throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);

        // 랜덤 키워드 불러오기
        String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));

        // (ENUM) 키워드를 이용해 cs 불러오기
        CsCategory cs = getCSCategoryDAO.exec(selectedKeyword);

        // cs로 랜덤질문 불러오기
        String question = getBasicQuestionDAO.exec(cs);

        return question;
    }
}
