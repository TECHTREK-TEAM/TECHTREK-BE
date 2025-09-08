package techtrek.domain.interview.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.interview.entity.SessionInfo;
import techtrek.domain.interview.service.small.GetSessionInfoDAO;
import techtrek.domain.user.entity.User;
import techtrek.global.gpt.prompt.Prompt;
import techtrek.global.gpt.prompt.PromptTemplate;

@Component
@RequiredArgsConstructor
public class CreateResumeUtil {
    private final PromptTemplate createPromptTemplateUtil;
    private final Prompt createPromptUtil;

    private final GetSessionInfoDAO getSessionInfoDAO;

    // 이력서 질문 생성
    public String exec(User user, String sessionId){
        // 세션 정보 불러오기
        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);

        // 이력서와 기업 설명 불러오기
        String resume = user.getResume();
        // String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription();

        // 프롬프트 생성, gpt로 질문 생성
        String promptTemplate = createPromptTemplateUtil.exec("prompts/resume_question_prompt.txt");
        String prompt = String.format(promptTemplate, resume, "기업설명");

        return createPromptUtil.exec(prompt);

    }
}
