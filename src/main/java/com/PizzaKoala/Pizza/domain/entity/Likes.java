package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import com.PizzaKoala.Pizza.domain.entity.enums.LikesType;
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
public class Likes extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private LikesType likesType;

    private Long likesTypeId;
    private LocalDateTime deletedAt;

    public static Likes of(Long memberId, LikesType likesType, Long likesTypeId) {

        return new LikesBuilder()
                .memberId(memberId)
                .likesType(likesType)
                .likesTypeId(likesTypeId)
                .build();

//    public void update(String title, String desc) {
//        this.title = title;
//        this.desc = desc;
//    }
//    public void delete() {
//        this.deletedAt= LocalDateTime.now();
//    }

//    private UUID modifierId;
    }
}
