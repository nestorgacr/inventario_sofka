package com.epa.inventario.models.enums;

public enum TipoMensaje {
    ERROR,
    INFO;

    @Override
    public String toString()
    {
        String tipo = "NA";

        switch (this)
        {
            case ERROR: tipo = "ERROR"; break;
            case INFO: tipo = "INFO"; break;
        }

        return tipo;
    }
}
