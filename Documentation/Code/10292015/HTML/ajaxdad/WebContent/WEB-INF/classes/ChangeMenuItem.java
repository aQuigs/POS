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

        if (!ServletUtilities
                .checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "menuItemId", "menuId", "name", "cost" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String menuItemId = request.getParameter("menuItemId");
        String menuId = request.getParameter("menuId");
        String itemName = request.getParameter("name");
        String cost = request.getParameter("cost");
        String subMenu = request.getParameter("submenu");
        String description = request.getParameter("description");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int retCode = sql.ProcedureChangeMenuItem(username, password, menuItemId, menuId, itemName, cost, subMenu, description);
            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "" + retCode);
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
