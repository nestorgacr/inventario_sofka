package com.epa.inventario.utils;

import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.models.dto.ProductoPaginadoDto;
import com.epa.inventario.models.mongo.Producto;
import org.springframework.beans.BeanUtils;

public class ProductoPaginadoUtil {
    public static ProductoPaginadoDto entityToDto(Producto entidad) {
        ProductoPaginadoDto dto = new ProductoPaginadoDto();
        BeanUtils.copyProperties(entidad, dto);
        return dto;
    }

    public static Producto dtoToEntity(ProductoPaginadoDto dto) {
        Producto entidad = new Producto();
        BeanUtils.copyProperties(dto, entidad);
        return entidad;
    }
}
