package com.dunk.django.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Profile({"runtime"})
@RequiredArgsConstructor
@Component
public class HtmlEmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailMessage message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(message.getTo());
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(message.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.info("message : {} ", message.getMessage());

        } catch (MessagingException e) {
            log.error("failed to email : {} ", e);
        }
    }
}
