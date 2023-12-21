package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.models.dto.*;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Producto;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.utils.TransaccionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UseCaseTest {

    @Mock
    private IProductoRepository repository;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;
    private CrearProductoUseCase crearProductoUseCase;
    private InventarioPaginadoUseCase inventarioPaginadoUseCase;

    private RegistrarInventarioUseCase registrarInventarioUseCase;

    @Mock
    private TransaccionUtil transaccionUtil;

    @BeforeEach
    public void setup() {
        crearProductoUseCase = new CrearProductoUseCase(repository,rabbitMqPublisher);
        inventarioPaginadoUseCase = new InventarioPaginadoUseCase(repository,rabbitMqPublisher);
        registrarInventarioUseCase = new RegistrarInventarioUseCase(repository,rabbitMqPublisher);
    }


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
        producto.setId("1");



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



//        doNothing().when(rabbitMqPublisher).publishTransaccion(any(LogDto.class));


        StepVerifier.create(crearProductoUseCase.apply(createProductRequestDto))
                .expectNextMatches(result -> result.getNombre().equals("test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Usecase -> Crear producto error")
    public void CrearProductoError() {

        CreateProductRequestDto createProductRequestDto = new CreateProductRequestDto();
        createProductRequestDto.setNombre("test");
        createProductRequestDto.setExistencia(1000);
        createProductRequestDto.setPrecio(100);

        when(repository.save(any(Producto.class)))
                .thenReturn(Mono.error(new RuntimeException("Error al crear el producto")));

        StepVerifier.create(crearProductoUseCase.apply(createProductRequestDto))
                .expectErrorMessage("Error al crear el producto")
                .verify();
    }

    @Test
    @DisplayName("Usecase -> Inventario paginado")
    public void InventarioPaginado() {

        Producto producto = new Producto();
        producto.setId("1");
        producto.setPrecio(100);
        producto.setExistencia(100);
        producto.setNombre("test");

        when(repository.findAll())
                .thenReturn(Flux.just(producto));

        StepVerifier.create(inventarioPaginadoUseCase.apply(1,10))
                .expectNextMatches(prod -> prod.getNombre().equals("test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Usecase -> Inventario paginado error")
    public void InventarioPaginadoError() {

        when(repository.findAll())
                .thenReturn(Flux.error(new DatosNoEncontrados("No hay productos")));


        StepVerifier.create(inventarioPaginadoUseCase.apply(1,10))
                .expectErrorMessage("No hay productos")
                .verify();
    }

//    @Test
//    @DisplayName("Usecase -> Registrar inventario")
//    public void RegistrarInventario() {
//
//        Producto producto = new Producto();
//        producto.setId("1");
//        producto.setPrecio(100);
//        producto.setExistencia(100);
//        producto.setNombre("test");
//
//        Transaccion transacction = new Transaccion();
//        transacction.setIdProducto("1");
//        transacction.setId("1");
//        transacction.setTipo("1");
//        transacction.setCantidad(0);
//        transacction.setFecha(new Date());
//        transacction.setDescuento(0);
//
//        producto.setTransacciones(List.of(transacction));
//
//        when(repository.findById("1"))
//                .thenReturn(Mono.just(producto));
//
//        when(repository.save(any(Producto.class)))
//                .thenReturn(Mono.just(producto));
//
//
//        ActualizarInventarioRequestDto data = new ActualizarInventarioRequestDto();
//        data.setIdProducto("1");
//        data.setPrecio(100);
//        data.setExistencia(100);
//
//        StepVerifier.create(registrarInventarioUseCase.apply(data))
//                .expectNextMatches(tran -> tran.getIdProducto().equals("1"))
//                .verifyComplete();
//    }

}
