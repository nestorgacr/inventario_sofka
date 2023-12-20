package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.models.dto.ErrorDto;
import com.epa.inventario.models.dto.ProductoPaginadoDto;
import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.utils.ProductoPaginadoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InventarioPaginadoUseCase implements PaginacionFuncion {
    private final IProductoRepository repositorio;

    private final RabbitMqPublisher eventBus;

    public InventarioPaginadoUseCase(IProductoRepository repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }
    @Override
    public Flux<ProductoPaginadoDto> apply(int pagina, int tamanno)
    {
        return repositorio.findAll()
                .skip((long) (pagina - 1) * tamanno)
                .take(tamanno)
                .map(ProductoPaginadoUtil::entityToDto)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            eventBus.publishError(new ErrorDto.Builder()
                                    .addTipo(TipoMensaje.ERROR)
                                    .addData("No hay productos").build());

                            return Mono.error( new DatosNoEncontrados("No hay productos"));
                        } ));
    }
}
