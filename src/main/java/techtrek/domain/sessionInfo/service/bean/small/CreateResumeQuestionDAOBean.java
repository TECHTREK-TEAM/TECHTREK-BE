package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.user.entity.User;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.OpenAiService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CreateResumeQuestionDAOBean {
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final OpenAiService openAiService;

    // 이력서 질문 생성
    public String exec(User user, String sessionId){
        // 세션 정보 불러오기
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);

        // 이력서와 기업 설명 불러오기
        String resume = user.getResume();
        String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription();

        // 프롬프트 생성
        String promptTemplate;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("prompts/resume_question_prompt.txt")) {
            if (is == null) throw new CustomException(ErrorCode.PROMPT_NOT_FOUND);
            promptTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PROMPT_READ_FAILED);
        }

        // gpt 호출
        String prompt = String.format(promptTemplate, resume, enterpriseDescription);
        return openAiService.askToGpt(prompt);

    }
}
