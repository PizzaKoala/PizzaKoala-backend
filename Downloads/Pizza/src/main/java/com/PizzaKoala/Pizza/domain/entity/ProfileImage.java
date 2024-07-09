package com.PizzaKoala.Pizza.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    private Long memberId;

    private String url;
    private String format;
    private String size;
    private String imagePhysicalName;
    private String imageLogicalName;
    @CreatedDate
    private LocalDateTime createdAt;



}
