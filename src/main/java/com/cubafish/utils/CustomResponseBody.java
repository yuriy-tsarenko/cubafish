package com.cubafish.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponseBody {
    private String key;
    private String status;
    private String communicationData;
}
