package com.epa.inventario.usecase;


import reactor.core.publisher.Flux;

@FunctionalInterface
public interface PaginacionFuncion<T> {
    Flux<T> apply(int skip, int take);
}
