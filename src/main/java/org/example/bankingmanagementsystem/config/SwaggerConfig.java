package org.example.bankingmanagementsystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API аутентификации")
                        .version("1.0.0")
                        .description("Документация для регистрации и входа"))
                .externalDocs(new ExternalDocumentation()
                        .url("resources/static/openapi.yml"));
    }
} //http://localhost:8080/swagger-ui/index.html
