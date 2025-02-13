package com.spartanwrath.model;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nombre")

    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "precio")
    private double precio;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "category")
    private String category;
    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<User> usuarios = new ArrayList<>();

    public Product() {

    }

    public Product(String nombre, String descripcion, String imagen, double precio, Integer cantidad, String category) {

        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        if (imagen == null || imagen.isEmpty()){
            this.imagen = "../../images/DefaultProduct.jpg";
        } else {
            this.imagen = imagen;
        }
        this.precio = precio;
        this.cantidad = cantidad;
        this.category = category;
        this.usuarios = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
            this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }



    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", category='" + category + '\'' +
                ", usuarios=" + usuarios +
                '}';
    }
}
