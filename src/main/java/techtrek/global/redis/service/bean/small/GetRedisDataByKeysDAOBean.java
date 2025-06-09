package techtrek.global.redis.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.global.redis.dto.RedisResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GetRedisDataByKeysDAOBean {
    private final RedisTemplate<String, String> redisTemplate;

    // key들에 의해 데이터 조회 (list)
    public List<RedisResponse.ListData> exec(Set<String> keys){
        // 필요한 필드만 추출해서 저장할 리스트
        List<RedisResponse.ListData> response = new ArrayList<>();

        // 추출
        for (String key : keys) {
            Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

            String question = String.valueOf(data.get("question"));
            String answer = data.containsKey("answer") ? String.valueOf(data.get("answer")) : null;
            String totalQuestionNumber = data.get("totalQuestionNumber").toString();
            String tailQuestionMessage = null;
            if(data.containsKey("tailQuestionNumber")) {
                tailQuestionMessage = "연계 질문입니다.";
            }

            response.add(new RedisResponse.ListData(question, answer, totalQuestionNumber, tailQuestionMessage));
        }

        return response;
    }
}
