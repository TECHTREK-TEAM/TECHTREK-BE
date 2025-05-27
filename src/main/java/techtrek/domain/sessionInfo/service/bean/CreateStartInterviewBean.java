package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetBasicQuestionListDAOBean;
import techtrek.domain.basicQuestion.service.bean.small.GetCSCategoryDAOBean;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.service.bean.small.CreateJsonDAOBean;
import techtrek.domain.sessionInfo.service.bean.small.CreateNewDataDAOBean;
import techtrek.domain.sessionInfo.service.bean.small.SaveSessionInfoDAOBean;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    private final GetUserDAOBean getUserDAOBean;
    private final GetCSCategoryDAOBean getCSCategoryDAOBean;
    private final GetBasicQuestionListDAOBean getBasicQuestionListDAOBean;
    private final GetBasicQuestionDAOBean getBasicQuestionDAOBean;
    private final SaveSessionInfoDAOBean saveSessionInfoDAOBean;
    private final CreateNewDataDAOBean createNewDataDAOBean;
    private final CreateJsonDAOBean createJsonDAOBean;
    private final RedisTemplate<String, String> redisTemplate;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(EnterpriseName enterpriseName, EnterpriseType enterpriseType){
        // 시용자 불러오기
        User user = getUserDAOBean.exec("1");

        // 세션, 필드 ID 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;

        // (ENUM) 해당 기업의 키워드 목록 불러오기
        List<String> keywords = enterpriseName.getKeywords();

        // (ENUM) 키워드를 이용해 cs 불러오기
        CSCategory cs = getCSCategoryDAOBean.exec(keywords);

        // cs로 질문 리스트 불러오기
        List<BasicQuestion> questions = getBasicQuestionListDAOBean.exec(cs);

        // 랜덤 질문 불러오기
        String basicQuestion = getBasicQuestionDAOBean.exec(questions);

        // 새로운 질문 번호 계산 (기본질문 + 이력서 질문)
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 전체 질문 개수 계산 (새로운 질문 + 꼬리질문)
        Long currentTotalCount = redisTemplate.opsForHash().size(sessionKey);
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 새로운 질문 Map 생성
        Map<String, String> newData = createNewDataDAOBean.exec(fieldId, basicQuestion, questionNumber, totalQuestionCount);

        // JSON 문자열로 변환
        String jsonString = createJsonDAOBean.exec(newData);

        // Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);

        // 세션정보 테이블에 값 저장
        saveSessionInfoDAOBean.exec(sessionId, enterpriseName, enterpriseType, user);

        return new SessionInfoResponse.Start(sessionId,fieldId,basicQuestion,questionNumber);
    };
}
