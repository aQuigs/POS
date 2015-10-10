import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddMenuItem")
public class AddMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public AddMenuItem()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId", "itemName", "cost" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("itemName");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql
                    .SelectSQL("SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON MenuList.restaurantId=UserInfo.restaurantId AND UserInfo.type='admin' AND UserInfo.username='"
                            + username + "' AND MenuList.menuId=" + menuId + ";");
            if (rs.next())
            {
                int rowsAdded;
                if (description == null)
                {
                    rowsAdded = sql.InsertSQL("INSERT INTO MenuDetails (menuId,itemName,cost) VALUES (" + menuId + ",'" + itemName
                            + "'," + cost + ");");
                }
                else
                {
                    rowsAdded = sql.InsertSQL("INSERT INTO MenuDetails (menuId,itemName,cost,itemDescription) VALUES (" + menuId + ",'" + itemName
                            + "'," + cost + ",'" + description + "');");
                }

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
