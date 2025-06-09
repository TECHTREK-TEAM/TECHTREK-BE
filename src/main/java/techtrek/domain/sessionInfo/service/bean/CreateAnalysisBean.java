package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.AnalysisParserResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.small.GetSessionInfoDAOBean;
import techtrek.domain.sessionInfo.service.bean.small.SaveAnalysisDAOBean;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.gpt.service.bean.manager.CreateJsonReadManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptTemplateManager;
import techtrek.global.redis.dto.RedisResponse;
import techtrek.global.redis.service.bean.small.GetRedisDataByKeysDAOBean;
import techtrek.global.redis.service.bean.manager.GetHashDataManager;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateAnalysisBean {

    private final GetHashDataManager getHashDataManager;
    private final CreatePromptTemplateManager createPromptTemplateManager;
    private final CreatePromptManager createPromptManager;
    private final CreateJsonReadManager createJsonReadManager;

    private final GetUserDAOBean getUserDAOBean;
    private final GetRedisDataByKeysDAOBean getRedisDataByKeysDAOBean;
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final SaveAnalysisDAOBean saveAnalysisDAOBean;

    // 분석하기
    public SessionInfoResponse.Analysis exec(String sessionId, int duration){
        // 사용자 조회
        User user = getUserDAOBean.exec("1");
        String seniority = user.getSeniority();
        String userGroup = user.getUserGroup();

        // 세션정보 조회
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);
        EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();
        String enterpriseDescription = enterpriseName.getDescription();

        // 키 생성
        String newKey = "interview:session:" + sessionId + ":new:";
        String tailKey = "interview:session:" + sessionId + ":tail:";

        // 모든 키 데이터 조회
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(getHashDataManager.exec(newKey + "*"));
        allKeys.addAll(getHashDataManager.exec(tailKey + "*"));

        // 필요한 필드만 바로 추출해서 저장할 리스트
        List<RedisResponse.ListData> listData = getRedisDataByKeysDAOBean.exec(allKeys);

        // totalQuestionNumber 기준 오름차순 정렬
        listData.sort(Comparator.comparingInt(data -> Integer.parseInt(data.getTotalQuestionNumber())));

        // 질문/답변 문자열 누적
        StringBuilder qaBuilder = new StringBuilder();
        for (RedisResponse.ListData data : listData) {
            qaBuilder.append("질문: ").append(data.getQuestion()).append("\n");
            qaBuilder.append("답변: ").append(data.getAnswer() != null ? data.getAnswer() : "응답 없음").append("\n\n");
        }

        // 프롬프트 생성 후, 분석결과 받기
        String promptTemplate = createPromptTemplateManager.exec("prompts/analysis_prompt.txt");
        String prompt = String.format(promptTemplate, enterpriseName, enterpriseDescription, userGroup, seniority, qaBuilder.toString());
        String gptResponse = createPromptManager.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        AnalysisParserResponse object = createJsonReadManager.exec(gptResponse, AnalysisParserResponse.class);
        System.out.println(gptResponse);

        // 합격 계산
        Boolean status;
        if(object.getTotalScore() >= 70) status = Boolean.TRUE;
        else status = Boolean.FALSE;

        // 분석 테이블에 저장
        String AnalysisId= saveAnalysisDAOBean.exec(sessionInfo, status, object.getTotalScore(), object.getEvaluation().getFollowScore().getScore(), object.getResult(), object.getKeyKeywords().getKeyword(),userGroup, duration);

        return new SessionInfoResponse.Analysis(AnalysisId, status, object.getTotalScore(), object.getEvaluation().getFollowScore().getScore(), object.getResult(), duration, object.getKeyKeywords().getKeyword());
    }

}