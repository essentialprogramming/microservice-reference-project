package com.email.config;

import com.email.service.EmailService;
import com.email.service.SendGridEmailService;

import com.email.service.SendInBlueEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailConfiguration {

    @Bean
    public EmailService loadEmailService() {
        return new SendGridEmailService();
    }
}
