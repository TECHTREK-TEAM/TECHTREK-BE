package techtrek.domain.redis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.interview.dto.SessionParserResponse;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GetRedisByKeyDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // key로 데이터 조회
    public List<SessionParserResponse.ListData> exec(Set<String> keys){
        // 저장할 리스트
        List<SessionParserResponse.ListData> response = new ArrayList<>();

        // 추출
        for (String key : keys) {
            Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

            if (data == null || data.isEmpty()) {
                System.out.println("  - Redis hash 데이터가 없거나 비어있음.");
                continue;
            }

            String question = String.valueOf(data.get("question"));
            String answer = data.containsKey("answer") ? String.valueOf(data.get("answer")) : null;
            String questionNumber = String.valueOf(data.get("questionNumber"));
            String totalQuestionNumber = data.get("totalQuestionNumber").toString();

            String tailQuestionMessage = null;
            String tailQuestionNumberVal = String.valueOf(data.getOrDefault("tailQuestionNumber", "0"));
            if ("1".equals(tailQuestionNumberVal) || "true".equalsIgnoreCase(tailQuestionNumberVal)) {
                tailQuestionMessage = "연계 질문입니다.";
            }

            response.add(new SessionParserResponse.ListData(question, answer, questionNumber, totalQuestionNumber, tailQuestionMessage));
        }

        // totalQuestionNumber 기준 오름차순 정렬
        response.sort(Comparator.comparingInt(data -> Integer.parseInt(data.getTotalQuestionNumber())));

        return response;
    }
}
