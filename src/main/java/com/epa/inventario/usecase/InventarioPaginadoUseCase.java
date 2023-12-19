package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import reactor.core.publisher.Flux;

public class InventarioPaginadoUseCase implements PaginacionFuncion {
    private final IProductoRepository repositorio;

    private final RabbitMqPublisher eventBus;

    public InventarioPaginadoUseCase(IProductoRepository repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Flux apply(int skip, int take) {
        return null;
    }
}
