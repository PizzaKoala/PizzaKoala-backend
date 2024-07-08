package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.global.entity.AlarmType;
import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.hibernate.sql.Update;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
//@SQLDelete(sql = "UPDATE alarm SET deleted_at = current_timestamp WHERE id = ?")
//@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity@Table(name = "alarm", indexes = {
        @Index(name = "member_id_index", columnList = "memberId")
})
@EntityListeners(AuditingEntityListener.class)
public class Alarm extends CreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //alram 받는 사람
    private Long memberId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private AlarmArgs args;



    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Alarm of(Long memberId,AlarmType alarmType, AlarmArgs args) {

        return new AlarmBuilder()
                .memberId(memberId)
                .alarmType(alarmType)
                .args(args)
                .build();
    }

//    public void update(String title, String desc) {
//        this.title = title;
//        this.desc = desc;
//    }
//    public void delete() {
//        this.deletedAt= LocalDateTime.now();
//    }

//    private UUID modifierId;

}
