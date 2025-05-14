package com.spartanwrath.dto;


public record ProductDTO(String nombre,
                         double precio,
                         Long id,
                         String descripcion,
                         Integer cantidad,
                         String category) {}

