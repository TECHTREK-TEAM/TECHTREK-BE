package techtrek;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
		"openai.api-key=dummy-key",
		"openai.model=gpt-3.5-turbo",
		"openai.url=https://api.openai.com/v1/chat/completions"
})
class TechtrekApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("✅ CI 통과 확인용 테스트");
	}

}
