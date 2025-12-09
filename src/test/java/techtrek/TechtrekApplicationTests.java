package techtrek;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest
class TechtrekApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("✅ CI 통과 확인용 테스트");
	}

}
