package com.epa.inventario.routes;

import com.epa.inventario.handlers.ProductoHandler;
import com.epa.inventario.models.dto.ProductoPaginadoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RouterOperations({
            @RouterOperation(
                    path = "/Producto/InventarioPaginado/{tamaño}/{pagina}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    beanClass = ProductoHandler.class,
                    beanMethod = "obtenerProductoPaginado",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            tags = "Productos",
                            operationId = "listarInventario",
                            parameters = {
                                    @Parameter(name = "tamaño", in = ParameterIn.PATH, required = true, description = "Cantidad de artículos por página"),
                                    @Parameter(name = "pagina", in = ParameterIn.PATH, required = true, description = "Página")
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de articulos en inventario",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoPaginadoDto.class)))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionProductos() {
        return RouterFunctions.route()
                .path("/Producto", builder ->
                        builder
                                .POST("/Crear", handler::crearProducto)
                                .POST("/RegistrarInventario", handler::registrarInventario)
                                .POST("/RegistrarInventarioMasivo", handler::registrarInventarioMasivo)
                                .GET("/InventarioPaginado/{tamaño}/{pagina}", handler::obtenerProductoPaginado)
                )
                .build();
    }
}
