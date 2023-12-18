package com.epa.inventario.utils;

import com.epa.inventario.models.dto.TransaccionDto;
import com.epa.inventario.models.mongo.Transaccion;
import org.springframework.beans.BeanUtils;

public class TransaccionUtil {
    public static TransaccionDto entityToDto(Transaccion entidad) {
        TransaccionDto dto = new TransaccionDto();
        BeanUtils.copyProperties(entidad, dto);
        return dto;
    }

    public static Transaccion dtoToEntity(TransaccionDto dto) {
        Transaccion entidad = new Transaccion();
        BeanUtils.copyProperties(dto, entidad);
        return entidad;
    }
}
