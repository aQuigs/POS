import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class OrderStatusChanged extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public OrderStatusChanged()
    {
        super();
    }

    protected void doChange(HttpServletRequest request, HttpServletResponse response, ServletUtilities.OrderStatus prevStatus,
            ServletUtilities.OrderStatus newStatus) throws ServletException, IOException
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
            int rowsAffected = sql
                    .UpdateSQL(String
                            .format("UPDATE UserInfo INNER JOIN OrderList ON UserInfo.username='%s' AND UserInfo.password='%s' AND UserInfo.type='kitchen' AND UserInfo.restaurantId=OrderList.restaurantId INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId SET OrderList.status='%s',OrderDetails.status='%s' WHERE OrderList.orderId=%s AND OrderDetails.orderId=%s AND OrderList.status='%s';",
                                    username, password, newStatus.name(), newStatus.name(), orderId, orderId, prevStatus.name()));

            if (rowsAffected != 0)
            {
                writer.append("orderStatusChanged:" + orderId + ":" + newStatus.name());
            }
            else
            {
                writer.append("invalidOrderToChange:" + orderId + ":" + newStatus.name());
            }
        }
        catch (ClassNotFoundException e)
        {

            e.printStackTrace(writer);
            writer.append("error");
        }
        catch (SQLException e)
        {
            e.printStackTrace(writer);
            writer.append("error");
        }
    }
}
