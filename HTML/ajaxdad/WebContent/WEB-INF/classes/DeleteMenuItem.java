import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteMenuItem")
public class DeleteMenuItem extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DeleteMenuItem()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuItemId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String itemId = request.getParameter("menuItemId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsChanged = sql
                    .DeleteSQL("DELETE MenuDetails FROM UserInfo INNER JOIN MenuList ON UserInfo.username='" + username
                            + "' AND UserInfo.type='admin' AND UserInfo.restaurantId=MenuList.restaurantId "
                            + "INNER JOIN MenuDetails ON MenuDetails.menuId=MenuList.menuId "
                            + "WHERE MenuDetails.menuItemId=" + itemId + ";");

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
