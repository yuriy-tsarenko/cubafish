package com.cubafish.utils;

import com.cubafish.dto.BookingItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingListResponseBody {
    private Long id;
    private String dateOfBooking;
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
    private String bookingComments;
    private List<BookingItemDto> bookingItems;
}
