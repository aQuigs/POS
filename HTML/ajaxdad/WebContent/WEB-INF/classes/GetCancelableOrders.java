import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetCancelableOrders")
public class GetCancelableOrders extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public GetCancelableOrders()
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
                    "SELECT restaurantId FROM UserInfo WHERE username='%s' AND password='%s' AND type='waitstaff';", username, password));
            if (!rs.next())
            {
                writer.append("invalid");
                return;
            }

            String restaurantId = rs.getString(1);
            rs = sql
                    .SelectSQL(String
                            .format(
                                    "SELECT OrderList.orderId,OrderDetails.detailId,OrderList.status,OrderDetails.status,MenuDetails.itemName,OrderDetails.miscInfo,MenuDetails.imageUrl,OrderList.customerUsername "
                                            + "FROM UserInfo INNER JOIN OrderList ON OrderList.restaurantId=%s "
                                            + "AND OrderList.customerUsername=UserInfo.username AND ((UserInfo.type='waitstaff' AND UserInfo.username='%s') OR UserInfo.type='customer')"
                                            + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                                            + "INNER JOIN MenuDetails ON OrderDetails.menuItemId=MenuDetails.menuItemId "
                                            + "WHERE OrderDetails.status != 'PAID'"
                                            + "ORDER BY OrderList.status,OrderList.orderId,OrderDetails.status;", restaurantId, username));
            while (rs.next())
            {
                writer.append(rs.getString(1));
                writer.append("::");
                writer.append(rs.getString(2));
                writer.append("::");
                writer.append(rs.getString(3));
                writer.append("::");
                writer.append(rs.getString(4));
                writer.append("::");
                writer.append(rs.getString(5));
                writer.append("::");
                String temp = rs.getString(6);
                writer.append(temp == null ? "" : temp);
                writer.append("::");
                temp = rs.getString(7);
                writer.append(temp == null ? "" : temp);
                writer.append("::");
                writer.append(rs.getString(8));
                writer.append(";;");
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
