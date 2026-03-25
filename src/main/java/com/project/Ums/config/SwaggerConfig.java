package com.project.Ums.config;

import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${SWAGGER_DEV_URL:http://localhost:8080}")
    private String devUrl;

    @Value("${SWAGGER_PROD_URL:https://ums-production.com}")
    private String prodUrl;

    @Value("${SWAGGER_CONTACT_EMAIL:saransdhiman.try@gmail.com}")
    private String contactEmail;

    @Value("${SWAGGER_CONTACT_URL:https://github.com/Saransh-27/User-Management-System}")
    private String contactUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("User Management System API")
                        .version("1.0.0")
                        .description("""
                                ### 👥 User Management System APIs
                                
                                - 🔐 User Authentication & Authorization
                                - 👤 Complete User CRUD Operations
                                - 📧 Email Verification with OTP
                                - 🛡️ Role-based Access Control (ADMIN/USER)
                                - 🔑 Secure APIs using JWT Authentication
                                - 📊 User Management Dashboard
                                """)
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Development Team")
                                .email(contactEmail)
                                .url(contactUrl))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )

                // 🌍 Server configuration
                .servers(List.of(
                        new Server()
                                .url(devUrl)
                                .description("Local Development Server"),
                        new Server()
                                .url(prodUrl)
                                .description("Production Server")
                ))

                // 🔐 Enable JWT globally in Swagger
                                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\"")
                        )
                );
    }
}