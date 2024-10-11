package com.PizzaKoala.Pizza.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() throws IOException {
        return "meepy day!!!";

    }
}
