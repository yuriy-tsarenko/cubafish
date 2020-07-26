package com.cubafish.mapper;

import com.cubafish.dto.ProductDto;
import com.cubafish.entity.Product;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    List<ProductDto> mapEntitiesToDtos(List<Product> products);

    Product mapDtoToEntity(ProductDto productDto);

    ProductDto mapEntityToDto(Product product);
}
