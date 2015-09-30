import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

@WebServlet("/Registration")
public class Registration extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public Registration()
    {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password", "email" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT username FROM UserInfo WHERE username='" + username + "' or email='" + email + "';");
            if (rs.next())
            {
                writer.append("taken");
                return;
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(email.getBytes("UTF-8"));
            String hashString = DatatypeConverter.printHexBinary(hash);

            if (1 == sql.InsertSQL("INSERT INTO UserInfo (username,password,email,type,unverifiedHash) VALUES ('" + username + "','" + password
                    + "','" + email + "','customer','" + hashString + "');"))
            {
                writer.append("success");
                try
                {
                    EmailSender.sendEmail(email, "Verify Your Email Address",
                            "To verify your email, please go to the following URL: http://ec2-52-23-188-89.compute-1.amazonaws.com:8080/POS/ValidateEmail?validatecode="
                                    + hashString);
                }
                catch (MessagingException e)
                {
                    e.printStackTrace(writer);
                }
            }
            else
            {
                writer.append("failure");
            }
        }
        catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
            writer.append("error");
        }
        catch (SQLException e)
        {
            writer.append("error");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            writer.append("error");
        }
    }
}
