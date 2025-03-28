package com.spartanwrath.dto;

import java.util.List;

public class ProductDTO {
    private String nombre;
    private double precio;
    private List<UserDTO> usuarios;
    // Getter para 'nombre'
    public String getNombre() {
        return nombre;
    }

    // Setter para 'nombre'
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para 'precio'
    public double getPrecio() {
        return precio;
    }

    // Setter para 'precio'
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Getter para 'usuarios'
    public List<UserDTO> getUsuarios() {
        return usuarios;
    }

    // Setter para 'usuarios'
    public void setUsuarios(List<UserDTO> usuarios) {
        this.usuarios = usuarios;
    }
}
