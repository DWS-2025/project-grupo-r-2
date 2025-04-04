package com.spartanwrath.dto;



import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;


import java.util.List;

public record ProductDTO(@JsonView({ProductDTO.class,UserDTO.class}) String nombre,
                         @JsonView({ProductDTO.class,UserDTO.class}) double precio,
                         @JsonView(ProductDTO.class) Long id,
                         @JsonView(ProductDTO.class) String descripcion,
                         @JsonView(ProductDTO.class) Integer cantidad,
                         @JsonView(ProductDTO.class) String category
                         ) {}

