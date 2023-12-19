package com.epa.inventario.models.dto;

import java.util.Objects;

public class ProductoPaginadoDto {
    private String id;
    private String nombre;
    private double precio;
    private int existencia;

    public ProductoPaginadoDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(o instanceof ProductoPaginadoDto that)) return false;
        return Double.compare(precio, that.precio) == 0 && existencia == that.existencia && Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, existencia);
    }
}
