package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingListDto {
    private Long id;
    private Date dateOfBooking;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String contact;
    private String userConfirmation;
    private BigDecimal totalPrice;
    private Integer totalAmount;
    private String paymentType;
    private String deliveryType;
    private String region;
    private String city;
    private String address;
    private String bookingItems;
}
