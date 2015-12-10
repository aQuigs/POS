import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CancelOrder")
public class CancelOrder extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public CancelOrder()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password", "orderId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String orderId = request.getParameter("orderId");
        
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            ResultSet rs = sql.SelectSQL(String.format(
                    "SELECT restaurantId FROM UserInfo WHERE username='%s' AND password='%s' AND type='waitstaff';", username, password));
            if (!rs.next())
            {
                writer.append("invalid");
                return;
            }

            String restaurantId = rs.getString(1);
            int rowsChanged = sql
                    .DeleteSQL(String
                            .format("DELETE OrderDetails,OrderList FROM UserInfo INNER JOIN OrderList ON OrderList.restaurantId=%s "
                                        + "AND OrderList.customerUsername=UserInfo.username AND ((UserInfo.type='waitstaff' AND UserInfo.username='%s') OR UserInfo.type='customer') "
                                        + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                                        + "WHERE OrderList.orderId=%s;", restaurantId, username, orderId));

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