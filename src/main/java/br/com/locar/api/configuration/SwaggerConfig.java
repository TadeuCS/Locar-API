package br.com.locar.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI initOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Locar - API")
                        .description("API de exemplo de microserviço construído em SpringBoot")
                        .version("v0.0.1")
                );
    }

}

