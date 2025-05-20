package techtrek.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.global.gpt.service.OpenAiService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeResponse createResume(MultipartFile file) throws IOException {
        // 1. PDF 텍스트 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        }

        // 2. GPT 프롬프트 작성
        String prompt = """
            다음 이력서를 요약해서 JSON으로 응답해줘. 형식은 다음과 같아:

            {
              "group": "직군 (예: FrontEnd Developer 또는 BackEnd Developer)",
              "seniority": "연차가 있을 경우 숫자로만, 연차가 없으면 지망생",
              "text": "이력서 요약",
              "stacks": [
                {
                  "stackName": "기술 이름"
                }
              ]
            }

            이력서:
            ---
            %s
            ---
            """.formatted(extractedText);

        // 3. GPT 호출
        String gptResponse = openAiService.askToGpt(prompt);

        // 4. JSON 파싱
        try {
            ResumeResponse summary = objectMapper.readValue(gptResponse, ResumeResponse.class);

            return summary;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답을 JSON으로 파싱 실패: " + gptResponse, e);
        }
    }
}

