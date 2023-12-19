package com.epa.inventario.handlers;

import com.epa.inventario.models.dto.*;
import com.epa.inventario.usecase.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VentaHandler {
    private final RegistrarVentaUseCase registrarVentaUseCase;

    private final RegistrarVentaAlPorMayorUseCase registrarVentaAlPorMayorUseCase;

    public VentaHandler(RegistrarVentaUseCase registrarVentaUseCase, RegistrarVentaAlPorMayorUseCase registrarVentaAlPorMayorUseCase) {
        this.registrarVentaUseCase = registrarVentaUseCase;
        this.registrarVentaAlPorMayorUseCase = registrarVentaAlPorMayorUseCase;
    }


    public Mono<ServerResponse> registrarVentaAlPorMenor(ServerRequest request) {
        return request.bodyToFlux(VentaRequestDto.class)
                .flatMap(registrarVentaUseCase)
                .collectList()
                .flatMap(transaccionDtos -> ServerResponse.ok()
                        .bodyValue(transaccionDtos));
    }

    public Mono<ServerResponse> registrarVentaAlPorMayor(ServerRequest request) {
        return request.bodyToFlux(VentaRequestDto.class)
                .flatMap(registrarVentaAlPorMayorUseCase)
                .collectList()
                .flatMap(transaccionDtos -> ServerResponse.ok()
                        .bodyValue(transaccionDtos));
    }




}
