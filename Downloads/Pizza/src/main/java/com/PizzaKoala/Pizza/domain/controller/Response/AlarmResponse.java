package com.PizzaKoala.Pizza.domain.controller.Response;

import com.PizzaKoala.Pizza.domain.entity.Alarm;
import com.PizzaKoala.Pizza.domain.entity.AlarmArgs;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Long id;
    private UserResponse member;
    private AlarmType alarmType;
    private AlarmArgs args;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static AlarmResponse fromAlarmDTO(AlarmDTO alarm) {
        return new AlarmResponse(
                alarm.getId(),
                UserResponse.fromUser(alarm.getMemberId()),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getCreatedAt(),
                alarm.getDeletedAt(),
                alarm.getModifiedAt()
        );
    }
}