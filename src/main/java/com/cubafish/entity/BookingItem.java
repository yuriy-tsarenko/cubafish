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
@Table(name = "booking_item")
@Data
public class BookingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_time")
    private String creationTime;

    @Column(name = "product_key")
    private Integer key;

    @Column(name = "description")
    private String description;

    @Column(name = "product_image_name")
    private String productImageName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "item_amount")
    private Integer itemAmount;
}
