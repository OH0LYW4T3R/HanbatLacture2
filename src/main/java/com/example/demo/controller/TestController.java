package com.example.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class TestController {
    @GetMapping("/test")
    String test() {
        return "test.html";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
