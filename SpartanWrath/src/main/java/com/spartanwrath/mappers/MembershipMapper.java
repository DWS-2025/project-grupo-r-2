package com.spartanwrath.mappers;

import com.spartanwrath.dto.MembershipDTO;
import com.spartanwrath.model.Membership;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CombatClassMapper.class})
public interface MembershipMapper {
    MembershipDTO toDTO(Membership membership);
    List<MembershipDTO> toDTOs(List<Membership> memberships);
    Membership toDomain(MembershipDTO membershipDTO);

}