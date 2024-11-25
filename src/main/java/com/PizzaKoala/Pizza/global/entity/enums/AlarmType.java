package com.PizzaKoala.Pizza.global.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_Like_ON_POST("new like!"),
    NEW_COMMENT_ON_POST("new post!"),
    NEW_FOLLOWER("new follower!");


    private final String alarmText;

}
