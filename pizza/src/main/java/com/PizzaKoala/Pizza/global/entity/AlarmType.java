package com.PizzaKoala.Pizza.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_Like_ON_POST("new like!"),
    NEW_COMMENT_ON_POST("new post!");


    private final String alarmText;

}
