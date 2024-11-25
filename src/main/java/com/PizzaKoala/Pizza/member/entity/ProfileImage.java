package com.PizzaKoala.Pizza.member.entity;

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
    private Long size;
    private String imageLogicalName;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    public static ProfileImage of(Long memberId, String url, String format, Long size, String imageLogicalName) {

        return new ProfileImageBuilder()
                .memberId(memberId)
                .url(url)
                .format(format)
                .size(size)
                .imageLogicalName(imageLogicalName)
                .build();
    }


}
