package techtrek.domain.sessionInfo.service.bean.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.service.bean.small.GetSessionInfoDAOBean;
import techtrek.domain.user.entity.User;
import techtrek.global.gpt.service.bean.util.CreatePromptUtil;
import techtrek.global.gpt.service.bean.util.CreatePromptTemplateUtil;

@Component
@RequiredArgsConstructor
public class CreateResumeUtil {
    private final CreatePromptTemplateUtil createPromptTemplateUtil;
    private final CreatePromptUtil createPromptUtil;

    private final GetSessionInfoDAOBean getSessionInfoDAOBean;

    // 이력서 질문 생성
    public String exec(User user, String sessionId){
        // 세션 정보 불러오기
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);

        // 이력서와 기업 설명 불러오기
        String resume = user.getResume();
        String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription();

        // 프롬프트 생성, gpt로 질문 생성
        String promptTemplate = createPromptTemplateUtil.exec("prompts/resume_question_prompt.txt");
        String prompt = String.format(promptTemplate, resume, enterpriseDescription);

        return createPromptUtil.exec(prompt);

    }
}
