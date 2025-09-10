package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.Interview.service.common.NumberCountProvider;
import techtrek.domain.analysis.dto.AnalysisResponse;
//import techtrek.domain.analysis.service.small.CreateAnalysisDTO;
//import techtrek.domain.Interview.dto.SessionParserResponse;
//import techtrek.domain.Interview.entity.SessionInfo;
//import techtrek.domain.Interview.service.small.GetSessionInfoDAO;
//import techtrek.domain.analysis.service.small.SaveAnalysisDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.component.Chat;
import techtrek.global.openAI.chat.service.common.Prompt;
import techtrek.global.redis.service.small.GetRedisByKeyDAO;
import techtrek.global.redis.service.common.GetRedisHashUtil;
import techtrek.global.openAI.chat.service.common.JsonRead;

import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CreateAnalysis {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final NumberCountProvider numberCountProvider;

    private final GetRedisHashUtil getRedisHashUtil;
    private final Prompt createPromptTemplateUtil;
    private final Chat createPromptUtil;
    private final JsonRead changeJsonReadUtil;

    private final GetUserDAO getUserDAO;
    private final GetRedisByKeyDAO getRedisByKeyDAO;
//    private final GetSessionInfoDAO getSessionInfoDAO;
//    private final SaveAnalysisDAO saveAnalysisDAO;
//    private final CreateAnalysisDTO createAnalysisDTO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    // 분석하기
    public AnalysisResponse.Analysis exec(String sessionId, int duration){
        // key 생성
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix;
        String resumeKey = sessionKey + resumePrefix;
        String tailKey = sessionKey + tailPrefix;

        // TODO: 사용자 조회
        User user = userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 세션 유효성 확인
        Boolean hasSession = redisTemplate.hasKey(sessionKey);
        if (hasSession == null || !hasSession) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // 결과 계산 (유사도 0.6이상 개수 * 100 / 전체개수)
        ParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);
        long highCount = Stream.of(basicKey+"*", resumeKey+"*", tailKey+"*")
                .mapToLong(this::countHighSimilarity)
                .sum();

        double score = numberCount.getTotalCount() > 0 ? (highCount * 100.0 / numberCount.getTotalCount()) : 0.0;
        boolean isPass = score >= 70.0;

        // 유사도 제일 낮은 필드의 질문과 답변과 유사도와 questionNumber추출
        // gpt 돌려서 피드백 ( 단, 0.6이상이면 잘했다는 칭찬)
        // return

//        User user = getUserDAO.exec("1");
//        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);
//
//        // 키 생성
//        String newKey = interviewPrefix + sessionId + ":new:";
//        String tailKey = interviewPrefix + sessionId + ":tail:";
//
//        // 모든 hash 데이터 조회
//        Set<String> allKeys = new HashSet<>();
//        allKeys.addAll(getRedisHashUtil.exec(newKey + "*"));
//        allKeys.addAll(getRedisHashUtil.exec(tailKey + "*"));
//
//        // 모든 hash의 질문, 답변 모두 추출 (오름차순)
//        List<SessionParserResponse.ListData> listData = getRedisByKeyDAO.exec(allKeys);
//
//        // 질문/답변 문자열 누적
//        StringBuilder qaBuilder = new StringBuilder();
//        for (SessionParserResponse.ListData data : listData) {
//            qaBuilder.append("질문: ").append(data.getQuestion()).append("\n");
//            qaBuilder.append("답변: ").append(data.getAnswer() != null ? data.getAnswer() : "응답 없음").append("\n");
//            qaBuilder.append("번호: ").append(data.getQuestionNumber()).append("\n\n");
//        }
//
//        // 프롬프트 생성 후, 분석결과 받기
//        String promptTemplate = createPromptTemplateUtil.exec("prompts/analysis_prompt.txt");
//        //String prompt = String.format(promptTemplate, sessionInfo.getEnterpriseName(), sessionInfo.getEnterpriseName().getDescription(), user.getUserGroup(), user.getSeniority(), qaBuilder.toString());
//        String prompt = String.format(promptTemplate, sessionInfo.getEnterpriseName(), "기업 설명", user.getUserGroup(), user.getSeniority(), qaBuilder.toString());
//        String gptResponse = createPromptUtil.exec(prompt);
//
//        // JSON 파싱 (JSON -> 객체)
//        AnalysisParserResponse object = changeJsonReadUtil.exec(gptResponse, AnalysisParserResponse.class);
//
//        // 분석 테이블에 저장
//        Analysis analysis= saveAnalysisDAO.exec(sessionInfo,object,user,duration);

        // 반환
        return AnalysisResponse.Analysis.builder()
                .analysisId("1")
                .isPass(isPass)
                .score(score)
                .result("dd")
                .duration(duration)
                .keyword("3")
                .build();
    }


    // similarity >= 0.6인 필드 개수 계산
    public long countHighSimilarity(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) return 0;

        long count = 0;

        for (String key : keys) {
            Object value = redisTemplate.opsForHash().get(key, "similarity");
            if (value != null) {
                try {
                    double sim = Double.parseDouble(value.toString());
                    if (sim >= 0.6) count++;
                } catch (NumberFormatException e) {
                    // 무시
                }
            }
        }

        return count;
    }

}