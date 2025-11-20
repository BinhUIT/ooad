package com.example.ooad.controller.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name="Bearer Auth")
public class TestController {
    @GetMapping("/auth/test") 
    public String test() {
        return "Success";
    }
    @GetMapping("/test_jwt_token") 
    public String testWithToken() {
        return "Success";
    }
    @GetMapping("/doctor/test") 
    public String testBacSi() {
        return "Bac si";
    }
}
