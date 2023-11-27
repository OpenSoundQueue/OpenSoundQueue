package com.example.backend.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Component
public class EmailComponent {
    private static final Logger LOG = LoggerFactory.getLogger(EmailComponent.class);

    @Value("${email.password}")
    private String password;

    public void sendMail(String email, String verificationCode, String username) throws MessagingException, IOException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("OpenSoundQueue Email Verification");

        String emailTemplate = Files.readString(Path.of("backend/src/main/java/com/example/backend/email/VerificationEmailTemplate.html"));

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailTemplate.replaceAll("###VERIFICATION_CODE###", verificationCode).replaceAll("###USERNAME###", username), "text/html"); //emailVerificationTemplate.generateEmailTemplate()

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        Transport.send(message);

        LOG.info("Email wurde versendet!");
    }

    private MimeMessage createMailTemplate(String email) throws MessagingException {
        String to = email;
        String from = "registration@opensoundqueue.org";
        String host = "smtp.easyname.eu";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", false);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("registration@opensoundqueue.org", password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        return message;
    }
}
