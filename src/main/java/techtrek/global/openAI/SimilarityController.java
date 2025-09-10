package techtrek.global.openAI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import techtrek.global.openAI.Embedding.service.component.Embedding;

import java.util.List;

@RestController
@RequestMapping("/similarity")
public class SimilarityController {

    private final Embedding embeddingService;

    public SimilarityController(Embedding embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping
    public int getSimilarity(@RequestParam String text1, @RequestParam String text2) {
        List<Double> vec1 = embeddingService.getEmbedding(text1);
        List<Double> vec2 = embeddingService.getEmbedding(text2);
        return embeddingService.cosineSimilarity(vec1, vec2);
    }
}
