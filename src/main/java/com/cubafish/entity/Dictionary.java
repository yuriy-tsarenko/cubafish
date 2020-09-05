package com.cubafish.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dictionary")
@Data
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_word")
    private String originalWord;

    @Column(name = "uk_word")
    private String ukWord;

    @Column(name = "en_word")
    private String enWord;
}
