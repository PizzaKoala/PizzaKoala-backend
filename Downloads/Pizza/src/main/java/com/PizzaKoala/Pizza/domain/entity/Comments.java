package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Table(name = "comments", indexes = {
        @Index(name = "post_id_index", columnList = "postId")
})
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Comments extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Long parent_commentId;

    private String comment;
    private LocalDateTime deletedAt;
    private Long depth;
    // modifier id?!
    public static Comments of(Member member, Post post,String comment) {

        return new CommentsBuilder()
                .comment(comment)
                .member(member)
                .postId(post)
                .build();
    }
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }





}
