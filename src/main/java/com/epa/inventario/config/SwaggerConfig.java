package com.epa.inventario.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI myOpenAPI()
    {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8087/");
        devServer.setDescription("URL del Servidor de Desarrollo");



        Info info = new Info()
                .title("Documentación Inventario Log")
                .version("1.0")
                .description("Modulo de inventario");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
