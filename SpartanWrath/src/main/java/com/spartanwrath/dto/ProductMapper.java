package com.spartanwrath.dto;

import com.spartanwrath.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.spartanwrath.model.Product;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTOs(List<Product> products);
    Product toDomain(ProductDTO productDTO);

}
