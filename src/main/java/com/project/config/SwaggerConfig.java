package com.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private String schemeName = "bearerAuth";
    private String bearerFormat = "JWT";
    private String scheme = "bearer";

    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                   .addSecurityItem(new SecurityRequirement()
                                    .addList(schemeName))
                   .components(new Components()
                                    .addSecuritySchemes(schemeName,
                                                        new SecurityScheme()
                                                        .name(schemeName)
                                                        .type(SecurityScheme.Type.HTTP)
                                                        .bearerFormat(bearerFormat)
                                                        .in(SecurityScheme.In.HEADER)
                                                        .scheme(scheme)
                   )

            ).info(new Info()
                   .title("Super Security API")
                   .description("API Rest for testing security")
                   .version("1.0"));
    }
}
