package com.epa.inventario.usecase;

import com.epa.inventario.drivenAdapters.repositorios.IProductoRepository;
import com.epa.inventario.models.dto.CreateProductRequestDto;
import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.models.enums.TipoTransaccion;
import com.epa.inventario.models.mongo.Producto;
import com.epa.inventario.models.mongo.Transaccion;
import com.epa.inventario.utils.ProductoUtil;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class CrearProductoUseCase implements Function<CreateProductRequestDto, Mono<ProductoDto>> {

    private final IProductoRepository repositorio;

    public CrearProductoUseCase(IProductoRepository repositorio) {
        this.repositorio = repositorio;
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

        return repositorio.save(producto).map(ProductoUtil::entityToDto);
    }
}
