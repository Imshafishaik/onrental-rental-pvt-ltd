package com.onriderentals.util;

import java.util.Properties;
import java.io.UnsupportedEncodingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    private static final String SMTP_SERVER = ConfigManager.get("SMTP_SERVER");
    private static final String SMTP_PORT = ConfigManager.get("SMTP_PORT");
    private static final String SMTP_USERNAME = ConfigManager.get("SMTP_USERNAME");
    private static final String SMTP_PASSWORD = ConfigManager.get("SMTP_PASSWORD");

    private static final String FROM_EMAIL = ConfigManager.get("SMTP_USERNAME");
    private static final String FROM_NAME = "OnRide Rentals";

    private static final int OTP_VALIDITY_MINUTES = ConfigManager.getInt("OTP_VALIDITY_MINUTES", 5);

    public static boolean sendPasswordResetEmail(String recipientEmail, String userName, String otp) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_SERVER);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.starttls.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.connectiontimeout", "5000");
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.writetimeout", "5000");
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            session.setDebug(false);

            Message message = new MimeMessage(session);
            message.setFrom(createInternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("OnRide Rentals - Password Reset Request");

            String emailBody = buildPasswordResetEmailBody(userName, otp, OTP_VALIDITY_MINUTES);
            message.setContent(emailBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Password reset email sent successfully to: " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendWelcomeEmail(String recipientEmail, String userName) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_SERVER);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.starttls.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.connectiontimeout", "5000");
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.writetimeout", "5000");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            session.setDebug(false);

            Message message = new MimeMessage(session);
            message.setFrom(createInternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome to OnRide Rentals!");

            String emailBody = "<html><body>" +
                    "<h2>Welcome to OnRide Rentals, " + userName + "!</h2>" +
                    "<p>Thank you for registering with us.</p>" +
                    "<p>You can now login to your account and start exploring vehicles or listing your own.</p>" +
                    "<p>Best regards,<br/>OnRide Rentals Team</p>" +
                    "</body></html>";
            message.setContent(emailBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Welcome email sent to: " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String buildPasswordResetEmailBody(String userName, String otp, int validityMinutes) {
        return "<html><body>" +
                "<h2>Password Reset Request</h2>" +
                "<p>Hi " + userName + ",</p>" +
                "<p>We received a request to reset your password. Use the OTP below to complete the reset:</p>" +
                "<div style='background-color: #f0f0f0; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0;'>"
                +
                "<h3 style='color: #007bff; font-size: 32px; letter-spacing: 5px;'>" + otp + "</h3>" +
                "<p style='color: #666;'>Valid for " + validityMinutes + " minutes</p>" +
                "</div>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "<p>Best regards,<br/>OnRide Rentals Team</p>" +
                "</body></html>";
    }

    private static InternetAddress createInternetAddress(String email, String personal) throws MessagingException {
        try {
            return new InternetAddress(email, personal);
        } catch (UnsupportedEncodingException e) {
            System.err.println("UnsupportedEncodingException: " + e.getMessage());
            return new InternetAddress(email);
        }
    }
}