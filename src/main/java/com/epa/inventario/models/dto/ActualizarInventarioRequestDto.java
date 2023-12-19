package com.epa.inventario.models.dto;

import java.util.Objects;

public class ActualizarInventarioRequestDto {

    private String idProducto;
    private double precio;
    private int existencia;

    public ActualizarInventarioRequestDto() {
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActualizarInventarioRequestDto that)) return false;
        return Double.compare(precio, that.precio) == 0 && existencia == that.existencia && Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, precio, existencia);
    }
}
