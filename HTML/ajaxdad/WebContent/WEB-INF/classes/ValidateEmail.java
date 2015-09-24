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

@WebServlet("/ValidateEmail")
public class ValidateEmail extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ValidateEmail()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "validatecode" }))
        {
            writer.append("error");
            return;
        }

        String hash = request.getParameter("validatecode");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT unverifiedHash FROM UserInfo WHERE unverifiedHash='" + hash + "';");
            if (rs.next())
            {
                sql.UpdateSQL("UPDATE UserInfo SET unverifiedHash=NULL WHERE unverifiedHash='" + hash + "';");
                writer.append("validated");
            }
            else
            {
                writer.append("invalid");
            }
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
