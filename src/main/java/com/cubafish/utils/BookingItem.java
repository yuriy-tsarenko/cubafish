package com.cubafish.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingItem {
    private Integer id;
    private Integer key;
    private String description;
    private String productImageName;
    private String productPrice;
    private String totalAmount;
    private String itemAmount;
}
