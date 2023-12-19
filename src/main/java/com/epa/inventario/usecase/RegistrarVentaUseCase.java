package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.models.dto.ActualizarInventarioRequestDto;
import com.epa.inventario.models.dto.ErrorDto;
import com.epa.inventario.models.dto.LogDto;
import com.epa.inventario.models.dto.TransaccionDto;
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
public class RegistrarVentaUseCase implements Function<ActualizarInventarioRequestDto, Mono<TransaccionDto>>
{
    private final IProductoRepository repositorio;

    private final RabbitMqPublisher eventBus;

    public RegistrarVentaUseCase(IProductoRepository repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<TransaccionDto> apply(ActualizarInventarioRequestDto data) {

        Transaccion transaccion = new Transaccion();
        transaccion.setIdProducto(data.getIdProducto());
        transaccion.setPrecio(data.getPrecio());
        transaccion.setCantidad(data.getExistencia());
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.VENTA.toString());

        UUID uuid = UUID.randomUUID();
        transaccion.setId(uuid.toString());

        return repositorio.findById(data.getIdProducto())
                .flatMap(producto -> {
                    producto.getTransacciones().add(transaccion);
                    producto.setPrecio(data.getPrecio());
                    producto.setExistencia(producto.getExistencia() - data.getExistencia());
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
                });
    }

}