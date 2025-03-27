package com.spartanwrath.dto;

import java.time.LocalDate;

public record MembershipDTO(Long id, String name, String descripcion, Double precio, LocalDate fechaalta, LocalDate fechafin, boolean active, List<UserBasicDTO> users, CombatClassBasicDTO combatClass) {
}
