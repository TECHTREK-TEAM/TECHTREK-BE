package techtrek.global.openAI.Embedding.service.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import techtrek.global.openAI.Embedding.dto.EmbeddingRequest;
import techtrek.global.openAI.Embedding.dto.EmbeddingResponse;

import java.util.List;

// 벡터 변환, 유사도 계산
@Component
public class Embedding {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.embedding-model}")
    private String model;

    @Value("${openai.embedding-url}")
    private String apiUrl;

    // Embedding 생성
    public List<Double> getEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        EmbeddingRequest request = new EmbeddingRequest(model, text);
        HttpEntity<EmbeddingRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<EmbeddingResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                EmbeddingResponse.class
        );

        return response.getBody().data().get(0).embedding();
    }

    // Cosine similarity 계산
    public double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            dot += vec1.get(i) * vec2.get(i);
            normA += Math.pow(vec1.get(i), 2);
            normB += Math.pow(vec2.get(i), 2);
        }
        double similarity = dot / (Math.sqrt(normA) * Math.sqrt(normB));
        return Math.round(similarity * 10.0) / 10.0;  // 소수 첫째 자리까지 반올림
    }
}