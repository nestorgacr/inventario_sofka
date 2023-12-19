package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.models.dto.ProductoPaginadoDto;
import com.epa.inventario.utils.ProductoPaginadoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class InventarioPaginadoUseCase implements PaginacionFuncion {
    private final IProductoRepository repositorio;

    public InventarioPaginadoUseCase(IProductoRepository repositorio) {
        this.repositorio = repositorio;
    }
    @Override
    public Flux<ProductoPaginadoDto> apply(int pagina, int tamanno)
    {
        return repositorio.findAll().skip((long) (pagina - 1) * tamanno).take(tamanno).map(ProductoPaginadoUtil::entityToDto);
    }
}
