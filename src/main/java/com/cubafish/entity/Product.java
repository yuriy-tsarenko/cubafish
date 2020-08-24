package com.cubafish.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_sub_category")
    private String productSubCategory;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "type_of_purpose")
    private String typeOfPurpose;

    @Column(name = "description")
    private String description;

    @Column(name = "specification")
    private String specification;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_image_name")
    private  String productImageName;

    @Column(name = "product_image_right_name")
    private String productImageRightName;

    @Column(name = "product_image_left_name")
    private String productImageLeftName;

    @Column(name = "product_image_back_name")
    private String productImageBackName;
}
