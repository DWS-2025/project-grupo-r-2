package com.spartanwrath.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public record MembershipDTO(@JsonView({MembershipDTO.class,UserDTO.class})String nombre,
                            @JsonView({MembershipDTO.class,UserDTO.class})double precio,
                            @JsonView(MembershipDTO.class)String descripcion,
                            @JsonView(MembershipDTO.class)Long id,
                            @JsonView(MembershipDTO.class)List<UserDTO> users,
                            @JsonView(MembershipDTO.class)CombatClassDTO combatClass) {}

