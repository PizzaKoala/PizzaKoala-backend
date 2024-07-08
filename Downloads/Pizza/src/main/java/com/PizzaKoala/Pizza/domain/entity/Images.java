package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.domain.entity.Post;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    private String url;
    private String format;
    private String size;
    private String imagePhysicalName;
    private String imageLogicalName;
    @CreatedDate
    private LocalDateTime createdAt;
}
