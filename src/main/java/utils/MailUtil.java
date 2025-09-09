package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_USER = "attendease0@gmail.com";
    private static final String SMTP_PASS = "spbwakwetqhgegcp";

    public static void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println(" Email sent using TLS (587)");

        } catch (Exception exTLS) {
            System.err.println(" TLS failed, trying SSL (465)...");

            Properties propsSSL = new Properties();
            propsSSL.put("mail.smtp.auth", "true");
            propsSSL.put("mail.smtp.host", SMTP_HOST);
            propsSSL.put("mail.smtp.port", "465");
            propsSSL.put("mail.smtp.socketFactory.port", "465");
            propsSSL.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            propsSSL.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session sessionSSL = Session.getInstance(propsSSL, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message messageSSL = new MimeMessage(sessionSSL);
            messageSSL.setFrom(new InternetAddress(SMTP_USER));
            messageSSL.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            messageSSL.setSubject(subject);
            messageSSL.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(messageSSL);
            System.out.println("Email sent using SSL (465)");
        }
    }
}
