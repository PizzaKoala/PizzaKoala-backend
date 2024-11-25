package com.PizzaKoala.Pizza.global.controller.response;

import com.PizzaKoala.Pizza.global.dto.AlarmArgs;
import com.PizzaKoala.Pizza.global.dto.AlarmDTO;
import com.PizzaKoala.Pizza.global.entity.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private AlarmArgs args;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static AlarmResponse fromAlarmDTO(AlarmDTO alarmDTO) {
        return new AlarmResponse(
                alarmDTO.getId(),
                alarmDTO.getAlarmType(),
                alarmDTO.getArgs(),
                alarmDTO.getCreatedAt(),
                alarmDTO.getModifiedAt()
        );
    }

}
