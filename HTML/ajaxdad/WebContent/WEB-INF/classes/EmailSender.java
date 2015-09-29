import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender
{
    private static final String FROM = "aquigley1777@gmail.com";
    private static final String SMTP_USERNAME = "AKIAILK7KE44MV3ZQDBQ";
    private static final String SMTP_PASSWORD = "Aou+Dg5o3NZsQvsEXv1vJq1C4EipxvQLcGY6/XfW0/sA";
    private static final String HOST = "email-smtp.us-east-1.amazonaws.com";

    private static final int PORT = 25;

    public static void sendEmail(String to, String subject, String body) throws MessagingException, NoSuchProviderException
    {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(body, "text/plain");

        Transport transport = session.getTransport();

        try
        {
            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex)
        {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            transport.close();
        }
    }
}
