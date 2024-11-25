package com.PizzaKoala.Pizza.global.entity;

import com.PizzaKoala.Pizza.global.dto.AlarmArgs;
import com.PizzaKoala.Pizza.global.entity.enums.AlarmType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    //alarm 받는 사람
    private Long receiverId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private AlarmArgs args;



    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;


    public static Alarm of(Long receiverId,AlarmType alarmType, AlarmArgs args) {

        return new AlarmBuilder()
                .receiverId(receiverId)
                .alarmType(alarmType)
                .args(args)
                .build();
    }



}
