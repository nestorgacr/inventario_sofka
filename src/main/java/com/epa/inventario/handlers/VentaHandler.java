package com.epa.inventario.handlers;

import com.epa.inventario.models.dto.*;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.InventarioPaginadoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VentaHandler {
    private final CrearProductoUseCase crearProductoUseCase;
    private final RegistrarInventarioUseCase registrarInventarioUseCase;

    private final InventarioPaginadoUseCase inventarioPaginadoUseCase;

    public VentaHandler(CrearProductoUseCase crearProductoUseCase, RegistrarInventarioUseCase registrarInventarioUseCase, InventarioPaginadoUseCase inventarioPaginadoUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.registrarInventarioUseCase = registrarInventarioUseCase;
        this.inventarioPaginadoUseCase = inventarioPaginadoUseCase;
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

    public Mono<ServerResponse> obtenerProductoPaginado(ServerRequest request)
    {

        String pagina = request.pathVariable("pagina");
        String tamanno = request.pathVariable("tamaño");

        int numeroPagina = Integer.parseInt(pagina);
        int tamanoPagina = Integer.parseInt(tamanno);

      Flux<ProductoPaginadoDto> list = inventarioPaginadoUseCase.apply(numeroPagina, tamanoPagina);

        return ServerResponse.ok()
                .body(list, ProductoPaginadoDto.class);
    }


}
