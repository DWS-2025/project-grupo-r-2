package com.spartanwrath.dto;

import com.spartanwrath.model.CombatClass;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CombatClassMapper {
    CombatClassDTO toDTO(CombatClass combatClass);

    List<CombatClassDTO> toDTOs(List<CombatClass> combatclasses);

    CombatClass toDomain(CombatClassDTO combatClassDTO);
}