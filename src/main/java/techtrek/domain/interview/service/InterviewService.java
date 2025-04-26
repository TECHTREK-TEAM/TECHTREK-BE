package techtrek.domain.interview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import techtrek.domain.interview.dto.InterviewResponse;
import techtrek.domain.interview.entity.BasicQuestion;
import techtrek.domain.interview.repository.BasicQuestionRepository;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InterviewService {

    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;

    //면접 시작하기
    public InterviewResponse.Start startInterview() {
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();

        // 데이터베이스에서 랜덤으로 하나의 질문을 가져옴
        BasicQuestion getQuestion = basicQuestionRepository.findRandomQuestion()
                .orElseThrow(() -> new GlobalException(ResponseCode.BASIC_QUESTION_NOT_FOUND));

        // 질문 내용
        String basicQuestion = getQuestion.getQuestion();

        String redisKey = "interview:session:" + sessionId;
        redisTemplate.opsForHash().put(redisKey, fieldId + ":question", basicQuestion);
        redisTemplate.opsForHash().put(redisKey, fieldId + ":answer", "");


        return new InterviewResponse.Start(sessionId,fieldId,basicQuestion);
    }

}
