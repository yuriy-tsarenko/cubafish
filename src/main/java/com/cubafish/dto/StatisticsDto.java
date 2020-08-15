package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    private Long id;
    private Date updateDate;
    private BigDecimal totalPrice;
    private Long totalAmount;
}
