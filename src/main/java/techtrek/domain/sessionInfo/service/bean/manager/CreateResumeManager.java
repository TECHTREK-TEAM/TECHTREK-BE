package techtrek.domain.sessionInfo.service.bean.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.service.bean.small.GetSessionInfoDAOBean;
import techtrek.domain.user.entity.User;
import techtrek.global.gpt.service.bean.manager.CreatePromptManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptTemplateManager;

@Component
@RequiredArgsConstructor
public class CreateResumeManager {
    private final CreatePromptTemplateManager createPromptTemplateManager;
    private final CreatePromptManager createPromptManager;

    private final GetSessionInfoDAOBean getSessionInfoDAOBean;

    // 이력서 질문 생성
    public String exec(User user, String sessionId){
        // 세션 정보 불러오기
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);

        // 이력서와 기업 설명 불러오기
        String resume = user.getResume();
        String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription();

        // 프롬프트 생성, gpt로 질문 생성
        String promptTemplate = createPromptTemplateManager.exec("prompts/resume_question_prompt.txt");
        String prompt = String.format(promptTemplate, resume, enterpriseDescription);

        return createPromptManager.exec(prompt);

    }
}
