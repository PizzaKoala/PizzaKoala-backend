package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Images extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    private Long memberId;

    private String url;
    private String format;
    private Long size;
//    private String imagePhysicalName;
    private String imageLogicalName;
    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    public static Images of(Post postId,Long memberId, String url, String format, Long size, String imageLogicalName) {

        return new ImagesBuilder()
                .postId(postId)
                .memberId(memberId)
                .url(url)
                .format(format)
                .size(size)
                .imageLogicalName(imageLogicalName)
                .build();
    }
}
