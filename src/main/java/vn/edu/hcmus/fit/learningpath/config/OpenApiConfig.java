package vn.edu.hcmus.fit.learningpath.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Academic Advisor API Documentation")
                        .version("1.0.0")
                        .description("This is the API documentation for the Academic Advisor System. " +
                                "It provides endpoints for student profiles, academic progress, recommendations, and notifications.")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("support@hcmus.edu.vn"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
