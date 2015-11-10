import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

public class ServletUtilities
{
    private static int SALT_SIZE = 32;

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

    public static int checkEqualSizeInputs(HttpServletRequest request, String[] params)
    {
        int expectedSize = 0;
        Map<String, String[]> args = request.getParameterMap();
        for (String key : params)
        {
            if (!args.containsKey(key))
            {
                return 0;
            }

            int len = args.get(key).length;
            if (len == 0)
            {
                return 0;
            }
            if (expectedSize == 0)
            {
                expectedSize = len;
            }
            else if (expectedSize != len)
            {
                return 0;
            }
        }
        return expectedSize;
    }

    public static boolean checkExactlyOneSingletonInput(HttpServletRequest request, String[] params)
    {
        boolean found = false;
        Map<String, String[]> args = request.getParameterMap();
        for (String key : params)
        {
            if (args.containsKey(key))
            {
                if (found || args.get(key).length != 1)
                {
                    return false;
                }
                found = true;
            }
        }
        return found;
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
            int rowsChanged = sql.UpdateSQL(String.format("UPDATE OrderList SET status='%s' WHERE orderId=%s;", minDetailStatus.name(), orderId));
            return rowsChanged != 0;
        }

        return false;
    }

    public static boolean isUsernameEmailTaken(MySQLUtilities sql, String username, String email) throws SQLException
    {
        ResultSet rs = sql.SelectSQL(String.format("SELECT username FROM UserInfo WHERE username='%s' OR email='%s';", username, email));
        return rs.next();
    }

    public static String getRestaurantFromAdmin(MySQLUtilities sql, String adminUsername, String adminPassword) throws SQLException
    {
        ResultSet rs = sql.SelectSQL(String.format("SELECT restaurantId FROM UserInfo WHERE username='%s' AND password='%s' AND type='admin';",
                adminUsername, adminPassword));
        if (rs.next())
            return rs.getString(1);
        return null;
    }

    public static String generateSalt()
    {
        byte[] saltBytes = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(saltBytes);
        return DatatypeConverter.printHexBinary(saltBytes);
    }

    public static String generateHash(String password, String salt)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((password + salt).getBytes());
            return DatatypeConverter.printHexBinary(hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }

    }

    public static String storePasswordAndSalt(MySQLUtilities sql, String username, String password, String salt) throws SQLException
    {
        String passwordHash = generateHash(password, salt);
        int rowsAffected = sql.InsertSQL(String.format("UPDATE UserInfo SET password='%s',salt='%s' WHERE username='%s';", passwordHash, salt,
                username));
        return rowsAffected > 0 ? passwordHash : null;
    }

    public static String getSalt(MySQLUtilities sql, String username) throws SQLException
    {
        ResultSet rs = sql.SelectSQL(String.format("SELECT salt FROM UserInfo WHERE username='%s';", username));
        if (rs.next())
            return rs.getString(1);
        return null;
    }

    public static boolean checkPassword(MySQLUtilities sql, String username, String password) throws SQLException
    {
        return sql.SelectSQL(String.format("SELECT username FROM UserInfo WHERE username='%s' AND password='%s';", username, password)).next();
    }

    public static String getLastInsertId(MySQLUtilities sql) throws SQLException
    {
        ResultSet rs = sql.SelectSQL("SELECT LAST_INSERT_ID();");
        return rs.next() ? rs.getString(1) : null;
    }

    public static String decodeErrorCode(int errorCode)
    {
        switch (errorCode)
        {
            case -1:
                return "Could not add menu name";
            case -2:
                return "Could not find valid restaurant id";
            case -3:
                return "Returned kitchen user type when needed admin";
            case -4:
                return "Returned customer user type when needed admin";
            case -5:
                return "Returned waitstaff user type when needed admin";
            case -6:
                return "Invalid credentials given; may have been logged out";
            case -7:
                return "Menu name already in use in restaurant";
            case -8:
                return "Failed to insert menu item";
            case -9:
                return "Admin not connected to menu";
            case -10:
                return "Failed to insert user";
            case -11:
                return "Duplicate username/email";
            case -20:
                return "Failed to update user from the admin page"
            case -22:
                return "Failed to delete user from admin page";
            case -24: 
                return "User exists already with username or email from registration page";
            case -25:
                return "Could not add user from registration";
            case -26:
                return "Unverified Hash does not exist";
            case -27:
                return "Failed to update unverifiedHash to Null";
            
            default:
                return null;
        }
    }
}
