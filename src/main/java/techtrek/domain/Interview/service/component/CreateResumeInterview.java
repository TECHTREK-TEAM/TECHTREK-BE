package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.SessionResponse;

// 이력서 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateResumeInterview {

    public SessionResponse.Question exec(String sessionId){




        return SessionResponse.Question.builder()
                .fieldId("1")
                .question("1")
                .questionNumber("1")
                .build();
    }

}
