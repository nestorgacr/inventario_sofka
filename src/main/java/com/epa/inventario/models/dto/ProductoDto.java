package com.epa.inventario.models.dto;

import java.util.List;
import java.util.Objects;

public class ProductoDto {
    private String id;
    private String nombre;
    private double precio;
    private int existencia;

    private double descuento;
    private int unidadesMinimasDescuento;
    private List<TransaccionDto> transacciones;

    public ProductoDto() {
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

    public List<TransaccionDto> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionDto> transacciones) {
        this.transacciones = transacciones;
    }


    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public int getUnidadesMinimasDescuento() {
        return unidadesMinimasDescuento;
    }

    public void setUnidadesMinimasDescuento(int unidadesMinimasDescuento) {
        this.unidadesMinimasDescuento = unidadesMinimasDescuento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoDto that)) return false;
        return Double.compare(precio, that.precio) == 0 && existencia == that.existencia && Double.compare(descuento, that.descuento) == 0 && unidadesMinimasDescuento == that.unidadesMinimasDescuento && Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(transacciones, that.transacciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, existencia, descuento, unidadesMinimasDescuento, transacciones);
    }
}
