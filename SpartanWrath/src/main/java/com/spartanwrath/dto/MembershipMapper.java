package com.spartanwrath.dto;

import com.spartanwrath.model.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CombatClassMapper.class})
public interface MembershipMapper {
    MembershipDTO toDTO(Membership membership);
    List<MembershipDTO> toDTOs(List<Membership> memberships);
    Membership toDomain(MembershipDTO membershipDTO);

}