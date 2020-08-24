package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemDto {
    private Long id;
    private String creationTime;
    private Integer key;
    private String description;
    private String productImageName;
    private BigDecimal productPrice;
    private Integer totalAmount;
    private Integer itemAmount;
}
