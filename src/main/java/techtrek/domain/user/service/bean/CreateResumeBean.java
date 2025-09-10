package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.domain.user.service.small.SaveResumeDAO;
import techtrek.domain.user.service.small.SaveStackDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.JsonRead;
import techtrek.global.openAI.chat.service.ChatService;
import techtrek.global.openAI.chat.service.common.Prompt;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreateResumeBean {
    //상수 정의
    private static final String PROMPT_PATH_RESUME = "prompts/resume_summary_prompt.txt";

    private final Prompt createPromptTemplateUtil;
    private final ChatService createPromptUtil;
    private final JsonRead changeJsonReadUtil;

    private final SaveResumeDAO saveResumeDAO;
    private final SaveStackDAO saveStackDAO;
    private final GetUserDAO getUserDAO;

    // 이력서 추출
    public UserResponse.Resume exec(MultipartFile file) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // TODO:사용자 조회
        User user = getUserDAO.exec("1");

        // 이력서 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PDF_PARSING_FAILED);
        }

        // 프롬프트 생성 후 gpt 호출
        String promptTemplate = createPromptTemplateUtil.exec(PROMPT_PATH_RESUME);
        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = createPromptUtil.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        UserResponse.Resume object = changeJsonReadUtil.exec(gptResponse, UserResponse.Resume.class);

        // 이력서, 스택 등 값 저장
        saveResumeDAO.exec(user, object.getGroup(), object.getSeniority(), object.getResume());
        saveStackDAO.exec(user, object.getStacks());

        return object;
    }
}
