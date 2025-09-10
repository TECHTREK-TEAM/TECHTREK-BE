package techtrek.domain.Interview.service.component;

import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewResponse;

@Component
public class CreatePreviousTailInterview {

    public InterviewResponse.TailQuestion exec(String sessionId, String previousId) {

        return new InterviewResponse.TailQuestion("1", "1","1","1");
    }
}
