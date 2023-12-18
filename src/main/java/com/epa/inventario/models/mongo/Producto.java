package com.epa.inventario.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Producto")
public class Producto {
    @Id
    private String id;
    private String nombre;
    private double precio;
    private int existencia;
    private List<Transaccion> transacciones;
}
