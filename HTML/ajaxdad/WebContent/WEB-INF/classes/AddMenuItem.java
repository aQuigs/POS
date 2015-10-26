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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId", "itemName", "cost", "password" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("itemName");
        String cost = request.getParameter("cost");
        String submenu = request.getParameter("submenu");
        String description = request.getParameter("description");
        String password = request.getParameter("password");

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql
                  .SelectSQL(String
                            .format("SELECT MenuList.menuId FROM UserInfo INNER JOIN MenuList ON MenuList.restaurantId=UserInfo.restaurantId AND UserInfo.type='admin' AND UserInfo.username='%s' AND UserInfo.password='%s' AND MenuList.menuId=%s;",
                                    username, password, menuId));
            if (rs.next())
            {
                StringBuilder insertStatement = new StringBuilder();
                insertStatement.append("INSERT INTO MenuDetails (menuId,itemName,cost");
                if (description != null)
                    insertStatement.append(",itemDescription");
                if (submenu != null)
                    insertStatement.append(",submenu");

                insertStatement.append(") VALUES (");
                insertStatement.append(menuId);
                insertStatement.append(",'");
                insertStatement.append(itemName);
                insertStatement.append("',");
                insertStatement.append(cost);
                if (description != null)
                {
                    insertStatement.append(",'");
                    insertStatement.append(description);
                    insertStatement.append("'");
                }
                if (submenu != null)
                {
                    insertStatement.append(",'");
                    insertStatement.append(submenu);
                    insertStatement.append("'");
                }
                insertStatement.append(");");

                int rowsAdded = sql.InsertSQL(insertStatement.toString());

                if (rowsAdded != 0)
                {
                    rs = sql.SelectSQL("SELECT LAST_INSERT_ID();");
                    if (rs.next())
                    {
                        writer.append("success");
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
