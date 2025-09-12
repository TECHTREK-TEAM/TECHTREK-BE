package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.stack.repository.StackRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.JsonRead;
import techtrek.global.openAI.chat.service.component.Chat;
import techtrek.global.openAI.chat.service.common.Prompt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateResume {
    //상수 정의
    private static final String PROMPT_PATH_RESUME = "prompts/resume_summary_prompt.txt";

    private final UserRepository userRepository;
    private final StackRepository stackRepository;
    private final Prompt Prompt;
    private final Chat chat;
    private final JsonRead jsonRead;

    // 이력서 추출
    public UserResponse.Resume exec(MultipartFile file) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // TODO:사용자 조회
        User user = userRepository.findById("1").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 이력서 추출
        String extractedText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PDF_PARSING_FAILED);
        }

        // 프롬프트 생성 후 gpt 호출
        String promptTemplate = Prompt.exec(PROMPT_PATH_RESUME);
        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = chat.exec(prompt);

        // JSON 파싱 (JSON -> 객체)
        UserResponse.Resume resumeResult = jsonRead.exec(gptResponse, UserResponse.Resume.class);

        // 이력서, 스택 등 값 저장
        if (user.getRole() != null) user.setRole(resumeResult.getRole());
        if (user.getSeniority() != null) user.setSeniority(resumeResult.getSeniority());
        if (user.getResume() != null) user.setResume(resumeResult.getResume());
        userRepository.save(user);

        // 새로운 스택 리스트 생성
        List<Stack> newStacks = resumeResult.getStacks().stream()
                .map(dto -> Stack.builder()
                        .id(UUID.randomUUID().toString())
                        .stackName(dto.getStackName())
                        .user(user)
                        .build())
                .toList();
        stackRepository.saveAll(newStacks);

        return resumeResult;
    }
}
