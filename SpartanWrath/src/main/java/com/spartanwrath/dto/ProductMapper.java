package com.spartanwrath.dto;


import org.mapstruct.Mapper;

import com.spartanwrath.model.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    List<ProductDTO> toDTOs(List<Product> products);
    Product toDomain(ProductDTO productDTO);

}
