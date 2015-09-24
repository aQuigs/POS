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

/**
 * Servlet implementation class loginVerification
 */
@WebServlet("/registration")
public class registration extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public registration()
    {
        super();
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

        if (!checkInputs(request, new String[] { "username", "password", "email" }))
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
                    EmailSender.sendEmail(email, "HEY BRO");
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
