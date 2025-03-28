package com.spartanwrath.dto;


import com.spartanwrath.dto.UserDTO;
import com.spartanwrath.model.User;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    List<UserDTO> toDTOs(List<User> users);
    User toDomain(UserDTO userDTO);
}

