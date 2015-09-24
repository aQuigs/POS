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

    private String generatePassword()
    {
        String chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (sb.length() < 9)
        {
            int index = (int) (random.nextFloat() * chars.length());
            sb.append(Characters.charAt(index));
        }

        return sb.toString();

    }

    private boolean checkInputs(HttpServletRequest request, String[] params)
    {
        Map<String, String[]> args = request.getParameterMap();
        for (String key : params)
        {
            if (!args.containsKey(key) || args.get(key).length != 1)
            {
                return false;
            }
        }
        return true;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!checkInputs(request, new String[] { "email" }))
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
            String password = generatePassword();

            sql.UpdateSQL("UPDATE UserInfo SET password='" + password + "' WHERE username='" + username + "';");
            writer.append("success");
            EmailSender.sendEmail(email, "Password Reset", "Per your request, your password has been reset to " + password);
        }
        catch (MessagingException e)
        {
            e.printStackTrace(writer);
        }
        catch (ClassNotFoundException e1)
        {
            e1.printStackTrace(writer);
            writer.append("error");
        }
        catch (SQLException e)
        {
            e.printStackTrace(writer);
            writer.append("error");
        }
    }
}
