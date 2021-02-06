package com.recipe.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
/*
*   TODO
*       추후 진짜 이메일 서비스로 변경
*
* */
@Slf4j
@Profile("local")
@Component
public class ConsoleEmailServiceImpl implements EmailService{

    @Override
    public void sendEmail(EmailMessage message) {
        log.info("message : " + message);
    }
}
