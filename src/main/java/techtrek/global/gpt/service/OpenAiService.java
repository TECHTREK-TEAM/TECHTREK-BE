package techtrek.global.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import techtrek.global.gpt.dto.OpenAiMessage;
import techtrek.global.gpt.dto.OpenAiRequest;
import techtrek.global.gpt.dto.OpenAiResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${OPENAI_MODEL}")
    private String model;

    @Value("${OPENAI_URL}")
    private String apiUrl;

    public String askToGpt(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // GPT에게 줄 메시지 구성
        OpenAiMessage message = new OpenAiMessage("user", prompt);
        OpenAiRequest request = new OpenAiRequest(model, List.of(message));

        // 요청 생성
        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                OpenAiResponse.class
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}

