package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionListDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetCSCategoryDAOBean;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateBasicQuestionDAOBean {
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final GetCSCategoryDAOBean getCSCategoryDAOBean;
    private final GetBasicQuestionListDAOBean getBasicQuestionListDAOBean;
    private final GetBasicQuestionDAOBean getBasicQuestionDAOBean;

    // 기본 질문 생성
    public String exec(String sessionId){
        // 세션 정보 불러오기
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);

        // 세션 정보에서 enterpriseName 불러오기
        EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

        // (ENUM) 해당 기업의 키워드 목록 불러오기
        List<String> keywords = enterpriseName.getKeywords();

        // (ENUM) 키워드를 이용해 cs 불러오기
        CSCategory cs = getCSCategoryDAOBean.exec(keywords);

        // cs로 질문 리스트 불러오기
        List<BasicQuestion> questions = getBasicQuestionListDAOBean.exec(cs);

        // 랜덤 질문 불러오기
        String question = getBasicQuestionDAOBean.exec(questions);

        return question;
    }
}
