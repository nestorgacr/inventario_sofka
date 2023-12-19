package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.models.dto.*;
import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Producto;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.utils.TransaccionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductosUseCaseTest {

    @Mock
    private IProductoRepository repository;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;


    @InjectMocks
    private CrearProductoUseCase crearProductoUseCase;


    @Test
    @DisplayName("Usecase -> Crear producto")
    public void CrearProducto() {


        CreateProductRequestDto createProductRequestDto = new CreateProductRequestDto();
        createProductRequestDto.setNombre("test");
        createProductRequestDto.setExistencia(1000);
        createProductRequestDto.setPrecio(100);

        Producto producto = new Producto();
        producto.setNombre("test");
        producto.setExistencia(1000);
        producto.setPrecio(100);

        ProductoDto productoDto = new ProductoDto();
        productoDto.setNombre("test");
        productoDto.setExistencia(1000);
        productoDto.setPrecio(100);

        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.PRODUCTO_NUEVO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);


        producto.setTransacciones( List.of(transaccion));


        when(repository.save(any(Producto.class)))
                .thenReturn(Mono.just(producto));


        TransaccionDto tran = TransaccionUtil.entityToDto(transaccion);
        tran.setIdProducto("1");

        LogDto log = new LogDto.Builder("1")
                .addTipo(TipoTransaccion.PRODUCTO_NUEVO)
                .addData(tran)
                .build();

        doNothing().when(rabbitMqPublisher).publishTransaccion(log);

        ErrorDto errorDto = new ErrorDto.Builder()
                .addTipo(TipoMensaje.ERROR)
                .addData("Error").build();

        doNothing().when(rabbitMqPublisher).publishError(errorDto);


        StepVerifier.create(crearProductoUseCase.apply(createProductRequestDto))
                .expectNext(productoDto)
                .verifyComplete();
    }

}
