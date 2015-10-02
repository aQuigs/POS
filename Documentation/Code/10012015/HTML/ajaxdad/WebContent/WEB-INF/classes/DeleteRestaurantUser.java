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

        if (!ServletUtilities.checkSingletonInputs(request, new String[]
        { "adminUsername", "username" }))
        {
            writer.append("error");
            return;
        }

        String adminUsername = request.getParameter("adminUsername");
        String todelete = request.getParameter("username");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String restaurantId = ServletUtilities.getRestaurantFromAdmin(sql, adminUsername);
            if (restaurantId == null)
            {
                writer.append("Invalid admin account");
                return;
            }
            
            int numDeleted = sql.DeleteSQL("DELETE FROM UserInfo WHERE username='" + todelete + "' AND restaurantId=" + restaurantId
                    + " AND username!='" + adminUsername + "';");
            if (numDeleted == 0)
            {
                writer.append("invalid");
            }
            else
            {
                writer.append("success");
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
