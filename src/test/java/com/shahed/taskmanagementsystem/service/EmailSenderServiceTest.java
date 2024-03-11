package com.shahed.taskmanagementsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shahed.taskmanagementsystem.jpa.entity.Notification;
import com.shahed.taskmanagementsystem.jpa.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    void givenValidData_whenSendEmail_shouldWorkAsExpected() {
        User user = User.builder()
            .email("test@example.com")
            .build();
        Notification notification = Notification.builder()
            .user(user)
            .subject("Test Subject")
            .message("Test Message")
            .build();

        emailSenderService.sendEmail(notification);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}