package techtrek.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.stack.repository.StackRepository;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;
import techtrek.global.gpt.service.OpenAiService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final UserRepository userRepository;
    private final StackRepository stackRepository;
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
              "resume": "이력서를 5줄 이내로 간결하게 요약해줘. 특히 어떤 경험이 있었는지, 어떤 기술 스택을 사용했는지 중점적으로 알려줘.",
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

        try {
            // 4. JSON 파싱
            ResumeResponse summary = objectMapper.readValue(gptResponse, ResumeResponse.class);

            // 5. DB에 저장
            User user = userRepository.findById("1")
                    .orElseThrow(() -> new GlobalException(ResponseCode.USER_NOT_FOUND));
            user.setUserGroup(summary.getGroup());
            user.setSeniority(summary.getSeniority());
            user.setResume(summary.getResume());

            user = userRepository.save(user);

            // 6. 기존 스택 삭제 후 새 스택 저장 (덮어쓰기)
            stackRepository.deleteByUserId(user.getId());

            User finalUser = user;
            List<Stack> newStacks = summary.getStacks().stream()
                    .map(stackDto -> {
                        Stack stack = new Stack();
                        stack.setId(UUID.randomUUID().toString());
                        stack.setStackName(stackDto.getStackName());
                        stack.setUser(finalUser);
                        return stack;
                    }).collect(Collectors.toList());

            stackRepository.saveAll(newStacks);

            return summary;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답을 JSON으로 파싱 실패: " + gptResponse, e);
        }
    }
}

