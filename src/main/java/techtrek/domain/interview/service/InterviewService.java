package techtrek.domain.interview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import techtrek.domain.interview.dto.InterviewResponse;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InterviewService {

    private final RedisTemplate<String, String> redisTemplate;

    //면접 시작하기
    public InterviewResponse.Start startInterview() {
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String basicQuestion = "자기소개 해주세요제발요";


        String redisKey = "interview:session:" + sessionId;
        redisTemplate.opsForHash().put(redisKey, fieldId + ":question", basicQuestion);
        redisTemplate.opsForHash().put(redisKey, fieldId + ":answer", "");


        return new InterviewResponse.Start(sessionId,fieldId,basicQuestion);
    }

}
