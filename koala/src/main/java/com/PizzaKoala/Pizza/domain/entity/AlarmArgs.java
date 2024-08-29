package com.PizzaKoala.Pizza.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class AlarmArgs {
    //알람을 발생시킨사람
    private Long fromMemberId;
    private Long targetId;
}
