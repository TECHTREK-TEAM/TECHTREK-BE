package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.global.gpt.service.OpenAiService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final OpenAiService openAiService;

    // 이략서 업로드
    public String createResume(MultipartFile file) throws IOException {

        // 1. PDF에서 텍스트 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        }

        // 2. GPT에 요청
        String prompt = "다음 이력서 내용을 5줄 이내로 요약해줘:\n\n" + extractedText;
        String summary = openAiService.askToGpt(prompt);

        return summary;

    }
}
