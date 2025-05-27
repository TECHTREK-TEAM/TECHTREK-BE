package techtrek.domain.user.service.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.OpenAiService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CreateResumeBean {

    private final OpenAiService openAiService;
    private final SaveResumeDAOBean saveResumeDAOBean;
    private final SaveStackDAOBean saveStackDAOBean;
    private final GetUserDAOBean getUserDAOBean;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 인증코드 검증
    public ResumeResponse exec(MultipartFile file) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // 이력서 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PARSING_FAILED);
        }

        // 프롬프트 생성
        String promptTemplate;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("prompts/resume_summary_prompt.txt")) {
            if (is == null) throw new CustomException(ErrorCode.PROMPT_NOT_FOUND);
            promptTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PROMPT_READ_FAILED);
        }

        // GPT 호출
        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = openAiService.askToGpt(prompt);

        // JSON 파싱
        ResumeResponse summary;
        try {summary = objectMapper.readValue(gptResponse, ResumeResponse.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.PROMPT_PARSING_FAILED);
        }

        // 사용자 불러오기
        User user = getUserDAOBean.exec("1");

        // 이력서, 스택 등 값 저장
        saveResumeDAOBean.exec(user, summary.getGroup(), summary.getSeniority(), summary.getResume());
        saveStackDAOBean.exec(user, summary.getStacks());


        return summary;

    }
}
