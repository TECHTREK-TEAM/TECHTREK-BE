package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateNewDataDAOBean {

    // 새로운 질문 Map 생성
    public  Map<String, String> exec(String fieldId, String basicQuestion, String questionNumber, String count, String phase, String totalQuestionCount){
        Map<String, String> newData = new HashMap<>();
        newData.put("fieldId", fieldId);
        newData.put("question", basicQuestion);
        newData.put("answer", "");
        newData.put("questionNumber", questionNumber);
        newData.put("count", count);
        newData.put("phase", phase);
        newData.put("totalQuestionCount", totalQuestionCount);

        return newData;
    }
}
