package com.PizzaKoala.Pizza.global.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "search")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    @CreatedDate
    private LocalDateTime createdAt;

    //modified_date& modified_id?
}
