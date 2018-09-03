package com.mackleaps.formium.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService implements EmailSenderServiceInterface {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleEmailMessage(String to, String subject, String body) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(body);
        mailSender.send(email);
    }

    @Override
    public void sendMimeEmailMessage() {
        throw new UnsupportedOperationException();
    }
}
