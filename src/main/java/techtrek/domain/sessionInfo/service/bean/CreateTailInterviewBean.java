package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.bean.helper.CreateTailHelper;
import techtrek.domain.sessionInfo.service.bean.helper.CreatetailCountHelper;
import techtrek.domain.sessionInfo.service.bean.helper.GetRedisParentHelper;
import techtrek.domain.sessionInfo.service.bean.helper.SaveRedisTailHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTailInterviewBean {
    private final GetRedisParentHelper getRedisParentHelper;
    private final CreateTailHelper createTailHelper;
    private final CreatetailCountHelper createtailCountHelper;
    private final SaveRedisTailHelper saveRedisTailHelper;

    // 꼬리질문 생성
    public SessionInfoResponse.NewQuestion exec(String sessionId, String parentId) {
        // 세션 예외처리
        if (sessionId == null || sessionId.isEmpty()) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        if (parentId == null || parentId.isEmpty()) throw new CustomException(ErrorCode.PARENTFIELD_NOT_FOUND);

        // 세션키, 꼬리키 생성
        String sessionKey = "interview:session:" + sessionId + ":new";
        String tailSessionKey = "interview:session:" + sessionId + ":tail";
        String tailCountKey = "interview:session:" + sessionId + ":tailCount:" + parentId;
        String fieldId = UUID.randomUUID().toString();

        // 이전 질문, 답변, 번호 불러오기
        RedisResponse.ParentQuestion dto = getRedisParentHelper.exec(sessionKey, parentId);
        String parentQuestionNumber = dto.getParentQuestionNumber();
        String parentQuestion = dto.getParentQuestion();
        String parentAnswer = dto.getParentAnswer();

        // 꼬리질문 생성
        String question = createTailHelper.exec(parentQuestion, parentAnswer);

        // 꼬리질문 번호 생성
        String newTailCount = createtailCountHelper.exec(parentQuestionNumber, tailCountKey);

        // redis에 저장
        saveRedisTailHelper.exec(tailSessionKey, sessionKey,fieldId,question,newTailCount);

        return new SessionInfoResponse.NewQuestion(fieldId, question, newTailCount);
    }
}
