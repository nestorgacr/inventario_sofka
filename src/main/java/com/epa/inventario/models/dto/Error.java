package com.epa.inventario.models.dto;

import com.epa.inventario.models.enums.TipoMensaje;
import com.epa.inventario.models.enums.TipoTransaccion;

import java.util.Date;

public class Error {
    private String id;
    private Date fecha;
    private String tipo;
    private Object data;

    private Error() {
    }

    public static class Builder {
        private Error log;

        public Builder() {
            log = new Error();
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

        public Error build() {
            return log;
        }
    }
}
