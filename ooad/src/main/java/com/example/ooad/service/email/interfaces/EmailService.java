package com.example.ooad.service.email.interfaces;

public interface EmailService {
    public void sendCode(String code, String email);

    public void sendStaffRegistrationEmail(int staffId, String email, String fullName);
}
