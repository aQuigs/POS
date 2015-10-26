import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ResetPassword()
    {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "email" }))
        {
            writer.append("error");
            return;
        }

        String email = request.getParameter("email");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT username,email FROM UserInfo WHERE email='" + email + "';");
            if (!rs.next())
            {
                writer.append("invalid");
                return;
            }

            String username = rs.getString(1);
            String password = ServletUtilities.generatePassword();

            String salt = ServletUtilities.generateSalt();
            String hashedPassword = ServletUtilities.generateHash(password, salt);
            ServletUtilities.storePasswordAndSalt(sql, username, hashedPassword, salt);
            EmailSender.sendEmail(email, "Password Reset", "Per your request, your password has been reset to " + password);
            writer.append("success");
        }
        catch (MessagingException e)
        {
            writer.append("error");
        }
        catch (ClassNotFoundException e1)
        {
            writer.append("error");
        }
        catch (SQLException e)
        {
            writer.append("error");
        }
    }
}
