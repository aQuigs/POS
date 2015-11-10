import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetTableLayout")
public class GetTableLayout extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public GetTableLayout()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL(String.format(
                    "SELECT TableList.tableId,TableList.x,TableList.y,TableList.width,TableList.height,TableList.capacity,TableList.filledSeats,TableList.booth "
                    + "FROM UserInfo INNER JOIN TableList ON UserInfo.restaurantId=TableList.restaurantId AND UserInfo.username='%s' AND UserInfo.password='%s' "
                    + "AND (UserInfo.type='admin' OR UserInfo.type='waitstaff');", username, password));
            
            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append(',');
                writer.append(rs.getString(2));
                writer.append(',');
                writer.append(rs.getString(3));
                writer.append(',');
                writer.append(rs.getString(4));
                writer.append(',');
                writer.append(rs.getString(5));
                writer.append(',');
                writer.append(rs.getString(6));
                writer.append(',');
                writer.append(rs.getString(7));
                writer.append(',');
                writer.append(rs.getString(8));
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
