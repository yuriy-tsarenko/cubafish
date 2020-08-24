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
@Table(name = "booking_list")
@Data
public class BookingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_booking")
    private String dateOfBooking;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "user_confirmation")
    private String userConfirmation;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "booking_comments")
    private String bookingComments;

    @Column(name = "booking_items")
    private String bookingItems;
}
