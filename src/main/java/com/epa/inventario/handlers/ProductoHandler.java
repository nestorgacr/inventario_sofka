package com.epa.inventario.handlers;

import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.models.dto.*;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.InventarioPaginadoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoHandler {
    private final CrearProductoUseCase crearProductoUseCase;
    private final RegistrarInventarioUseCase registrarInventarioUseCase;

    private final InventarioPaginadoUseCase inventarioPaginadoUseCase;

    public ProductoHandler(CrearProductoUseCase crearProductoUseCase, RegistrarInventarioUseCase registrarInventarioUseCase, InventarioPaginadoUseCase inventarioPaginadoUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.registrarInventarioUseCase = registrarInventarioUseCase;
        this.inventarioPaginadoUseCase = inventarioPaginadoUseCase;
    }

    public Mono<ServerResponse> crearProducto(ServerRequest request) {
        return request.bodyToMono(CreateProductRequestDto.class).flatMap(
                producto -> {
                    return crearProductoUseCase.apply(producto)
                            .flatMap(result -> {
                                return ServerResponse.ok().bodyValue(result);
                            })
                            .onErrorResume(error -> {
                                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .bodyValue(error.getMessage());
                            });
                }
        );
    }

    public Mono<ServerResponse> registrarInventario(ServerRequest request) {
        return request.bodyToMono(ActualizarInventarioRequestDto.class).flatMap(
                transaccion -> registrarInventarioUseCase.apply(transaccion)
                        .flatMap(transaccionDto ->
                                ServerResponse.ok()
                                        .bodyValue(transaccionDto))
                        .onErrorResume(DatosNoEncontrados.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(ex.getMessage())
                        )
        );
    }


    public Mono<ServerResponse> registrarInventarioMasivo(ServerRequest request) {
        return request.bodyToFlux(ActualizarInventarioRequestDto.class)
                .flatMap(registrarInventarioUseCase)
                .collectList()
                .flatMap(transaccionDtos -> ServerResponse.ok()
                        .bodyValue(transaccionDtos))
                .onErrorResume(DatosNoEncontrados.class, ex ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .bodyValue(ex.getMessage())
                );
    }

    public Mono<ServerResponse> obtenerProductoPaginado(ServerRequest request)
    {

        String pagina = request.pathVariable("pagina");
        String tamanno = request.pathVariable("tamaño");

        int numeroPagina = Integer.parseInt(pagina);
        int tamanoPagina = Integer.parseInt(tamanno);

      Flux<ProductoPaginadoDto> list = inventarioPaginadoUseCase.apply(numeroPagina, tamanoPagina)
              .onErrorMap(
              DatosNoEncontrados.class, ex -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                      ex.getLocalizedMessage()
              ));

        return ServerResponse.ok()
                .body(list, ProductoPaginadoDto.class);
    }


}
