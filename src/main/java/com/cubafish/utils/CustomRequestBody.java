package com.cubafish.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomRequestBody {
    private String key;
    private String communicationKey;
}
