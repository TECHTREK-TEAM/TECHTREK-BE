package techtrek.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechTrek API")
                        .version("v1.0")
                        .description("TechTrek 기업 맞춤형 면접 플랫폼 API 문서입니다.\n\n"))
                // JWT Bearer 인증 scheme 등록
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // 모든 api 요청에 jwt 인증 필요
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
