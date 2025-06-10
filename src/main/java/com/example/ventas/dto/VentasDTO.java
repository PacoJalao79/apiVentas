package com.example.ventas.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VentasDTO {

    private Integer idVenta;
    private Integer id_cliente;
    private Integer id_vendedor;
    private LocalDateTime feha_venta;
    
}
