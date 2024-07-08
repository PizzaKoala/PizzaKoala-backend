package com.PizzaKoala.Pizza.domain.model;

import com.PizzaKoala.Pizza.domain.entity.Alarm;
import com.PizzaKoala.Pizza.domain.entity.AlarmArgs;
import com.PizzaKoala.Pizza.domain.entity.Comments;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AlarmDTO {
    private Long id;
    private UserDTO memberId;
    private AlarmType alarmType;
    private AlarmArgs args;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static AlarmDTO fromAlarmEntity(Alarm alarm, Member member) {
        return new AlarmDTO(
                alarm.getId(),
                UserDTO.fromMemberEntity(member),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getCreatedAt(),
                alarm.getDeletedAt(),
                alarm.getModifiedAt()
        );
    }

}
