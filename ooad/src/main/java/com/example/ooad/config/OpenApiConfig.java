package com.example.ooad.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info=@Info(
        contact=@Contact(
            name="Dang Le Binh",
            email="email@gmail.com",
            url="http://localhost:8080"
        ),
        description="Open api doc for clinic website",
        title="Clinic Backend Spec",
        version="1.0"


    ),
    servers={
        @Server(
            description="local environment",
            url="http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name="Bearer Auth",
    description="Jwt Auth Description",
    scheme="bearer",
    type=SecuritySchemeType.HTTP,
    bearerFormat="JWT",
    in=SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    
}
