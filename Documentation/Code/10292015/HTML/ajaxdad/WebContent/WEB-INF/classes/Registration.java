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
            if (ServletUtilities.isUsernameEmailTaken(sql, username, email))
            {
                writer.append("taken");
                return;
            }

            String unverifiedHash = ServletUtilities.generateHash(email, ServletUtilities.generateSalt());

            String salt = ServletUtilities.generateSalt();
            String passwordHash = ServletUtilities.generateHash(password, salt);
            if (1 == sql.InsertSQL(String.format(
                    "INSERT INTO UserInfo (username,password,salt,email,type,unverifiedHash) VALUES ('%s','%s','%s','%s','customer','%s');",
                    username, passwordHash, salt, email, unverifiedHash)))
            {
                writer.append("success");
                try
                {
                    EmailSender.sendEmail(email, "Verify Your Email Address",
                            "To verify your email, please go to the following URL: http://ec2-54-152-96-171.compute-1.amazonaws.com:8080/POS/ValidateEmail?validatecode="
                                    + unverifiedHash);
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
        catch (ClassNotFoundException e)
        {
            writer.append("error");
        }
        catch (SQLException e)
        {
            writer.append("error");
        }
    }
}
