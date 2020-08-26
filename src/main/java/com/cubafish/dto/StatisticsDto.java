package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDto {
    private Long id;
    private String updateDate;
    private String reportingPeriod;
    private BigDecimal totalPrice;
    private Long totalAmount;
    private Long bookingAmount;
    private Long canceledBookingAmount;
    private BigDecimal canceledBookingPrice;
}
