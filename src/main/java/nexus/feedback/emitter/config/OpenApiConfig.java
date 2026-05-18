package nexus.feedback.emitter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Feedback Emitter API")
                        .version("0.0.1")
                        .description("Microserviço gerador de carga assíncrona de alta performance utilizando Java 21 e Virtual Threads."));
    }
}
