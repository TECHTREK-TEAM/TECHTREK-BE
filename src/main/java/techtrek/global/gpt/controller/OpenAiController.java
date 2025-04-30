package techtrek.global.gpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import techtrek.global.gpt.service.OpenAiService;

@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @GetMapping("/test-gpt")
    public String testGpt() {
        String prompt = "안녕하세요 김윤아님!이라고 응답해줘";
        return openAiService.askToGpt(prompt);
    }
}
