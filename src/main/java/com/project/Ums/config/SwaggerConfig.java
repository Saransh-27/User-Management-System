package com.project.Ums.config;

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
                                .email("support@ums.com")
                                .url("https://github.com/Saransh-27/User-Management-System"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )

                // 🌍 Server configuration
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://ums-production.com")
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