package com.epa.inventario.exception;

public class DatosNoEncontrados extends RuntimeException {
    public DatosNoEncontrados(String mensaje) {
        super(mensaje);
    }
}
