package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.models.dto.*;
import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.utils.TransaccionUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class RegistrarVentaUseCase implements Function<VentaRequestDto, Mono<TransaccionDto>>
{
    private final IProductoRepository repositorio;

    private final RabbitMqPublisher eventBus;

    public RegistrarVentaUseCase(IProductoRepository repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<TransaccionDto> apply(VentaRequestDto data) {

        Transaccion transaccion = new Transaccion();
        transaccion.setIdProducto(data.getIdProducto());

        transaccion.setCantidad(data.getCantidad());
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.VENTA.toString());
        transaccion.setDescuento(0);

        UUID uuid = UUID.randomUUID();
        transaccion.setId(uuid.toString());

        return repositorio.findById(data.getIdProducto())
                .flatMap(producto -> {
                    transaccion.setPrecio(producto.getPrecio());
                    producto.getTransacciones().add(transaccion);
                    producto.setExistencia(producto.getExistencia() - data.getCantidad());
                    return repositorio.save(producto)
                            .doOnSuccess(inv -> {
                                LogDto log = new LogDto.Builder(data.getIdProducto())
                                        .addTipo(TipoTransaccion.VENTA)
                                        .addData(transaccion)
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
                            .map(savedProducto -> TransaccionUtil.entityToDto(transaccion));
                })
                .switchIfEmpty(
                        Mono.defer(() -> {
                            eventBus.publishError(new ErrorDto.Builder()
                                    .addTipo(TipoMensaje.ERROR)
                                    .addData("No se puede registrar la venta, el producto no existe:" + data.getIdProducto()).build());

                            return Mono.error( new DatosNoEncontrados("No se puede registrar la venta, el producto no existe:" + data.getIdProducto()));
                        } ));
    }

}
