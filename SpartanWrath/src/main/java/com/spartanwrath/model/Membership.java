package com.spartanwrath.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "membership")
public class Membership {

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "combatclass_id")
    private CombatClass combatClass;

    public Membership() {

        }

    public Membership( String nombre, String descripcion, double precio, LocalDate fechaalta, LocalDate fechafin, boolean active, User user) {

        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fechaalta = fechaalta;
        this.fechafin = fechafin;
        this.active = active;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", user=" + user +
                '}';
    }
}
