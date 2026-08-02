package com.tellme.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 / Swagger UI configuration.
 *
 * <p>Exposes interactive API documentation at:
 * <ul>
 *   <li>{@code /swagger-ui.html} — Swagger UI</li>
 *   <li>{@code /v3/api-docs} — Raw OpenAPI JSON spec</li>
 * </ul>
 *
 * <p>Authentication in the UI: click the <em>Authorize</em> button and enter
 * your Bearer token (obtained from {@code POST /api/auth/login}) in the format:
 * <pre>{@code Bearer <your-token-here>}</pre>
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    /**
     * Defines the OpenAPI specification metadata and Bearer token security scheme.
     *
     * @return configured {@link OpenAPI} bean
     */
    @Bean
    public OpenAPI tellmeOpenApi() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("TellMe API")
                        .version("1.0.0")
                        .description("""
                                **TellMe** is an open-source student feedback, complaint, and discussion platform
                                designed for universities, academic departments, and student organizations.

                                ## Authentication
                                Most endpoints require a Bearer token. Obtain one by calling `POST /api/auth/login`.
                                Include the token in subsequent requests:
                                ```
                                Authorization: Bearer <your-token>
                                ```

                                ## Source Code
                                [github.com/Zulfatah69/tellme](https://github.com/Zulfatah69/tellme)
                                """)
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .contact(new Contact()
                                .name("TellMe Contributors")
                                .url("https://github.com/Zulfatah69/tellme/issues")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("UUID")
                                        .description("Session token obtained from POST /api/auth/login")));
    }
}
