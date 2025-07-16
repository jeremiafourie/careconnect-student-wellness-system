package com.bc.web.dao;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String recipientEmail, String recipientName) throws MessagingException {
        final String senderEmail = "Mosamsiza.29@gmail.com";
        final String senderPassword = "yvgj epfx fyhw gchw"; // Use App Password (not Gmail password)

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Registration Confirmation");
        message.setText("Dear " + recipientName + ",\n\nThank you for registering!\n\nBest regards,\nStudent Management Team");

        Transport.send(message);
    }
}