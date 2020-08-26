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
@Table(name = "statistics")
@Data
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_date")
    private String updateDate;

    @Column(name = "reporting_period")
    private String reportingPeriod;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Column(name = "booking_amount")
    private Long bookingAmount;

    @Column(name = "canceled_booking_amount")
    private Long canceledBookingAmount;

    @Column(name = "canceled_booking_price")
    private BigDecimal canceledBookingPrice;

}
