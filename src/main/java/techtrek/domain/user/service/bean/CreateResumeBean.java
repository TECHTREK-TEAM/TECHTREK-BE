package techtrek.domain.user.service.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.helper.CreateResumeHelper;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.domain.user.service.bean.small.SaveResumeDAOBean;
import techtrek.domain.user.service.bean.small.SaveStackDAOBean;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.bean.CreateGPTBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CreateResumeBean {

    private final SaveResumeDAOBean saveResumeDAOBean;
    private final SaveStackDAOBean saveStackDAOBean;
    private final GetUserDAOBean getUserDAOBean;
    private final CreateResumeHelper createResumeHelper;

    // 인증코드 검증
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

        // 이력서 키워드 불러오기
        ResumeResponse summary = createResumeHelper.exec(extractedText);

        // 이력서, 스택 등 값 저장
        saveResumeDAOBean.exec(user, summary.getGroup(), summary.getSeniority(), summary.getResume());
        saveStackDAOBean.exec(user, summary.getStacks());


        return summary;

    }
}
