package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.helper.CreateBasicHelper;
import techtrek.domain.sessionInfo.service.bean.helper.CreateJsonHelper;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final GetUserDAOBean getUserDAOBean;
    private final CreateRedisNewDTOBean createNewQuestionMapDAOBean;
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final CreateJsonHelper createJsonDAOBean;
    private final GetPreviousQuestionDAOBean getPreviousQuestionDAOBean;
    private final CreateBasicHelper createNewQuestionDAOBean;
    private final CreateResumeQuestionDAOBean createResumeQuestionDAOBean;
    private final RedisTemplate<String, String> redisTemplate;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId){
        // 시용자 불러오기
        User user = getUserDAOBean.exec("1");

        // 필드Id, 세션키 생성
        String sessionKey = "interview:session:" + sessionId;
        String fieldId = UUID.randomUUID().toString();

        // Redis에서 최근 질문 가져오기
        List<String> jsonStrings = redisTemplate.opsForList().range(sessionKey + ":new", -1, -1);
        String previousJsonString = jsonStrings.isEmpty() ? null : jsonStrings.get(0);

        // 이전 질문의 phase, count값 가져오기
        GetPreviousQuestionDAOBean.PhaseCountDTO PhaseCountDTO = getPreviousQuestionDAOBean.exec(previousJsonString);
        String question = "";
        String phase = PhaseCountDTO.getPhase();
        int count = PhaseCountDTO.getCount();

        // basic일 경우 기본질문, 아닐경우 이력서 기반 질문
        if (phase.equals("basic")) {
            // 세션 정보 불러오기
            SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);

            // 세션 정보에서 enterpriseName 불러오기
            EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

            question = createNewQuestionDAOBean.exec(enterpriseName); }
        else question = createResumeQuestionDAOBean.exec(user, sessionId);

        // 새로운 질문 번호 계산 (기본질문 + 이력서 질문)
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 전체 질문 개수 계산 (새로운 질문 + 꼬리질문)
        Long currentTotalCount = redisTemplate.opsForList().size(sessionKey + ":new") + redisTemplate.opsForList().size(sessionKey + ":tail") ;
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 새로운 질문 Map 생성
        String resultCount = String.valueOf(count + 1);
        RedisRequest.NewQuestion newData = createNewQuestionMapDAOBean.exec(fieldId, question, questionNumber, resultCount, phase, totalQuestionCount);

        // JSON 문자열로 변환
        String jsonString = createJsonDAOBean.exec(newData);

        // Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);

        // 새로운 질문 반환
        return new SessionInfoResponse.NewQuestion(fieldId, question, questionNumber);

    }
}
