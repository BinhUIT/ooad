package com.example.ooad.service.email.implementation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import com.example.ooad.exception.BadRequestException;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImplementation emailService;

    @Test
    void sendMailSuccess() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
       assertDoesNotThrow(()->{
        emailService.sendCode("", "");
       });
    }

    @Test
    void sendMailFail() {
        doThrow(new RuntimeException("Fail")).when(mailSender).send(any(SimpleMailMessage.class));
       BadRequestException exception = assertThrows(BadRequestException.class, ()->{
        emailService.sendCode("","");
       });
       assertNotNull(exception);
       assertEquals("Failed to send email", exception.getMessage());
    }
}
