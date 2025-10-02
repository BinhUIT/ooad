package com.example.ooad.service.scheduleservice;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ooad.service.auth.JwtService;

@Component
public class CleanTokenInDBService {
    private final JwtService jwtService;
    public CleanTokenInDBService(JwtService jwtService) {
        this.jwtService= jwtService;
    }
    @Scheduled(fixedRate=300000)
    public void cleanExpiredToken() {
        jwtService.deleteAllExpiredToken();
        jwtService.deleteAllExpiredRefreshToken();
    }
}
