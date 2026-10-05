package com.api.empresa.Configuracion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI empresaOpenAPI() {

        return new OpenAPI()

            // =================================================
            // INFORMACIÓN DE LA API
            // =================================================

            .info(new Info()
                .title("API Empresa")
                .description(
                    "API REST para la gestión de clientes y usuarios " +
                    "con autenticación mediante JWT y control de roles."
                )
                .version("1.0.0")

                .contact(new Contact()
                    .name("Luis Pinos")
                    .email("admin@empresa.com")
                )

                .license(new License()
                    .name("API Empresa")
                )
            )

            // =================================================
            // AUTENTICACIÓN JWT
            // =================================================

            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearerAuth",

                        new SecurityScheme()
                            .name("Authorization")
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description(
                                "Ingrese el token JWT obtenido mediante " +
                                "POST /api/auth/login"
                            )
                    )
            );
    }
}


