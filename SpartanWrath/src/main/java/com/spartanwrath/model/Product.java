package com.spartanwrath.model;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    public interface Basico {}
    public interface Users {}

    @JsonView(Basico.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonView(Basico.class)
    @Column(name = "nombre")
    private String nombre;
    @JsonView(Basico.class)
    @Column(name = "descripcion")
    private String descripcion;
    @JsonView(Basico.class)
    @Column(name = "original_image_name")
    private String originalImageName;
    @JsonIgnore
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;
    @JsonIgnore
    @Transient
    private String base64Image;
    @JsonView(Basico.class)
    @Column(name = "precio")
    private double precio;
    @JsonView(Basico.class)
    @Column(name = "cantidad")
    private Integer cantidad;
    @JsonView(Basico.class)
    @Column(name = "category")
    private String category;
    @JsonView(Users.class)
    @ManyToMany(mappedBy = "products",cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<User> usuarios = new ArrayList<>();


    public Product() {

    }

    public Product(String nombre, String descripcion, byte[] imagen, double precio, Integer cantidad, String category) {

        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
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

    public String getOriginalImageName() {return originalImageName;}

    public void setOriginalImageName(String originalImageName) {this.originalImageName = originalImageName;}

    public String getBase64Image() {return base64Image;}

    public void setBase64Image(String base64Image) {this.base64Image = base64Image; }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
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

    public String getImagePath(){
        return "src/main/resources/static/images/"+getOriginalImageName();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen=" + Arrays.toString(imagen) +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", category='" + category + '\'' +
                ", usuarios=" + usuarios +
                '}';
    }
}
