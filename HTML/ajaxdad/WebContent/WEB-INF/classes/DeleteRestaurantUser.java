import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteRestaurantUser")
public class DeleteRestaurantUser extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DeleteRestaurantUser()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "username" }))
        {
            writer.append("error");
            return;
        }

        String adminUsername = request.getParameter("adminUsername");
        String adminPassword = request.getParameter("adminPassword");
        String todelete = request.getParameter("username");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int retCode = sql.ProcedureDeleteRestaurantUser(adminUsername, adminPassword, todelete);
            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "success");
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
