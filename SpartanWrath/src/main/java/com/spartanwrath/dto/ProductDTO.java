package com.spartanwrath.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String nombre;
    private double precio;
    private List<UserDTO> usuarios;

}
