import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangeFilledSeats")
public class ChangeFilledSeats extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public ChangeFilledSeats()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password", "tableId", "filledSeats" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String tableId = request.getParameter("tableId");
        String filledSeats = request.getParameter("filledSeats");

        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsChanged = sql.UpdateSQL(String.format(
                    "UPDATE TableList INNER JOIN UserInfo ON UserInfo.username='%s' AND UserInfo.password='%s' AND UserInfo.type='waitstaff'"
                            + " AND UserInfo.restaurantId=TableList.restaurantId SET filledSeats=%s WHERE tableId=%s;", username, password,
                    filledSeats, tableId));

            if (rowsChanged > 0)
                writer.append("success");
            else
                writer.append("invalid");
        }
        catch (ClassNotFoundException e)
        {
            writer.append("error");
        }
        catch (SQLException e)
        {
            writer.append("error");
        }
        catch (Exception e)
        {
            writer.append("error");
        }
    }
}
