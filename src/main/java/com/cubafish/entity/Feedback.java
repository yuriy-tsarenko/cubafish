package com.cubafish.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "feed_back")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_last_name")
    private String userLastName;

    @Column(name = "date_of_comment")
    private String dateOfComment;

    @Column(name = "recommendation")
    private Boolean recommendation;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "comment")
    private String comment;
}
