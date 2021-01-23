package com.dunk.django.mail;

public interface EmailService {
    void sendEmail(EmailMessage message);
}
