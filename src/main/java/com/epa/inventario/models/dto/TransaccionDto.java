package com.epa.inventario.models.dto;


import java.util.Date;
import java.util.Objects;

public class TransaccionDto {
    private String id;
    private String idProducto;
    private Date fecha;
    private int cantidad;
    private double precio;

    private double descuento;
    private String tipo;

    public TransaccionDto() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
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

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
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
        if (!(o instanceof TransaccionDto that)) return false;
        return cantidad == that.cantidad && Double.compare(precio, that.precio) == 0 && Double.compare(descuento, that.descuento) == 0 && Objects.equals(id, that.id) && Objects.equals(idProducto, that.idProducto) && Objects.equals(fecha, that.fecha) && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProducto, fecha, cantidad, precio, descuento, tipo);
    }
}
