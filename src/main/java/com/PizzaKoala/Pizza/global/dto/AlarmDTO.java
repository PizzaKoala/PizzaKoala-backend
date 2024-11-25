package com.PizzaKoala.Pizza.global.dto;

import com.PizzaKoala.Pizza.global.entity.Alarm;
import com.PizzaKoala.Pizza.global.entity.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AlarmDTO {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs args;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static AlarmDTO fromAlarmEntity(Alarm alarm) {
        return new AlarmDTO(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getCreatedAt(),
                alarm.getModifiedAt()
        );
    }

}
