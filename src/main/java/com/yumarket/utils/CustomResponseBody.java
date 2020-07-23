package com.yumarket.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponseBody {
    private Long id;
    private String key;
    private String status;
    private String communicationData;
}
