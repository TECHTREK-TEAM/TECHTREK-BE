package techtrek.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("TechTrek API")
                .version("v1.0")
                .description("TechTrek 기업 맞춤형 면접 플랫폼 API 문서입니다.\n\n");
        return new OpenAPI()
                .info(info);
    }
}
