import java.io.IOException;
import java.io.PrintWriter;
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
            int rowsChanged = sql
                    .DeleteSQL(String.format(
                            "DELETE OrderDetails FROM UserInfo INNER JOIN OrderList ON UserInfo.username='%s' AND UserInfo.username=OrderList.customerUsername "
                                    + "AND UserInfo.password='%s' AND UserInfo.type='waitstaff' AND UserInfo.restaurantId=OrderList.restaurantId "
                                    + "INNER JOIN OrderDetails ON OrderDetails.orderId=OrderList.orderId "
                                    + "WHERE OrderDetails.orderId=%s;", username, password, orderId));
            rowsChanged += sql.DeleteSQL(String.format("DELETE OrderList FROM UserInfo INNER JOIN OrderList ON UserInfo.username='%s' AND UserInfo.username=OrderList.customerUsername "
                    + "AND UserInfo.password='%s' AND UserInfo.type='waitstaff' AND UserInfo.restaurantId=OrderList.restaurantId "
                    + "WHERE OrderList.orderId=%s;", username, password, orderId));

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