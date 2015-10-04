import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetMenuDetails")
public class GetMenuDetails extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public GetMenuDetails()
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

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT MenuDetails.menuItemId,MenuDetails.itemName,MenuDetails.itemDescription,MenuDetails.cost "
                    + "FROM UserInfo INNER JOIN MenuList ON UserInfo.type='admin' AND UserInfo.username='" + request.getParameter("adminUsername")
                    + "' AND UserInfo.restaurantId=MenuList.restaurantId "
                    + "INNER JOIN MenuDetails ON MenuList.menuId=MenuDetails.menuId;");
            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append(',');
                writer.append(rs.getString(2));
                writer.append(',');
                String description = rs.getString(3);
                writer.append(description == null ? "" : description);
                writer.append(',');
                writer.append(rs.getString(4));
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
