import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangeMenuItem")
public class ChangeMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ChangeMenuItem()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuItemId", "menuId", "name", "cost" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuItemId = request.getParameter("menuItemId");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("name");
        String cost = request.getParameter("cost");
        String submenu = request.getParameter("submenu");
        String description = request.getParameter("description");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();

            // Ensure new menuId is valid for particular restaurant
            ResultSet rs = sql.SelectSQL("SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username='"
                    + username
                    + "' AND UserInfo.restaurantId=MenuList.restaurantId AND MenuList.menuId=" + menuId + ";");
            if (!rs.next())
            {
                writer.append("invalid menuId");
                return;
            }

            StringBuilder statement = new StringBuilder();
            statement.append("UPDATE UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username='");
            statement.append(username);
            statement
                    .append("' AND UserInfo.restaurantId=MenuList.restaurantId INNER JOIN MenuDetails ON MenuList.menuId=MenuDetails.menuId AND MenuDetails.menuItemId=");
            statement.append(menuItemId);
            statement.append(" SET MenuDetails.menuId=");
            statement.append(menuId);
            statement.append(",MenuDetails.itemName='");
            statement.append(itemName);
            statement.append("',MenuDetails.cost=");
            statement.append(cost);
            if (description != null)
            {
                statement.append(",MenuDetails.itemDescription='");
                statement.append(description);
                statement.append("'");
            }
            if (submenu != null)
            {
                statement.append(",MenuDetails.submenu='");
                statement.append(submenu);
                statement.append("'");
            }

            statement.append(';');

            int rowsChanged = sql.UpdateSQL(statement.toString());

            if (rowsChanged != 0)
            {
                writer.append("success");
            }
            else
            {
                writer.append("failed");
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