package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.domain.user.service.bean.small.SaveResumeDAOBean;
import techtrek.domain.user.service.bean.small.SaveStackDAOBean;
import techtrek.global.bean.manager.CreateJsonReadManager;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.bean.manager.CreatePromptManager;
import techtrek.global.gpt.service.bean.manager.CreatePromptTemplateManager;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreateResumeBean {

    private final CreatePromptTemplateManager createPromptTemplateHelper;
    private final CreatePromptManager createPromptHelper;
    private final CreateJsonReadManager createJsonReadManager;

    private final SaveResumeDAOBean saveResumeDAOBean;
    private final SaveStackDAOBean saveStackDAOBean;
    private final GetUserDAOBean getUserDAOBean;

    // 이력서 추출
    public ResumeResponse exec(MultipartFile file) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // 사용자 불러오기
        User user = getUserDAOBean.exec("1");

        // 이력서 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PARSING_FAILED);
        }

        // 프롬프트 생성 후 gpt 호출
        String promptTemplate = createPromptTemplateHelper.exec("prompts/resume_summary_prompt.txt");

        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = createPromptHelper.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        ResumeResponse object = createJsonReadManager.exec(gptResponse, ResumeResponse.class);

        // 이력서, 스택 등 값 저장
        saveResumeDAOBean.exec(user, object.getGroup(), object.getSeniority(), object.getResume());
        saveStackDAOBean.exec(user, object.getStacks());

        return object;
    }
}
