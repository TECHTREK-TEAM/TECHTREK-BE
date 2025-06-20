package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.domain.user.service.dao.SaveResumeDAO;
import techtrek.domain.user.service.dao.SaveStackDAO;
import techtrek.global.gpt.service.bean.util.CreateJsonReadUtil;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.bean.util.CreatePromptUtil;
import techtrek.global.gpt.service.bean.util.CreatePromptTemplateUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreateResumeBean {

    private final CreatePromptTemplateUtil createPromptTemplateUtil;
    private final CreatePromptUtil createPromptUtil;
    private final CreateJsonReadUtil createJsonReadUtil;

    private final SaveResumeDAO saveResumeDAO;
    private final SaveStackDAO saveStackDAO;
    private final GetUserDAO getUserDAO;

    // 이력서 추출
    public ResumeResponse exec(MultipartFile file) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 이력서 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PARSING_FAILED);
        }

        // 프롬프트 생성 후 gpt 호출
        String promptTemplate = createPromptTemplateUtil.exec("prompts/resume_summary_prompt.txt");

        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = createPromptUtil.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        ResumeResponse object = createJsonReadUtil.exec(gptResponse, ResumeResponse.class);

        // 이력서, 스택 등 값 저장
        saveResumeDAO.exec(user, object.getGroup(), object.getSeniority(), object.getResume());
        saveStackDAO.exec(user, object.getStacks());

        return object;
    }
}
