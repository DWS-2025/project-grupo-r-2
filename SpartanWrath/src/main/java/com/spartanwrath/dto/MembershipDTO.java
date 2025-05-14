package com.spartanwrath.dto;


public record MembershipDTO(String nombre,
                            double precio,
                            String descripcion,
                            Long id,
                            CombatClassDTO combatClass) {}

