import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class ServletUtilities
{
    public enum OrderStatus
    {
        PLACED,
        STARTED,
        COOKED,
        DELIVERED,
        PAID
    }

    public static boolean checkSingletonInputs(HttpServletRequest request, String[] params)
    {
        Map<String, String[]> args = request.getParameterMap();
        for (String key : params)
        {
            if (!args.containsKey(key) || args.get(key).length != 1)
            {
                return false;
            }
        }
        return true;
    }

    public static String generatePassword()
    {
        String chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (sb.length() < 9)
        {
            int index = (int) (random.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    public static boolean updateOrderStatusIfNecessary(MySQLUtilities sql, String detailId) throws SQLException
    {
        ResultSet rs = sql
                .SelectSQL("SELECT OrderList.status,OrderDetails.status,OrderList.orderId from OrderList INNER JOIN OrderDetails ON OrderList.orderId=OrderDetails.orderId;");

        String orderId = null;
        OrderStatus os = null;
        OrderStatus minDetailStatus = OrderStatus.PAID;
        while (rs.next())
        {
            if (os == null)
            {
                os = OrderStatus.valueOf(rs.getString(1));
                orderId = rs.getString(3);
            }

            OrderStatus detailStatus = OrderStatus.valueOf(rs.getString(2));
            if (detailStatus.ordinal() < minDetailStatus.ordinal())
                minDetailStatus = detailStatus;
        }

        if (os != null && os.ordinal() < minDetailStatus.ordinal())
        {
            int rowsChanged = sql.UpdateSQL("UPDATE OrderList SET status='" + minDetailStatus.name() + "' WHERE orderId=" + orderId + ";");
            return rowsChanged != 0;
        }

        return false;
    }
}
