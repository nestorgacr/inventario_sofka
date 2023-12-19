package com.epa.inventario.routes;

import com.epa.inventario.handlers.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductoRouter {
    private final ProductoHandler handler;

    public ProductoRouter(ProductoHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionProductos() {
        return RouterFunctions.route()
                .path("/Producto", builder ->
                        builder
                                .POST("/Crear", handler::crearProducto)
                                .POST("/RegistrarInventario", handler::registrarInventario)
                                .POST("/RegistrarInventarioMasivo", handler::registrarInventarioMasivo)
                )
                .build();
    }
}
