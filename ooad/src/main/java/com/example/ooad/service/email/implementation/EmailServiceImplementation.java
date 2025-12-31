package com.example.ooad.service.email.implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.ooad.exception.BadRequestException;
import com.example.ooad.service.email.interfaces.EmailService;

@Service
public class EmailServiceImplementation implements EmailService{
    private final JavaMailSender mailSender;
    public EmailServiceImplementation(JavaMailSender mailSender) {
        this.mailSender= mailSender;
    }

    @Override
    public void sendCode(String code, String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your verification code");
            message.setText("Hi,\n\nYour verification code is: " + code + "\n\nThis code will expire in 15 minutes");
            mailSender.send(message);
            
        } catch (Exception e) {
            throw new BadRequestException("Failed to send email");
        }
    }
    
}
