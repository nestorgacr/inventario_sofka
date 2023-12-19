package com.epa.inventario.models.dto;

import java.util.Objects;

public class VentaRequestDto {

    private String idProducto;
    private int cantidad;

    public VentaRequestDto() {
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VentaRequestDto that)) return false;
        return cantidad == that.cantidad && Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, cantidad);
    }
}
