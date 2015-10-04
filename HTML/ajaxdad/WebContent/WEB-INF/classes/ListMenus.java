import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ListMenus")
public class ListMenus extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ListMenus()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername" }))
        {
            writer.append("error");
            return;
        }

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT MenuList.menuId,MenuList.MenuName "
                    + "FROM MenuList INNER JOIN UserInfo "
                    + "ON UserInfo.restaurantId=MenuList.restaurantId AND UserInfo.type='admin' WHERE UserInfo.username='"
                    + request.getParameter("adminUsername") + "';");

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
