import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ListRestaurants")
public class ListRestaurants extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ListRestaurants()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs;
            
                rs = sql.SelectSQL("SELECT restaurantId,restaurantName FROM RestaurantList;");

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
