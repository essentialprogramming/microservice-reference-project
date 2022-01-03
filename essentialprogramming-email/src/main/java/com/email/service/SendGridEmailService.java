package com.email.service;

import com.crypto.Crypt;
import com.email.error.codes.EmailErrorCode;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.util.cloud.Configuration;
import com.util.cloud.ConfigurationManager;
import com.util.cloud.Environment;
import com.util.exceptions.ServiceException;
import com.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.util.cloud.Environment.getProperty;

public class SendGridEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);
    private static final Configuration configuration = ConfigurationManager.getConfiguration();

    private static final String EMAIL_FROM = getProperty("EMAIL_FROM", "razvan.prichici@thinkdigital21.de");
    private static final String EMAIL_FROM_NAME = getProperty("EMAIL_FROM_NAME", "EssentialProgramming Team");


    private final SendGrid sendGridClient;

    public SendGridEmailService() {
        final String encryptedAPIKey = Environment.getProperty("SENDGRID_API_KEY", configuration.getPropertyAsString("sendgrid.api.key"));
        sendGridClient = initClient(encryptedAPIKey);
    }

    @Override
    public void sendMail(String recipient, String subject, String htmlContent) {
        Email from = new Email(EMAIL_FROM, EMAIL_FROM_NAME);
        Email to = new Email(recipient);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, StringUtils.encodeText(subject), to, content);


        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGridClient.api(request);
            if (response.getStatusCode() != 202) {
                throw new ServiceException(EmailErrorCode.UNABLE_TO_SEND_EMAIL,
                        "Unable to send email for: " + recipient + " with subject:" + subject + ". Sendgrid response status code: " + response.getStatusCode());
            }
        } catch (IOException ex) {
            throw new ServiceException(EmailErrorCode.UNABLE_TO_SEND_EMAIL,
                    "Unable to send email for: " + recipient + " with subject:" + subject + ".");
        }
    }

    private static SendGrid initClient(final String encryptedAPIKey){
        final String sendGridAPIKey;
        try {
            sendGridAPIKey = Crypt.decrypt(encryptedAPIKey, "supercalifragilisticexpialidocious");
            return new SendGrid(sendGridAPIKey);
        } catch (GeneralSecurityException e) {
            logger.error(EmailErrorCode.EMAIL_CLIENT_INITIALIZATION_FAILED.getDescription());
            throw new ServiceException(EmailErrorCode.EMAIL_CLIENT_INITIALIZATION_FAILED, e);
        }
    }
}
