package com.spartanwrath.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDTO {
    private String nombre;
    private double precio;
    private List<UserDTO> users;
    private CombatClassDTO combatClass;
}
