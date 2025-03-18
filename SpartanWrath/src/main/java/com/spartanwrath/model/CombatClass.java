package com.spartanwrath.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="combatclasses")
public class CombatClass {

    public interface Basico {}
    public interface Memberships {}

    @JsonView(Basico.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(Basico.class)
    @Column(name = "name")
    private String name;

    @JsonView(Basico.class)
    @Column(name = "description")
    private String description;

    @JsonView(Basico.class)
    @Column(name = "turn")
    private String turn;

    @JsonView(Memberships.class)
    @OneToMany(mappedBy = "combatClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Membership> memberships = new ArrayList<>();

    public CombatClass() {
    }

    public CombatClass(String name, String description, String turn) {
        this.name = name;
        this.description = description;
        this.turn = turn;
    }

    // Getters y Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    @Override
    public String toString() {
        return "CombatClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", turn='" + turn + '\'' +
                '}';
    }
}