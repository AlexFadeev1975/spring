package org.example.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openapiDescription() {

        Contact contact = new Contact();
        contact.name("Alex Fadeev")
                .email("alex.fadeev43@outlook.com");

        Info info = new Info();
        info.title("API for client")
                .version("1.0.0")
                .description("API for news-service")
                .contact(contact);

        return new OpenAPI().info(info);
    }
}
