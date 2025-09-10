package techtrek.domain.Interview.service.component;

import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewResponse;

// 첫번째 연계질문
@Component
public class CreateParentTailInterview {

    public InterviewResponse.TailQuestion exec(String sessionId, String parentId) {

        return new InterviewResponse.TailQuestion("1", "1","1","1");
    }
}
