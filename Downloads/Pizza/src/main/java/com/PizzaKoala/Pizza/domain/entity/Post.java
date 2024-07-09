package com.PizzaKoala.Pizza.domain.entity;
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
public class Post extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String title;
    private String desc;

    private Long likes;

    private LocalDateTime deletedAt;

    public static Post of(String title, String desc, Member member) {

        return new PostBuilder()
                .title(title)
                .desc(desc)
                .member(member)
                .likes(0L)
                .build();
    }

    public void update(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void likes() {
        if (this.likes == null) {
            this.likes = 0L;  // Ensure likes is not null
        }
        this.likes+=1L;
    }


}
