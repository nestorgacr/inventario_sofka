package com.epa.inventario.models.dto;

import java.util.List;
public class ProductoDto {
    private String id;
    private String nombre;
    private double precio;
    private int existencia;
    private List<TransaccionDto> transacciones;
}
