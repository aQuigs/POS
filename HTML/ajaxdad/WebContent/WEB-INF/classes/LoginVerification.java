import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginVerification
 */
@WebServlet("/LoginVerification")
public class LoginVerification extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public LoginVerification()
    {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();

            ResultSet rs = sql.SelectSQL(String.format("SELECT type,unverifiedHash FROM UserInfo WHERE username='%s' AND password='%s';", username,
                    ServletUtilities.generateHash(password, ServletUtilities.getSalt(sql, username))));
            if (rs.next())
            {

                if (rs.getString(2) != null)
                {
                    writer.append("unverified");
                }
                else
                {
                    String hashedPassword = ServletUtilities.storePasswordAndSalt(sql, username, password, ServletUtilities.generateSalt());
                    if (hashedPassword != null)
                    {
                        writer.append(rs.getString(1));
                        writer.append("::");
                        writer.append(hashedPassword);
                    }
                    else
                    {
                        writer.append("error");
                    }
                }
            }
            else
            {
                writer.append("invalid");
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
