package techtrek.domain.sessionInfo.service.bean.small;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetPreviousQuestionDAOBean {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 이전 phase와 count값 불러오기
    public PhaseCountDTO exec(String previousJsonString) {
        String phase;
        int count = 0;

        // 이전 질문이 없을경우 예외처리
        if (previousJsonString != null) throw new CustomException(ErrorCode.PREVIOUS_QUESTION_NOT_FOUND);

        // 이전 질문의 phase, count 불러오기
        try {
            Map<String, String> lastQaData = objectMapper.readValue(previousJsonString, Map.class);
            phase = lastQaData.get("phase");
            count = Integer.parseInt(lastQaData.get("count"));

            // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
            if (count >= 5) {
                phase = phase.equals("basic") ? "resume" : "basic";
                count = 0;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }

        return new PhaseCountDTO(phase, count);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public class PhaseCountDTO {
        private String phase;
        private int count;
    }
}
