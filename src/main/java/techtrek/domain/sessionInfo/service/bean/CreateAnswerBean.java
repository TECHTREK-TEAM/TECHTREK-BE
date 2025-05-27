package techtrek.domain.sessionInfo.service.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.gpt.service.OpenAiService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateAnswerBean {
    private final UserRepository userRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;
    private final OpenAiService openAiService;

    public Boolean exec(String sessionId, String fieldId, String type, String answer){
        // 1. 세션 키 결정 (new인지 tail인지에 따라 다름)
        String sessionKey = "interview:session:" + sessionId + ":" + type;

        // 2. 해당 Redis 리스트에서 모든 질문/답변 항목 조회
        List<String> jsonList = redisTemplate.opsForList().range(sessionKey, 0, -1);
        ObjectMapper objectMapper = new ObjectMapper();

        // 3. 리스트 순회하며 해당 fieldId를 가진 JSON 항목 찾기 및 수정
        for (int i = 0; i < jsonList.size(); i++) {
            try {
                Map<String, String> qaData = objectMapper.readValue(jsonList.get(i), Map.class);
                if (fieldId.equals(qaData.get("fieldId"))) {
                    // 3-1. answer 값 업데이트
                    qaData.put("answer", answer);

                    // 3-2. 다시 JSON 문자열로 직렬화
                    String updatedJson = objectMapper.writeValueAsString(qaData);

                    // 3-3. 해당 인덱스에 수정된 JSON 저장
                    redisTemplate.opsForList().set(sessionKey, i, updatedJson);

                    return true;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 처리 중 오류 발생", e);
            }
        }

        // 해당 fieldId를 찾지 못한 경우
        return false;
    };
}
