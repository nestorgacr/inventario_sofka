package com.epa.inventario.routes;

import com.epa.inventario.drivenAdapters.bus.RabbitMqPublisher;
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
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductosRoutesTest {
    private WebTestClient webTestClient;

    @Mock
    private CrearProductoUseCase crearProductoUseCase;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;

    @Mock
    private RegistrarInventarioUseCase registrarInventarioUseCase;

    private InventarioPaginadoUseCase inventarioPaginadoUseCase;

    private ProductoHandler productoHandler;

    private ProductoRouter productoRouter;



    @BeforeEach
    void setUp(){
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

        LogDto log = new LogDto.Builder("1")
                .addTipo(TipoTransaccion.PRODUCTO_NUEVO)
                .addData(tran)
                .build();

        doNothing().when(rabbitMqPublisher).publishTransaccion(log);

        ErrorDto errorDto = new ErrorDto.Builder()
                .addTipo(TipoMensaje.ERROR)
                .addData("Error").build();

        doNothing().when(rabbitMqPublisher).publishError(errorDto);

        webTestClient.post()
                .uri("/Producto/Crear")
                .bodyValue(productRequestDto)
                .exchange()
                .expectStatus().is5xxServerError()  // Cambia según el código de error esperado
                .expectBody(String.class)
                .isEqualTo("Error al crear el producto");

    }


}
