package com.spartanwrath.dto;



import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;


import java.util.List;

public record ProductDTO(String nombre,
                         double precio,
                         Long id,
                         String descripcion,
                         Integer cantidad,
                         String category) {}

