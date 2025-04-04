
package com.spartanwrath.dto;
import com.spartanwrath.model.Product;
import com.spartanwrath.dto.MembershipDTO;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public record UserDTO(Long id,
                      String username,
                      MembershipDTO membership,
                      List<ProductDTO> products,
                      String name,
                      String email,
                      String address,
                      String phone,
                      String dni) {}

