package com.epa.inventario.routes;


import com.epa.inventario.exception.DatosNoEncontrados;
import com.epa.inventario.handlers.ProductoHandler;
import com.epa.inventario.handlers.VentaHandler;
import com.epa.inventario.models.dto.ActualizarInventarioRequestDto;
import com.epa.inventario.models.dto.ProductoPaginadoDto;
import com.epa.inventario.models.dto.TransaccionDto;
import com.epa.inventario.models.dto.VentaRequestDto;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import com.epa.inventario.usecase.RegistrarVentaAlPorMayorUseCase;
import com.epa.inventario.usecase.RegistrarVentaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VentaRoutesTest {
    private WebTestClient webTestClient;

    @Mock
    private RegistrarVentaUseCase registrarVentaUseCase;

    @Mock
    private RegistrarVentaAlPorMayorUseCase registrarVentaAlPorMayorUseCase;

    private VentaHandler ventaHandler;

    private VentaRouter ventaRouter;



    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        ventaHandler = new VentaHandler(
                registrarVentaUseCase,
                registrarVentaAlPorMayorUseCase);

        ventaRouter = new VentaRouter(ventaHandler);

        webTestClient = WebTestClient.bindToRouterFunction(ventaRouter.routerFunctionVentas())
                .build();
    }

    @Test
    @DisplayName("Routes -> Registrar Venta")
    void RegistrarVenta() {

        VentaRequestDto inv = new VentaRequestDto();
        inv.setCantidad(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarVentaUseCase.apply(any(VentaRequestDto.class)))
                .thenReturn(Mono.just(transaccion));

        webTestClient.post()
                .uri("/Venta/AlPorMenor")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransaccionDto.class)
                .isEqualTo(List.of(transaccion));

    }

    @Test
    @DisplayName("Routes -> Registrar venta error")
    void RegistrarVentaError() {

        VentaRequestDto inv = new VentaRequestDto();
        inv.setCantidad(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarVentaUseCase.apply(any(VentaRequestDto.class)))
                .thenReturn(Mono.error(new DatosNoEncontrados("Producto no encontrado")));

        webTestClient.post()
                .uri("/Venta/AlPorMenor")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Producto no encontrado");



    }

    @Test
    @DisplayName("Routes -> Registrar Venta al por mayor")
    void RegistrarVentaAlPorMayor() {

        VentaRequestDto inv = new VentaRequestDto();
        inv.setCantidad(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarVentaAlPorMayorUseCase.apply(any(VentaRequestDto.class)))
                .thenReturn(Mono.just(transaccion));

        webTestClient.post()
                .uri("/Venta/AlPorMayor")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransaccionDto.class)
                .isEqualTo(List.of(transaccion));

    }

    @Test
    @DisplayName("Routes -> Registrar venta al por mayor error")
    void RegistrarVentaAlPorMayorError() {

        VentaRequestDto inv = new VentaRequestDto();
        inv.setCantidad(100);
        inv.setIdProducto("1");

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setFecha(new Date());
        transaccion.setTipo(TipoTransaccion.INGRESO.toString());
        transaccion.setPrecio(100);
        transaccion.setCantidad(1000);

        when(registrarVentaAlPorMayorUseCase.apply(any(VentaRequestDto.class)))
                .thenReturn(Mono.error(new DatosNoEncontrados("Producto no encontrado")));

        webTestClient.post()
                .uri("/Venta/AlPorMayor")
                .bodyValue(List.of(transaccion))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("Producto no encontrado");



    }
}
