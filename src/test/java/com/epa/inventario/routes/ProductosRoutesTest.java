package com.epa.inventario.routes;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.handlers.ProductoHandler;
import com.epa.inventario.models.dto.*;
import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.InventarioPaginadoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import com.epa.inventario.utils.TransaccionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductosRoutesTest {
    private WebTestClient webTestClient;

    @Mock
    private CrearProductoUseCase crearProductoUseCase;

    @Mock
    private RegistrarInventarioUseCase registrarInventarioUseCase;

    @Mock
    private InventarioPaginadoUseCase inventarioPaginadoUseCase;

    private ProductoHandler productoHandler;

    private ProductoRouter productoRouter;



    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        productoHandler = new ProductoHandler(
                crearProductoUseCase,
                registrarInventarioUseCase,
                inventarioPaginadoUseCase);

        productoRouter = new ProductoRouter(productoHandler);

        webTestClient = WebTestClient.bindToRouterFunction(productoRouter.routerFunctionProductos())
                .build();
    }

    @Test
    @DisplayName("Routes -> Crear Producto")
    void CrearCliente() {
        CreateProductRequestDto productRequestDto = new CreateProductRequestDto();
        productRequestDto.setNombre("test");
        productRequestDto.setExistencia(1000);
        productRequestDto.setPrecio(100);

        ProductoDto producto = new ProductoDto();
        producto.setNombre("test");
        producto.setExistencia(1000);
        producto.setPrecio(100);
        producto.setId("");

        when(crearProductoUseCase.apply(productRequestDto))
                .thenReturn(Mono.just(producto));

        webTestClient.post()
                .uri("/Producto/Crear")
                .bodyValue(productRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductoDto.class)
                .isEqualTo(producto);

    }

    @Test
    @DisplayName("Routes -> Crear Producto con error")
    void CrearClienteConError() {
        CreateProductRequestDto productRequestDto = new CreateProductRequestDto();
        productRequestDto.setNombre("test");
        productRequestDto.setExistencia(1000);
        productRequestDto.setPrecio(100);

        when(crearProductoUseCase.apply(productRequestDto))
                .thenReturn(Mono.error(new RuntimeException("Error al crear el producto")));

        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.PRODUCTO_NUEVO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        TransaccionDto tran = TransaccionUtil.entityToDto(transaccion);
        tran.setIdProducto("1");


        webTestClient.post()
                .uri("/Producto/Crear")
                .bodyValue(productRequestDto)
                .exchange()
                .expectStatus().is5xxServerError()  // Cambia según el código de error esperado
                .expectBody(String.class)
                .isEqualTo("Error al crear el producto");

    }

    @Test
    @DisplayName("Routes -> Registrar Inventario")
    void RegistrarInventario() {

        ActualizarInventarioRequestDto inv = new ActualizarInventarioRequestDto();
        inv.setExistencia(100);
        inv.setPrecio(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarInventarioUseCase.apply(any(ActualizarInventarioRequestDto.class)))
                .thenReturn(Mono.just(transaccion));

        webTestClient.post()
                .uri("/Producto/RegistrarInventario")
                .bodyValue(inv)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransaccionDto.class)
                .isEqualTo(transaccion);

    }

    @Test
    @DisplayName("Routes -> Registrar Inventario error")
    void RegistrarInventarioError() {

        ActualizarInventarioRequestDto inv = new ActualizarInventarioRequestDto();
        inv.setExistencia(100);
        inv.setPrecio(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarInventarioUseCase.apply(any(ActualizarInventarioRequestDto.class)))
                .thenReturn(Mono.error(new DatosNoEncontrados("Producto no encontrado")));

        webTestClient.post()
                .uri("/Producto/RegistrarInventario")
                .bodyValue(transaccion)
                .exchange()
                .expectStatus().isBadRequest()  // Cambia según el código de error esperado
                .expectBody(String.class)
                .isEqualTo("Producto no encontrado");

    }


    @Test
    @DisplayName("Routes -> Registrar Inventario masivo")
    void RegistrarInventarioMasivo() {

        ActualizarInventarioRequestDto inv = new ActualizarInventarioRequestDto();
        inv.setExistencia(100);
        inv.setPrecio(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarInventarioUseCase.apply(any(ActualizarInventarioRequestDto.class)))
                .thenReturn(Mono.just(transaccion));

        webTestClient.post()
                .uri("/Producto/RegistrarInventarioMasivo")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransaccionDto.class)
                .isEqualTo(List.of(transaccion));

    }

    @Test
    @DisplayName("Routes -> Registrar Inventario masivo error")
    void RegistrarInventarioMasivoError() {

        ActualizarInventarioRequestDto inv = new ActualizarInventarioRequestDto();
        inv.setExistencia(100);
        inv.setPrecio(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarInventarioUseCase.apply(any(ActualizarInventarioRequestDto.class)))
                .thenReturn(Mono.error(new DatosNoEncontrados("Producto no encontrado")));

        webTestClient.post()
                .uri("/Producto/RegistrarInventarioMasivo")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isBadRequest()  // Cambia según el código de error esperado
                .expectBody(String.class)
                .isEqualTo("Producto no encontrado");

    }

//    @Test
//    @DisplayName("Routes -> Inventario paginado")
//    void InventarioPaginado() {
//
//        ProductoPaginadoDto inventario = new ProductoPaginadoDto();
//        inventario.setExistencia(100);
//        inventario.setNombre("test");
//        inventario.setPrecio(100);
//        inventario.setId("1");
//
//        when(inventarioPaginadoUseCase.apply(1,10))
//                .thenReturn(Flux.just(inventario));
//
//        webTestClient.post()
//                .uri("/Producto/InventarioPaginado/10/1")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(ProductoPaginadoDto.class)
//                .isEqualTo(List.of(inventario));
//
//    }



}
