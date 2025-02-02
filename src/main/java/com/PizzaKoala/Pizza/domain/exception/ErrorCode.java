package com.PizzaKoala.Pizza.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {
    ALARM_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete alarms."),
    DUPLICATED_EMAIL_ADDRESS(HttpStatus.CONFLICT,"The email already exists."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT,"The nickname already exists."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"The user does not exist."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"The password is invalid."),
    IMAGE_UPLOAD_REQUIRED(HttpStatus.BAD_REQUEST,"Photo upload is required"),
    ONE_TO_FIVE_IMAGES_ARE_REQUIRED(HttpStatus.BAD_REQUEST, "You must upload between 1 and 5 images"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"The token is invalid."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"The Post was not found."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"Permission is invalid."),
    ALREADY_LIKED(HttpStatus.CONFLICT,"User has already liked the post."),
    ALREADY_FOLLOWED(HttpStatus.CONFLICT,"User has already followed the account."),
    FOLLOW_NOT_FOUND(HttpStatus.UNAUTHORIZED, "This follow relationship does not exist."),
    FOLLOWING_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "The user you are trying to follow does not exist"),
    ALREADY_UNLIKED(HttpStatus.CONFLICT,"User has already unliked the post."),
    FOLLOWER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "The user is not following you."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND,"User hasn't liked the post."),
    FILE_NAME_NOT_FOUND(HttpStatus.NOT_FOUND,"The file name not found."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"Error occurred while uploading to S3."),
//    ALREADY_LOGGED_OUT(HttpStatus.CONFLICT,"It is already logged out."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"The comment does not exist."),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"Connecting alarm occurs error."),
    INVALID_YEAR(HttpStatus.BAD_REQUEST,"Invalid year provided"),
    INVALID_MONTH(HttpStatus.BAD_REQUEST,"Invalid month provided"),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "The alarm does not exists.")
    ;
    private final HttpStatus status;
    private final String message;

}
