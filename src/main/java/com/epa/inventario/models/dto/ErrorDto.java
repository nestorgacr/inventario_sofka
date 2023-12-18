package com.epa.inventario.models.dto;

import com.epa.inventario.models.enums.TipoMensaje;

import java.util.Date;

public class ErrorDto {
    private String id;
    private Date fecha;
    private String tipo;
    private Object data;

    private ErrorDto() {
    }

    public static class Builder {
        private ErrorDto log;

        public Builder() {
            log = new ErrorDto();
            // agrega la fecha
            log.fecha = new Date();
        }

        public Builder addTipo(TipoMensaje tipo) {
            log.tipo = tipo.toString();
            return this;
        }

        public Builder addData(Object data) {
            log.data = data;
            return this;
        }

        public ErrorDto build() {
            return log;
        }
    }
}
