package com.epa.inventario.handlers;

import com.epa.inventario.models.dto.*;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.InventarioPaginadoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import com.epa.inventario.usecase.RegistrarVentaUseCase;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VentaHandler {
    private final RegistrarVentaUseCase registrarVentaUseCase;

    public VentaHandler(RegistrarVentaUseCase registrarVentaUseCase) {

        this.registrarVentaUseCase = registrarVentaUseCase;
    }



    public Mono<ServerResponse> registrarVentaAlPorMenor(ServerRequest request)
    {
        return request.bodyToMono(ActualizarInventarioRequestDto.class).flatMap(
                transaccion -> {
                    Mono<TransaccionDto>  temp =  registrarVentaUseCase.apply(transaccion);
                    return ServerResponse.ok()
                            .body(temp, ProductoDto.class);
                }
        );
    }




}
