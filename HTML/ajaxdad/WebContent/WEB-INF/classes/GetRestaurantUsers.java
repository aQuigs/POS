import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetRestaurantUsers")
public class GetRestaurantUsers extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public GetRestaurantUsers()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[]
        { "username" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            String restaurantId = ServletUtilities.getRestaurantFromAdmin(sql, username);
            if (restaurantId == null)
            {
                writer.append("Invalid admin account");
                return;
            }

            ResultSet rs = sql.SelectSQL("SELECT username,password FROM UserInfo WHERE restaurantId=" + restaurantId + " AND username!='" + username + "';");
            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append(',');
                writer.append(rs.getString(2));
                writer.append('\n');
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
