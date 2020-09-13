package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productCategory;
    private String productSubCategory;
    private String productBrand;
    private String typeOfPurpose;
    private String description;
    private String specification;
    private Integer totalAmount;
    private BigDecimal productPrice;
    private BigDecimal oldProductPrice;
    private String productImageName;
    private String productImageRightName;
    private String productImageLeftName;
    private String productImageBackName;
}
