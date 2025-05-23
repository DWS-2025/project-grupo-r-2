package com.spartanwrath.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;


@Entity
@Table(name = "Users")
public class User {

    public interface Basico {}
    public interface Products {}
    public interface Memberships {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private long phone;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "dd-MM-yyyy")

    private LocalDate birthday;

    @Column(name = "dni")
    private String dni;

    @Column(name = "payment")
    private String payment;


    @ManyToMany
    @JoinTable(
            name = "user_product",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @ElementCollection(fetch = FetchType.EAGER)
    //Quitar tabla por ocultacion de acceso y JSONview
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    public User() {
    }

    public User( String name, String username, String email, String address, long phone, String password, LocalDate birthday, String dni, String payment) {
        super();
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.birthday = birthday;
        this.dni = dni;
        this.payment = payment;
        this.products = new ArrayList<>();
    }

    public User(String name, String username, String email, String address, long phone, String password, String dni, String payment) {
        super();
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.dni = dni;
        this.payment = payment;
        this.products = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public static boolean valid(User user) {
        if (user == null) return false;
        if (user.name == null || user.name.length() > 60) return false;
        if (user.dni != null && user.dni.length() > 9) return false;
        // if (user.address != null && user.address.length() > 40) return false; // opcional
        if (user.phone > 999999999L || user.phone < 0) return false;
        if (user.username != null && user.username.length() > 20) return false;
        if (user.password == null) return false;
        if (!user.password.startsWith("$2a$")) {
            if (user.password.length() > 20) return false;
        }
        if (user.birthday != null && user.birthday.isAfter(LocalDate.now())) return false;
        if (user.payment != null && user.payment.length() > 15) return false;
        if (user.email == null || user.email.length() > 30) return false;

        return true;
    }

}
