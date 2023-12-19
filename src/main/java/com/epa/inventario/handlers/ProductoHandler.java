package com.epa.inventario.handlers;

import com.epa.inventario.models.dto.ActualizarInventarioRequestDto;
import com.epa.inventario.models.dto.CreateProductRequestDto;
import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.models.dto.TransaccionDto;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ProductoHandler {
    private final CrearProductoUseCase crearProductoUseCase;
    private final RegistrarInventarioUseCase registrarInventarioUseCase;

    public ProductoHandler(CrearProductoUseCase crearProductoUseCase, RegistrarInventarioUseCase registrarInventarioUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.registrarInventarioUseCase = registrarInventarioUseCase;
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

    public Mono<ServerResponse> registrarInventario(ServerRequest request)
    {
        return request.bodyToMono(ActualizarInventarioRequestDto.class).flatMap(
                transaccion -> {
                    Mono<TransaccionDto>  temp =  registrarInventarioUseCase.apply(transaccion);
                    return ServerResponse.ok()
                            .body(temp, ProductoDto.class);
                }
        );
    }

    public Mono<ServerResponse> registrarInventarioMasivo(ServerRequest request) {
        return request.bodyToFlux(ActualizarInventarioRequestDto.class)
                .flatMap(registrarInventarioUseCase)
                .collectList()
                .flatMap(transaccionDtos -> ServerResponse.ok()
                        .bodyValue(transaccionDtos));
    }

}
