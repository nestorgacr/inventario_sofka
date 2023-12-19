package com.epa.inventario.routes;

import com.epa.inventario.handlers.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class VentaRouter {
    private final ProductoHandler handler;

    public VentaRouter(ProductoHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionVentas() {
        return RouterFunctions.route()
                .path("/Venta", builder ->
                        builder
                                .POST("/AlPorMenor", handler::crearProducto)
                                .POST("/AlPorMayor", handler::registrarInventario)
                )
                .build();
    }
}
