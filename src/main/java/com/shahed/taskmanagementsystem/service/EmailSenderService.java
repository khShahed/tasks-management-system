package com.shahed.taskmanagementsystem.service;

import com.shahed.taskmanagementsystem.jpa.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService{
    private final JavaMailSender javaMailSender;

    public void sendEmail(Notification notification){
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(notification.getUser().getEmail());
        mailMessage.setSubject(notification.getSubject());
        mailMessage.setText(notification.getMessage());
        javaMailSender.send(mailMessage);
    }
}
