package com.yumarket.mapper;

import com.yumarket.dto.ProductDto;
import com.yumarket.entity.Product;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<ProductDto> mapEntitiesToDtos(List<Product> products);

    Product mapDtoToEntity(ProductDto productDto);

    ProductDto mapEntityToDto(Product product);
}
