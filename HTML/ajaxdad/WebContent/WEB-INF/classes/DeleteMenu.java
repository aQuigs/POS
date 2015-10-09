import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteMenu")
public class DeleteMenu extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DeleteMenu()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "menuId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String menuId = request.getParameter("menuId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsChanged = sql
                    .DeleteSQL("DELETE MenuDetails FROM UserInfo INNER JOIN MenuList ON UserInfo.type='" + username
                            + "' AND UserInfo.username='admin' AND UserInfo.restaurantId=MenuList.restaurantId "
                            + "INNER JOIN MenuDetails on MenuDetails.menuId=MenuList.menuId "
                            + "WHERE MenuList.menuId=" + menuId + ";");
            rowsChanged += sql.DeleteSQL("DELETE MenuList FROM UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username='"
                    + username + "' AND UserInfo.restaurantId=MenuList.restaurantId AND menuId=" + menuId + ";");
            
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
