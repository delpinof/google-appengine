package com.appspot.fherdelpino.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    @ResponseBody
    public String getHello(@RequestParam(defaultValue = "world") String name) {
        return String.format("Hello %s!", name);
    }
}
