package com.epa.inventario.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

public class Transaccion {
    @Id
    @org.springframework.data.mongodb.core.mapping.Field(targetType = FieldType.OBJECT_ID)
    private String id;
    private Date fecha;
    private int cantidad;
    private double precio;
    private String tipo;
}
