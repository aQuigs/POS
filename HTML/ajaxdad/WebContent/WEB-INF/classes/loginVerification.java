import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class loginVerification
 */
@WebServlet("/loginVerification")
public class loginVerification extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public loginVerification()
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

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("select username,password,type,unverifiedHash from UserInfo where username='"
                    + request.getParameter("username") + "' and password='" + request.getParameter("password") + "'");
            if (rs.next())
            {
                if (rs.getString(4) != null)
                {
                    writer.append("unverified");
                }
                else
                {
                    writer.append(rs.getString(3));
                }
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
