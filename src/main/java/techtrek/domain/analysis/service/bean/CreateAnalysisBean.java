package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.service.small.CreateAnalysisDTO;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;
import techtrek.domain.analysis.service.small.SaveAnalysisDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.global.util.CreatePromptUtil;
import techtrek.global.util.CreatePromptTemplateUtil;
import techtrek.domain.redis.service.small.GetRedisByKeyDAO;
import techtrek.domain.redis.service.common.GetRedisHashUtil;
import techtrek.global.util.ChangeJsonReadUtil;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateAnalysisBean {

    private final GetRedisHashUtil getRedisHashUtil;
    private final CreatePromptTemplateUtil createPromptTemplateUtil;
    private final CreatePromptUtil createPromptUtil;
    private final ChangeJsonReadUtil changeJsonReadUtil;

    private final GetUserDAO getUserDAO;
    private final GetRedisByKeyDAO getRedisByKeyDAO;
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final SaveAnalysisDAO saveAnalysisDAO;
    private final CreateAnalysisDTO createAnalysisDTO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 분석하기
    public AnalysisResponse.Analysis exec(String sessionId, int duration){
        // TODO: 사용자, 세션정보 조회
        User user = getUserDAO.exec("1");
        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);

        // 키 생성
        String newKey = interviewPrefix + sessionId + ":new:";
        String tailKey = interviewPrefix + sessionId + ":tail:";

        // 모든 hash 데이터 조회
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(getRedisHashUtil.exec(newKey + "*"));
        allKeys.addAll(getRedisHashUtil.exec(tailKey + "*"));

        // 모든 hash의 질문, 답변 모두 추출 (오름차순)
        List<SessionParserResponse.ListData> listData = getRedisByKeyDAO.exec(allKeys);

        // 질문/답변 문자열 누적
        StringBuilder qaBuilder = new StringBuilder();
        for (SessionParserResponse.ListData data : listData) {
            qaBuilder.append("질문: ").append(data.getQuestion()).append("\n");
            qaBuilder.append("답변: ").append(data.getAnswer() != null ? data.getAnswer() : "응답 없음").append("\n");
            qaBuilder.append("번호: ").append(data.getQuestionNumber()).append("\n\n");
        }

        // 프롬프트 생성 후, 분석결과 받기
        String promptTemplate = createPromptTemplateUtil.exec("prompts/analysis_prompt.txt");
        String prompt = String.format(promptTemplate, sessionInfo.getEnterpriseName(), sessionInfo.getEnterpriseName().getDescription(), user.getUserGroup(), user.getSeniority(), qaBuilder.toString());
        String gptResponse = createPromptUtil.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        AnalysisParserResponse object = changeJsonReadUtil.exec(gptResponse, AnalysisParserResponse.class);

        // 분석 테이블에 저장
        Analysis analysis= saveAnalysisDAO.exec(sessionInfo,object,user,duration);

        // 반환
        return createAnalysisDTO.exec(analysis);
    }

}