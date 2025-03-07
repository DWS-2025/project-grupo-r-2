package com.spartanwrath.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    @OneToMany(mappedBy = "combatClass", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Membership> memberships = new ArrayList<>();

    public CombatClass() {
    }

    public CombatClass(String name, String description, String turn) {
        super();
        this.name = name;
        this.description = description;
        this.turn = turn;
        this.memberships = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTurn() {
        return turn;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTurn(String turn) {
        this.turn = turn;
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
