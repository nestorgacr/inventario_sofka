package com.epa.inventario.routes;

import com.epa.inventario.handlers.ProductoHandler;
import com.epa.inventario.models.dto.CreateProductRequestDto;
import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.usecase.CrearProductoUseCase;
import com.epa.inventario.usecase.RegistrarInventarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductosRoutesTest {
    private WebTestClient webTestClient;

    @Mock
    private CrearProductoUseCase crearProductoUseCase;

    @Mock
    private RegistrarInventarioUseCase registrarInventarioUseCase;

    private ProductoHandler productoHandler;

    private ProductoRouter productoRouter;



    @BeforeEach
    void setUp(){
        productoHandler = new ProductoHandler(  crearProductoUseCase, registrarInventarioUseCase);

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
    void CrearClienteWithError() {
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
}
