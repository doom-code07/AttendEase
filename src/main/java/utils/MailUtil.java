package utils;

import jakarta.mail.*;                // Correct package for Message, Session, Transport, MessagingException, etc.
import jakarta.mail.internet.*;       // Correct package for MimeMessage, InternetAddress
import java.util.Properties;

public class MailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_USER = "ghayaassmuhammad@gmail.com"; // your Gmail
    private static final String SMTP_PASS = "qflyteiyaspojbns";        // your 16-char app password

    public static void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", "587");

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
    }
}
