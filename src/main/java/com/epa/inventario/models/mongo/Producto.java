package com.epa.inventario.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document("Producto")
public class Producto {
    @Id
    private String id;
    private String nombre;
    private double precio;
    private int existencia;

    private double descuento;
    private int unidadesMinimasDescuento;
    private List<Transaccion> transacciones;

    public Producto() {
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

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
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
        if (!(o instanceof Producto producto)) return false;
        return Double.compare(precio, producto.precio) == 0 && existencia == producto.existencia && Double.compare(descuento, producto.descuento) == 0 && unidadesMinimasDescuento == producto.unidadesMinimasDescuento && Objects.equals(id, producto.id) && Objects.equals(nombre, producto.nombre) && Objects.equals(transacciones, producto.transacciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, precio, existencia, descuento, unidadesMinimasDescuento, transacciones);
    }
}
