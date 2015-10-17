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

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "orderId" }))
        {
            writer.append("error1");
            return;
        }

        String username = request.getParameter("username");
        String orderId = request.getParameter("orderId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsAffected = sql.UpdateSQL("UPDATE UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId "
                    + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                    + "SET OrderList.status='" + newStatus.name() + "',OrderDetails.status='" + newStatus.name() + "' "
                    + "WHERE UserInfo.username='" + username + "' AND OrderList.orderId=" + orderId + " "
                    + "AND OrderDetails.orderId=" + orderId + " AND OrderList.status='" + prevStatus.name() + "';");

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
