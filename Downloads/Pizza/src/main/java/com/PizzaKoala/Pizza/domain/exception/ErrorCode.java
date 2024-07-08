package com.PizzaKoala.Pizza.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_EMAIL_ADDRESS(HttpStatus.CONFLICT,"The email already exists."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT,"The nickname already exists."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"The member does not exist."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"The password is invalid."),
    IMAGE_UPLOAD_REQUIRED(HttpStatus.BAD_REQUEST,"Photo upload is required"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"The token is invalid."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"The Post was not found."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"Permission is invalid."),
    ALREADY_LIKED(HttpStatus.CONFLICT,"User has already liked the post.")
    ;
    private final HttpStatus status;
    private final String message;

}
