
package com.spartanwrath.dto;
import com.spartanwrath.model.Product;
import com.spartanwrath.dto.MembershipDTO;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public record UserDTO(@JsonView(UserDTO.class) Long id,
                      @JsonView(UserDTO.class) String username,
                      @JsonView(UserDTO.class) MembershipDTO membership,
                      @JsonView(UserDTO.class) List<ProductDTO> products,
                      @JsonView(UserDTO.class) String name,
                      @JsonView(UserDTO.class) String email,
                      @JsonView(UserDTO.class) String address,
                      @JsonView(UserDTO.class) String phone,
                      @JsonView(UserDTO.class) String dni
                        ) {}

