package techtrek.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import techtrek.global.gpt.dto.OpenAiMessage;
import techtrek.global.gpt.dto.OpenAiRequest;
import techtrek.global.gpt.dto.OpenAiResponse;
import techtrek.global.gpt.service.bean.CreateGPTBean;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final CreateGPTBean createGPTBean;

    public String askToGpt(String prompt) {
        return createGPTBean.exec(prompt);

    }
}

