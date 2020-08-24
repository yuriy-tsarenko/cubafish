package com.cubafish.utils;

import com.cubafish.dto.BookingItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookingBody {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String contact;
    private String userConfirmation;
    private String totalPrice;
    private String totalAmount;
    private String paymentType;
    private String deliveryType;
    private String region;
    private String city;
    private String address;
    private String bookingComments;
    private List<BookingItemDto> bookingItems;
}
