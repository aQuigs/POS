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

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId", "itemName", "cost", "adminPassword" }))
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
        String password = request.getParameter("adminPassword");

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int retCode = sql.ProcedureAddMenuItem(username, password, menuId, itemName, cost, submenu, description);
            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "" + retCode);
        }
        catch (ClassNotFoundException e)
        {
            writer.append("error");
            e.printStackTrace(writer);
        }
        catch (SQLException e)
        {
            writer.append("error");
            e.printStackTrace(writer);
        }
        catch (NumberFormatException e)
        {
            writer.append("invalid");
        }
    }
}
