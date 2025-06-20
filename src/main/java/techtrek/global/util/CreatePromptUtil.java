package techtrek.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import techtrek.global.gpt.dto.OpenAiMessage;
import techtrek.global.gpt.dto.OpenAiRequest;
import techtrek.global.gpt.dto.OpenAiResponse;

import java.util.List;

@Component
public class CreatePromptUtil {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String apiUrl;

    public String exec(String prompt){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // GPT에게 줄 메시지 구성
        OpenAiMessage message = new OpenAiMessage("user", prompt);
        OpenAiRequest request = new OpenAiRequest(model, List.of(message));

        // 요청 생성
        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                apiUrl, // 어디로 보낼지
                HttpMethod.POST, // POST 방식
                entity,  // 요청 본문 + 헤더
                OpenAiResponse.class  // 응답이 어떤 형식으로 오는지 (JSON-> JAVA)
        );

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}
