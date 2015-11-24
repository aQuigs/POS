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

        if (!ServletUtilities.checkExactlyOneSingletonInput(request, new String[] { "adminUsername", "adminPassword", "restaurantId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String restaurantId = request.getParameter("restaurantId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs;
            if (username == null && password == null)
            {
                // use restaurantId
                rs = sql.SelectSQL("SELECT menuId,menuName FROM MenuList WHERE restaurantId=" + restaurantId + ";");
            }
            else
            {
                // use username
                rs = sql.SelectSQL(String
                        .format("SELECT MenuList.menuId,MenuList.menuName "
                                + "FROM MenuList INNER JOIN UserInfo "
                                + "ON UserInfo.restaurantId=MenuList.restaurantId AND UserInfo.type='admin' "
                                + "WHERE UserInfo.username='%s' AND UserInfo.password='%s';",
                                username, password));
            }

            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append("::");
                writer.append(rs.getString(2));
                writer.append(";;");
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
