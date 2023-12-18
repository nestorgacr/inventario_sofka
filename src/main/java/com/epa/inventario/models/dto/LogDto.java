package com.epa.inventario.models.dto;

import com.epa.inventario.models.enums.TipoTransaccion;

import java.util.Date;

public class LogDto {
    private String id;
    private Date fecha;
    private String idProducto;
    private String tipo;
    private Object data;

    private LogDto() {
    }

    public static class Builder {
        private LogDto log;

        public Builder(String idProducto) {
            log = new LogDto();
            // agrega la fecha
            log.fecha = new Date();
            // agrega el id del producto
            log.idProducto = idProducto;
        }

        public Builder addTipo(TipoTransaccion tipo) {
            log.tipo = tipo.toString();
            return this;
        }

        public Builder addData(Object data) {
            log.data = data;
            return this;
        }

        public LogDto build() {
            if (log.idProducto == null) {
                throw new IllegalStateException("El ID del producto es obligatorio");
            }
            return log;
        }
    }
}
