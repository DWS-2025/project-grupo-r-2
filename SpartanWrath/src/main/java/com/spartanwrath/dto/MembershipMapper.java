package com.spartanwrath.dto;

import com.spartanwrath.model.Membership;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface MembershipMapper {
    MembershipDTO toDTO(Membership membership);
    List<MembershipDTO> toDTOs(List<Membership> memberships);
    Membership toDomain(MembershipDTO membershipDTO);

}