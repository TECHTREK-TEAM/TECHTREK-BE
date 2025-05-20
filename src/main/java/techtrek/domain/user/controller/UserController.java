package techtrek.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.service.ResumeService;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ResumeService resumeService;

    // 이력서 업로드
    @PostMapping("/resume")
    public ResponseEntity<String> createResume(@RequestPart MultipartFile file) throws IOException {
        String response = resumeService.createResume(file);

        return ResponseEntity.ok(response);
    }}
