import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class DetailStatusChanged extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public DetailStatusChanged()
    {
        super();
    }

    protected void doChange(HttpServletRequest request, HttpServletResponse response, ServletUtilities.OrderStatus prevStatus,
            ServletUtilities.OrderStatus newStatus, String userType) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "password", "itemId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String detailId = request.getParameter("itemId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsAffected = sql.UpdateSQL(String.format("UPDATE UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId "
                    + "AND UserInfo.username='%s' AND UserInfo.password='%s' AND UserInfo.type='%s' "
                    + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                    + "SET OrderDetails.status='%s' WHERE OrderDetails.status='%s' AND OrderDetails.detailId=%s;",
                    username, password, userType, newStatus.name(), prevStatus.name(), detailId));

            if (rowsAffected != 0)
            {
                if (ServletUtilities.updateOrderStatusIfNecessary(sql, detailId))
                {
                    writer.append("orderFullyChanged");
                }
                else
                {
                    writer.append("itemChanged");
                }
            }
            else
            {
                writer.append("invalidItemToChange");
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
