package com.epa.inventario.models.dto;

import com.epa.inventario.models.enums.TipoTransaccion;

import java.util.Date;

public class Log {
    private String id;
    private Date fecha;
    private String idProducto;
    private String tipo;
    private Object data;

    private Log() {
    }

    public static class Builder {
        private Log log;

        public Builder(String idProducto) {
            log = new Log();
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

        public Log build() {
            if (log.idProducto == null) {
                throw new IllegalStateException("El ID del producto es obligatorio");
            }
            return log;
        }
    }
}
