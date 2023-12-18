package com.epa.inventario.utils;

import com.epa.inventario.models.dto.ProductoDto;
import com.epa.inventario.models.mongo.Producto;
import org.springframework.beans.BeanUtils;

public class ProductoUtil {
    public static ProductoDto entityToDto(Producto entidad) {
        ProductoDto dto = new ProductoDto();
        BeanUtils.copyProperties(entidad, dto);
        return dto;
    }

    public static Producto dtoToEntity(ProductoDto dto) {
        Producto entidad = new Producto();
        BeanUtils.copyProperties(dto, entidad);
        return entidad;
    }
}
