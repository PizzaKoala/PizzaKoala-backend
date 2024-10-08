package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(PizzaAppException e) {
        this.errorCode = e.getErrorCode().toString();
        this.errorMessage = e.getMessage();
    }
}
