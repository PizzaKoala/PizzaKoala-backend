package com.PizzaKoala.Pizza.domain.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {
    @RequestMapping(value = "/test/hello")
    @ResponseBody
    public String helloRuckus(Model model) {
        return "Hello Ruckus";
    }

}
