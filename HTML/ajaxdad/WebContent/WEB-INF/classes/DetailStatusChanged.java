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
            ServletUtilities.OrderStatus newStatus) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (!ServletUtilities.checkSingletonInputs(request, new String[] { "username", "itemId" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("username");
        String detailId = request.getParameter("itemId");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            int rowsAffected = sql.UpdateSQL("UPDATE UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId "
                    + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                    + "SET OrderDetails.status='" + newStatus.name() + "' "
                    + "WHERE UserInfo.username='" + username + "' AND OrderDetails.status='" + prevStatus.name() + "' AND OrderDetails.detailId="
                    + detailId
                    + ";");

            if (rowsAffected != 0)
            {
                if (ServletUtilities.updateOrderStatusIfNecessary(sql, detailId))
                {
                    writer.append("orderFullyChanged:" + detailId + ":" + newStatus.name());
                }
                else
                {
                    writer.append("itemChanged:" + detailId + ":" + newStatus.name());
                }
            }
            else
            {
                writer.append("invalidItemToChange:" + detailId + ":" + newStatus.name());
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
