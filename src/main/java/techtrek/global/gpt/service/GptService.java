package techtrek.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.global.gpt.service.bean.util.CreatePromptUtil;

@Service
@RequiredArgsConstructor
public class GptService {
    private final CreatePromptUtil createPromptHelper;

    public String createPrompt(String prompt) {
        return createPromptHelper.exec(prompt);

    }
}

