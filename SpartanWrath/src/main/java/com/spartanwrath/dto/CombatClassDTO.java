package com.spartanwrath.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombatClassDTO {
    private String name;
    private List<MembershipDTO> memberships;
}
