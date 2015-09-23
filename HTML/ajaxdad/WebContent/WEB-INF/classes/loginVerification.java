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

//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//    {
//        // TODO Auto-generated method stub
//        // response.getWriter().append("Served at: ");
//        doPost(request, response);
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        MySQLUtilities sql;
        try
        {
            sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("select username,password,type,unverifiedHash from UserInfo where username='"
                    + request.getParameter("username") + "' and password='" + request.getParameter("password") + "'");
            if (rs.next())
            {
                String rv;
                if (rs.getString(4) == null)
                {
                    rv = "unverified";
                }
                else
                {
                    rv = rs.getString(3);
                }
                writer.append(rv);
            }
        }
        catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
