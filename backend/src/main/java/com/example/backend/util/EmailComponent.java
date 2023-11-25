package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class EmailComponent {
    private static final Logger LOG = LoggerFactory.getLogger(EmailComponent.class);

    public void sendMail(String email, String verificationCode) throws MessagingException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("Dies ist eine Test Email");

        EmailTemplate emailVerificationTemplate = new EmailTemplate();
        emailVerificationTemplate.addTitle("Dies ist eine Test Email");
        emailVerificationTemplate.addLine();
        emailVerificationTemplate.addText("Der Verification Code lautet: " + verificationCode);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailVerificationTemplate.generateEmailTemplate(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        Transport.send(message);

        LOG.info("Email wurde versendet!");
    }

    private static MimeMessage createMailTemplate(String email) throws MessagingException {
        String to = email;
        String from = "cityquest.noreply@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", false);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cityquest.noreply@gmail.com", "lwykmklxlaykjufg");
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        return message;
    }
}
