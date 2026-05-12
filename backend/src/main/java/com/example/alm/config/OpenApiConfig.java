
package com.example.alm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ALM API")
                .version("1.0.0")
                .description("Application Lifecycle Management System API")
                .contact(new Contact()
                    .name("ALM Team")
                    .email("support@alm-opensource.com")));
    }
}
