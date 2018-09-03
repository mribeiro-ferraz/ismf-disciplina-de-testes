package com.mackleaps.formium.service.auth;

public interface EmailSenderServiceInterface {

    void sendSimpleEmailMessage(String to, String subject, String body);
    void sendMimeEmailMessage();

}
