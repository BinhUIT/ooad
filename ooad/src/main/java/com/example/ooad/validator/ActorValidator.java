package com.example.ooad.validator;

import org.springframework.stereotype.Component;

@Component
public class ActorValidator {
    public boolean validateEmail(String email) {
        return true;
    } 
    public boolean validatePhoneNumber(String phoneNumber) {
        return true;
    } 
    public boolean validateIdCard(String idCard) {
        return true;
    }
}
