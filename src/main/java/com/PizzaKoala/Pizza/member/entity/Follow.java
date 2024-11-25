package com.PizzaKoala.Pizza.member.entity;

import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        }
)
public class Follow extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime deletedAt;
    public static Follow of(Long followerId, Long followingId) {

        return new FollowBuilder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

    }
}
