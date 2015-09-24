import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender
{

    static final String FROM = "aquigley1777@gmail.com"; // Replace with your "From" address. This
                                                         // address must be verified.
    // static final String TO = "ajquigle@oakland.edu"; // Replace with a "To" address. If your
    // account is still in the
    // sandbox, this address must be verified.

    // static final String BODY =
    // "This email was sent through the Amazon SES SMTP interface by using Java.";
    static final String SUBJECT = "Verify Your Email Address";

    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your
    // AWS credentials.
    static final String SMTP_USERNAME = "AKIAILK7KE44MV3ZQDBQ"; // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "Aou+Dg5o3NZsQvsEXv1vJq1C4EipxvQLcGY6/XfW0/sA"; // Replace
                                                                                        // with your
                                                                                        // SMTP
                                                                                        // password.

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";

    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we
    // will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;

    public static void sendEmail(String to, String body) throws MessagingException, NoSuchProviderException
    {
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);

        // Set properties indicating that we want to use STARTTLS to encrypt the connection.
        // The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(SUBJECT);
        msg.setContent(body, "text/plain");

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try
        {
            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
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
            // Close and terminate the connection.
            transport.close();
        }
    }
}
