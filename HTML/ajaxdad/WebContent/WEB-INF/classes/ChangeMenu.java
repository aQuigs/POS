import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangeMenu")
public class ChangeMenu extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ChangeMenu()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId", "menuName" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuId = request.getParameter("menuId");
        String menuName = request.getParameter("menuName");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsChanged = sql.UpdateSQL("UPDATE UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username='" + username
                    + "' AND UserInfo.restaurantId=MenuList.restaurantId SET menuName='" + menuName + "' WHERE menuId=" + menuId + ";");

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
