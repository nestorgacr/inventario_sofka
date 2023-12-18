package com.epa.inventario.handlers;

import com.epa.inventario.models.dto.CreateProductRequestDto;
import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.usecase.CrearProductoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ProductoHandler {
    private final CrearProductoUseCase crearProductoUseCase;

    public ProductoHandler(CrearProductoUseCase crearProductoUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
    }

    public Mono<ServerResponse> crearProducto(ServerRequest request)
    {
        return request.bodyToMono(CreateProductRequestDto.class).flatMap(
                producto -> {
                    Mono<ProductoDto>  temp =  crearProductoUseCase.apply(producto);
                    return ServerResponse.ok()
                            .body(temp, ProductoDto.class);
                }
        );
    }
}
