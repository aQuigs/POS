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
            ServletUtilities.OrderStatus newStatus, String userType) throws ServletException, IOException
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
//            int retCode = sql.ProcedureOrderStatusChanged(username, password, orderId, newStatus.name(), prevStatus.name());
//            writer.append(retCode < 0 ? ServletUtilities.decodeErrorCode(retCode) : "success");
            int rowsAffected = sql
                    .UpdateSQL(String
                            .format("UPDATE UserInfo INNER JOIN OrderList ON UserInfo.username='%s' AND UserInfo.password='%s' AND UserInfo.type='%s'"
                                    + " AND UserInfo.restaurantId=OrderList.restaurantId INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId"
                                    + " SET OrderList.status='%s',OrderDetails.status='%s' WHERE OrderList.orderId=%s AND OrderDetails.orderId=%s AND OrderList.status='%s';",
                                    username, password, userType, newStatus.name(), newStatus.name(), orderId, orderId, prevStatus.name()));

            if (rowsAffected != 0)
            {
                writer.append("orderStatusChanged");
            }
            else
            {
                writer.append("invalidOrderToChange");
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
