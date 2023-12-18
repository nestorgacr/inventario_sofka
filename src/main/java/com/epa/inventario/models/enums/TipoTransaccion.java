package com.epa.inventario.models.enums;

public enum TipoTransaccion {

    PRODUCTO_NUEVO,
    INGRESO,
    VENTA;

    @Override
    public String toString()
    {
        String tipo = "NA";

        switch (this)
        {
            case INGRESO: tipo = "INGRESO"; break;
            case VENTA: tipo = "VENTA"; break;
            case PRODUCTO_NUEVO: tipo = "PRODUCTO_NUEVO"; break;
        }

        return tipo;
    }
}
