package com.spartanwrath.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membership")
public class Membership {

    public interface Basico {}
    public interface CombatClasses {}
    public interface Users {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private double precio;

    @Column(name = "fechaalta")
    private LocalDate fechaalta;

    @Column(name = "fechafin")
    private LocalDate fechafin;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "membership", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "combatclass_id")
    private CombatClass combatClass;

    public Membership() {
    }

    public Membership(String nombre, String descripcion, double precio, LocalDate fechaalta, LocalDate fechafin, boolean active) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fechaalta = fechaalta;
        this.fechafin = fechafin;
        this.active = active;
    }

    // Getters y Setters

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaalta() {
        return fechaalta;
    }

    public void setFechaalta(LocalDate fechaalta) {
        this.fechaalta = fechaalta;
    }

    public LocalDate getFechafin() {
        return fechafin;
    }

    public void setFechafin(LocalDate fechafin) {
        this.fechafin = fechafin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<User> getUser() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public CombatClass getCombatClass() {
        return combatClass;
    }

    public void setCombatClass(CombatClass combatClass) {
        this.combatClass = combatClass;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", fechaalta=" + fechaalta +
                ", fechafin=" + fechafin +
                ", active=" + active +
                '}';
    }
}