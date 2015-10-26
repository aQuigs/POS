import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetUnfinishedOrders")
public class GetUnfinishedOrders extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public GetUnfinishedOrders()
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
                    "SELECT OrderList.orderId,OrderDetails.detailId,OrderList.status,OrderDetails.status,MenuDetails.itemName,OrderDetails.miscInfo,MenuDetails.submenu "
                            + "FROM UserInfo INNER JOIN OrderList ON UserInfo.restaurantId=OrderList.restaurantId "
                            + "AND UserInfo.username='%s' AND UserInfo.password='%s' AND UserInfo.type='kicthen' "
                            + "INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId "
                            + "INNER JOIN MenuDetails ON OrderDetails.menuItemId=MenuDetails.menuItemId "
                            + "WHERE OrderDetails.status='PLACED' OR OrderDetails.status='STARTED' OR OrderDetails.status='COOKED' "
                            + "ORDER BY OrderList.status,OrderList.orderId,OrderDetails.status;", username, password));
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
                String miscInfo = rs.getString(6);
                writer.append(miscInfo == null ? "" : miscInfo);
                writer.append(',');
                String submenu = rs.getString(7);
                writer.append(miscInfo == null ? "" : submenu);
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
