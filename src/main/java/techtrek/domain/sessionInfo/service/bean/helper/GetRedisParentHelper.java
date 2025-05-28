package techtrek.domain.sessionInfo.service.bean.helper;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRedisParentHelper {

    private final RedisTemplate<String, String> redisTemplate;

    // 부모 질문, 답변, 번호 불러오기
    public RedisResponse.ParentQuestion exec(String sessionKey, String parentId){
        List<String> sessionData = redisTemplate.opsForList().range(sessionKey, 0, -1);
        if (sessionData == null || sessionData.isEmpty()) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        // 3. parentFieldId에 해당하는 질문 객체 찾기
        JSONObject parentQuestionObj = null;
        for (String data : sessionData) {
            JSONObject obj = new JSONObject(data);
            System.out.println("Checking parentQuestionObj: " + parentId);
            System.out.println("Checking fieldId: " + obj.optString("fieldId")); // 확인 로그
            if (parentId.equals(obj.getString("fieldId"))) {
                parentQuestionObj = obj;
                break;
            }
        }
        if (parentQuestionObj == null) {
            throw new CustomException(ErrorCode.PREVIOUS_QUESTION_NOT_FOUND);
        }

        // 4. 부모 질문의 questionNumber 가져오기
        String parentQuestionNumber = parentQuestionObj.optString("questionNumber");
        String parentQuestion = parentQuestionObj.optString("question");
        String parentAnswer = parentQuestionObj.optString("answer");

        return new RedisResponse.ParentQuestion(parentQuestionNumber,parentQuestion, parentAnswer);

    }

}
