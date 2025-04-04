package com.spartanwrath.dto;


import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.List;



public record CombatClassDTO(@JsonView({CombatClassDTO.class,MembershipDTO.class})String name,
                             @JsonView({CombatClassDTO.class,MembershipDTO.class})Long id,
                             @JsonView({CombatClassDTO.class,MembershipDTO.class})String description,
                             @JsonView({CombatClassDTO.class,MembershipDTO.class})String turn) implements Serializable {}

