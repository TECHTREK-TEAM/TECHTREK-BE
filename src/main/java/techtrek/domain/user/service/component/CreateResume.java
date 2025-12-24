package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.stack.entity.status.StackType;
import techtrek.domain.stack.repository.StackRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateResume {
    //상수 정의
    private static final String PROMPT_PATH_CREATE_RESUME = "prompts/resume_summary_prompt.txt";

    private final UserHelper userHelper;
    private final UserRepository userRepository;
    private final StackRepository stackRepository;
    private final Gpt gpt;

    @Value("${s3.base-url}")
    private String s3BaseUrl;

    // 이력서 추출
    public UserResponse.Resume exec(MultipartFile file, CustomUserDetails userDetails) {
        // 파일 존재 확인
        if (file == null || file.isEmpty()) throw new CustomException(ErrorCode.RESUME_NOT_FOUND);

        // 사용자 조회
        User user = userHelper.validateUser(userDetails.getId());

        // 이력서 추출
        String extractedText;
        String fileName = file.getOriginalFilename();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractedText = pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESUME_PDF_PARSING_FAILED);
        }

        // gpt 이략서 요약
        UserResponse.Resume result = gpt.exec(PROMPT_PATH_CREATE_RESUME, new Object[]{extractedText}, UserResponse.Resume.class);

        // 이력서, 스택등 값 저장
        saveUserResume(user, result, fileName);

        // 새로운 스택 리스트 생성
        List<Stack> savedStacks = saveStacks(user, result);

        // 반환 dto
        List<UserResponse.Resume.Stack> responseStacks = toResponseStacks(savedStacks);
        return buildResponse(result, responseStacks);
    }


    // 이력서, 스택등 값 저장
    private void saveUserResume(User user,
                                UserResponse.Resume resume,
                                String fileName) {

        user.setPosition(resume.getPosition());
        user.setSeniority(resume.getSeniority());
        user.setResume(resume.getResume());
        user.setResumeName(fileName);

        userRepository.save(user);
    }


    // 새로운 스택 리스트 생성
    private List<Stack> saveStacks(User user, UserResponse.Resume resume) {

        user.getStackList().clear();

        List<Stack> stacks = resume.getStacks().stream()
                .map(dto -> {
                    Stack stack = new Stack();
                    stack.setStackName(dto.getStackName());
                    StackType type = StackType.from(dto.getStackName());
                    stack.setStackUrl(s3BaseUrl + type.name().toLowerCase().replace("_", "-") + ".png");
                    stack.setUser(user);
                    return stack;
                })
                .toList();

        return stackRepository.saveAll(stacks);
    }

    // 반환 dto
    private List<UserResponse.Resume.Stack> toResponseStacks(List<Stack> stacks) {
        return stacks.stream()
                .map(stack -> UserResponse.Resume.Stack.builder()
                        .stackName(stack.getStackName())
                        .stackUrl(stack.getStackUrl())
                        .build()
                )
                .toList();
    }

    private UserResponse.Resume buildResponse(UserResponse.Resume resume,
                                              List<UserResponse.Resume.Stack> stacks) {

        return UserResponse.Resume.builder()
                .position(resume.getPosition())
                .seniority(resume.getSeniority())
                .resume(resume.getResume())
                .stacks(stacks)
                .build();
    }
}
