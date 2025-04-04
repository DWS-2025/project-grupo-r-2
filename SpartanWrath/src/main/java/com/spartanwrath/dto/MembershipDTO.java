package com.spartanwrath.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public record MembershipDTO(String nombre,
                            double precio,
                            String descripcion,
                            Long id,
                            CombatClassDTO combatClass) {}

