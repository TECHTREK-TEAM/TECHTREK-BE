package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
@RequiredArgsConstructor
public class CreateSessionInfoDTOBean {

    // 면접 시작 Response DTO
    public SessionInfoResponse.Start exec(String sessionId, RedisRequest.NewQuestion dto) {

        SessionInfoResponse.Start response = new SessionInfoResponse.Start();
        response.setSessionId(sessionId);
        response.setFieldId(dto.getFieldId());
        response.setQuestion(dto.getQuestion());
        response.setQuestionNumber(dto.getQuestionNumber());
        response.setTotalQuestionNumber(dto.getTotalQuestionCount());

        return response;

    }


    // 새로운 질문 Response DTO
    public SessionInfoResponse.NewQuestion exec(RedisRequest.NewQuestion dto) {

        SessionInfoResponse.NewQuestion response = new SessionInfoResponse.NewQuestion();
        response.setFieldId(dto.getFieldId());
        response.setQuestion(dto.getQuestion());
        response.setQuestionNumber(dto.getQuestionNumber());
        response.setTotalQuestionNumber(dto.getTotalQuestionCount());

        return response;

    }
}
