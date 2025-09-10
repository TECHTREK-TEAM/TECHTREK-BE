package techtrek.global.openAI.Embedding.dto;

import java.util.List;

// Embedding API 응답 전체
public record EmbeddingResponse(List<EmbeddingData> data) {}