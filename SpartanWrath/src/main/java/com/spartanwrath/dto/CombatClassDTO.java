package com.spartanwrath.dto;


import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.List;



public record CombatClassDTO(@JsonView(CombatClassDTO.class)String name,
                             @JsonView(CombatClassDTO.class)Long id,
                             @JsonView(CombatClassDTO.class)String description,
                             @JsonView(CombatClassDTO.class)String turn) implements Serializable {}

