package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.models.dto.CreateProductRequestDto;
import com.epa.inventario.models.dto.ErrorDto;
import com.epa.inventario.models.dto.LogDto;
import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Producto;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.utils.ProductoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class CrearProductoUseCase implements Function<CreateProductRequestDto, Mono<ProductoDto>> {

    private final IProductoRepository repositorio;

    private final RabbitMqPublisher eventBus;

    public CrearProductoUseCase(IProductoRepository repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<ProductoDto> apply(CreateProductRequestDto createProductRequestDto) {

        Producto producto = new Producto();
        producto.setNombre(createProductRequestDto.getNombre());
        producto.setExistencia(createProductRequestDto.getExistencia());
        producto.setPrecio(createProductRequestDto.getPrecio());

        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(createProductRequestDto.getPrecio());
        transaccion.setCantidad(createProductRequestDto.getExistencia());

        List<Transaccion> list = new ArrayList<>();
        list.add(transaccion);

        producto.setTransacciones(list);

        return repositorio.save(producto)
                .doOnSuccess(data -> {
                    LogDto log = new LogDto.Builder(data.getId())
                            .addTipo(TipoTransaccion.INGRESO)
                            .addData(data)
                            .build();
                    eventBus.publishTransaccion(log);
                }).doOnError(
                        error -> {
                            ErrorDto errorDto = new ErrorDto.Builder()
                                    .addTipo(TipoMensaje.ERROR)
                                    .addData(error).build();

                            eventBus.publishError(errorDto);

                        }
                )
                .map(ProductoUtil::entityToDto);

    }
}
