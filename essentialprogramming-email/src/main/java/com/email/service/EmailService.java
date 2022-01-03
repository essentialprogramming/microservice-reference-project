package com.email.service;

public interface EmailService {

    /**
     * Send email.
     *
     * @param to destination email address
     * @param subject email subject
     * @param content email content
     */
    void sendMail(String to, String subject, String content);
}
