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

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "menuId" }))
        {
            writer.append("error");
            return;
        }

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL("SELECT menuItemId,itemName,itemDescription,cost,submenu,imageUrl FROM MenuDetails WHERE MenuDetails.menuId="
                    + request.getParameter("menuId") + " ORDER BY submenu;");
            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append(',');
                writer.append(rs.getString(2));
                writer.append(',');
                String temp = rs.getString(3);
                writer.append(temp == null ? "" : temp);
                writer.append(',');
                writer.append(rs.getString(4));
                writer.append(',');
                temp = rs.getString(5);
                writer.append(temp == null ? "" : temp);
                writer.append(',');
                temp = rs.getString(6);
                writer.append(temp == null ? "" : temp);
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
