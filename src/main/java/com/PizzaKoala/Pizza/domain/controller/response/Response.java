package com.PizzaKoala.Pizza.domain.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;
    private String message;    // 에러 메시지 (성공 시 null)

    // 성공 응답 (결과 포함)
    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result, null);
    }

    // 성공 응답 (결과 없음)
    public static Response<Void> success() {
        return new Response<>("SUCCESS", null, null);
    }

    // 에러 응답
    public static Response<Void> error(String errorCode, String message) {
        return new Response<>(errorCode, null, message);
    }

    // JSON 문자열로 변환 (테스트용 메서드)
    public String toStream() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"resultCode\":\"").append(resultCode).append("\"");

        if (result != null) {
            sb.append(",\"result\":").append("\"").append(result.toString()).append("\"");
        }

        if (message != null) {
            sb.append(",\"message\":\"").append(message).append("\"");
        }

        sb.append("}");
        return sb.toString();
    }
}
//
//    public static <T> Response<Void> error(String errorCode, String message) {
//        return new Response<>(errorCode);
//    }
//
//    public static Response<Void> success() {
//        return new Response<Void>("SUCCESS", null);
//    }
//
//    public static <T> Response<T> success(T result) {
//        return new Response<>(
//                "SUCCESS", result);
//    }
//
//    public String toStream() {
//        if (result == null) {
//            return "{" +
//                    "\"resultCode\":" + "\"" + resultCode + "\"," +
//                    "\"result\":" + null +
//                    "}";
//        }
//        return "{" +
//                "\"resultCode\":" + "\"" + resultCode + "\"," +
//                "\"result\":" + "\"" + result + "\"" +
//                "}";
//    }
//
//    ;

