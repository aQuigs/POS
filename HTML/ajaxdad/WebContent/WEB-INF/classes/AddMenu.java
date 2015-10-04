import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddMenu")
public class AddMenu extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public AddMenu()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuName" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuName = request.getParameter("menuName");
        try
        {

            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT restaurantId FROM UserInfo WHERE type='admin' AND username='" + username + "';");
            if (rs.next())
            {
                String restaurantId = rs.getString(1);
                if (restaurantId != null)
                {
                    int rowsAdded = sql.InsertSQL("INSERT MenuList (restaurantId,menuName) VALUES (" + restaurantId + ",'" + menuName + "');");
                    if (rowsAdded != 0)
                    {
                        rs = sql.SelectSQL("SELECT LAST_INSERT_ID();");
                        if (rs.next())
                        {
                            writer.append(rs.getString(1));
                            return;
                        }
                    }
                    writer.append("failed");
                    return;
                }
            }
            writer.append("invalid");
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
