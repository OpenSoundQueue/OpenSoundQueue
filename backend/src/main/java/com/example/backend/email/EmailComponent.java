/**
 * This component is responsible for sending emails
 */

package com.example.backend.email;

import com.example.backend.system_management.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Component
public class EmailComponent {
    PropertyService propertyService;

    public EmailComponent(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(EmailComponent.class);

    /**
     * This method sends an email to the specified user in order to verify their email address.
     * It contains a verification code so that the email can be mapped to the user.
     * The username is being used to make the email more personalized.
     *
     * @param email address of the recipient
     * @param verificationCode email verification code
     * @param username name of the recipient
     * @throws MessagingException
     */
    public void sendMail(String email, String verificationCode, String username) throws MessagingException, IOException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("OpenSoundQueue Email Verification");

        String emailTemplate = "";

        ClassLoader classLoader = getClass().getClassLoader();
        String resourcePath = "data/VerificationEmailTemplate.html";
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                emailTemplate = String.join("\n", reader.lines().toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailTemplate.replaceAll("###VERIFICATION_CODE###", verificationCode).replaceAll("###USERNAME###", username), "text/html"); //emailVerificationTemplate.generateEmailTemplate()

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        Transport.send(message);

        LOG.info("Email wurde versendet!");
    }

    /**
     * This function creates a simple template for emails that can be adjusted fairly easy
     * @param toEmail address of the recipient
     * @return a mail template
     * @throws MessagingException
     */
    private MimeMessage createMailTemplate(String toEmail) throws MessagingException, IOException {
        String smtpPassword = propertyService.getProperty("system.smtp-password");
        String smtpHostString = propertyService.getProperty("system.smtp-host-string");
        String fromEmail = propertyService.getProperty("system.from-email");

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", smtpHostString);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", false);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, smtpPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

        return message;
    }
}
