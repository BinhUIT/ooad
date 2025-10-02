package com.example.ooad.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/auth/test") 
    public String test() {
        return "Success";
    }
    @GetMapping("/test_jwt_token") 
    public String testWithToken() {
        return "Success";
    }
    @GetMapping("/bac_si/test") 
    public String testBacSi() {
        return "Bac si";
    }
}
