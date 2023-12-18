package com.epa.inventario.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.Objects;

public class Transaccion {
    @Id
    @org.springframework.data.mongodb.core.mapping.Field(targetType = FieldType.OBJECT_ID)
    private String id;
    private Date fecha;
    private int cantidad;
    private double precio;
    private String tipo;

    public Transaccion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaccion that)) return false;
        return cantidad == that.cantidad && Double.compare(precio, that.precio) == 0 && Objects.equals(id, that.id) && Objects.equals(fecha, that.fecha) && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, cantidad, precio, tipo);
    }
}
