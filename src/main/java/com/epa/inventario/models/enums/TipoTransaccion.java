package com.epa.inventario.models.enums;

public enum TipoTransaccion {
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
        }

        return tipo;
    }
}
