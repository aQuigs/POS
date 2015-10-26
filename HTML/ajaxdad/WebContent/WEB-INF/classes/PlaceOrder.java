import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PlaceOrder")
public class PlaceOrder extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public PlaceOrder()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // TODO make sure all order details are from the same restaurant (or menu?)
        // TODO make sure username is valid
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        int inputSize = ServletUtilities.checkEqualSizeInputs(request, new String[] { "itemId", "quantity" });
        if (inputSize == 0 || !ServletUtilities.checkSingletonInputs(request, new String[] { "restaurantId" }))
        {
            writer.append("error");
            return;
        }

        Map<String, String[]> args = request.getParameterMap();
        String[] itemIds = args.get("itemId");
        String[] miscInfo = args.get("miscInfo");
        String[] quantities = args.get("quantity");

        if (miscInfo != null && miscInfo.length != inputSize)
        {
            writer.append("error");
            return;
        }

        String restaurantId = request.getParameter("restaurantId");
        String username = request.getParameter("customerUsername");
        String password = request.getParameter("customerPassword");
        try
        {
            MySQLUtilities sql = new MySQLUtilities();
            if ((username != null || password != null) && !ServletUtilities.checkPassword(sql, username, password))
            {
                writer.append("invalid");
                return;
            }

            // TODO make transaction
            int rowsChanged;
            if (username == null)
            {
                rowsChanged = sql.InsertSQL("INSERT INTO OrderList (restaurantId,status) VALUES (" + restaurantId + ",'PLACED');");
            }
            else
            {
                rowsChanged = sql.InsertSQL("INSERT INTO OrderList (restaurantId,status,customerUsername) VALUES (" + restaurantId + ",'PLACED','"
                        + username + "');");
            }

            if (rowsChanged == 0)
            {
                writer.append("failed");
                return;
            }

            ResultSet rs = sql.SelectSQL("SELECT LAST_INSERT_ID();");
            if (!rs.next())
            {
                writer.append("failed");
                return;
            }

            String orderId = rs.getString(1);
            for (int i = 0; i < inputSize; ++i)
            {
                for (int j = 0; j < Integer.parseInt(quantities[i]); ++j)
                {
                    if (miscInfo != null && miscInfo[i].length() != 0)
                    {
                        rowsChanged = sql.InsertSQL("INSERT INTO OrderDetails (orderId,menuItemId,status,miscInfo) VALUES (" + orderId + ","
                                + itemIds[i] + ",'PLACED','" + miscInfo[i] + "');");
                    }
                    else
                    {
                        rowsChanged = sql.InsertSQL("INSERT INTO OrderDetails (orderId,menuItemId,status) VALUES (" + orderId + "," + itemIds[i]
                                + ",'PLACED');");
                    }

                    if (rowsChanged == 0)
                    {
                        // TODO rollback transaction
                        writer.append("failed");
                        return;
                    }
                }
            }
            writer.append("success");
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
