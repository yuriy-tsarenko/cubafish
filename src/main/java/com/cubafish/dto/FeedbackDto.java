package com.cubafish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private String userName;
    private String userLastName;
    private String dateOfComment;
    private Boolean recommendation;
    private Integer mark;
    private String comment;
}
