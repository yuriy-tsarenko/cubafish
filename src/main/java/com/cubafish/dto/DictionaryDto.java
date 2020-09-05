package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDto {
    private Long id;
    private String originalWord;
    private String ukWord;
    private String enWord;
}
