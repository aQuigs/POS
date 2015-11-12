import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EditTableLayout")
public class EditTableLayout extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public EditTableLayout()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        int inputSize = ServletUtilities.checkEqualSizeInputs(request, new String[] { "tableId", "x", "y", "width", "height", "capacity", "booth" });
        if (inputSize == 0
                || !ServletUtilities.checkSingletonInputs(request, new String[] { "adminUsername", "adminPassword", "gridWidth", "gridHeight" }))
        {
            writer.append("error");
            return;
        }

        String username = request.getParameter("adminUsername");
        String password = request.getParameter("adminPassword");
        String gridWidth = request.getParameter("gridWidth");
        String gridHeight = request.getParameter("gridHeight");

        Map<String, String[]> args = request.getParameterMap();
        String[] ids = args.get("tableId");
        String[] x = args.get("x");
        String[] y = args.get("y");
        String[] w = args.get("width");
        String[] h = args.get("height");
        String[] c = args.get("capacity");
        String[] b = args.get("booth");

        try
        {
            MySQLUtilities sql = new MySQLUtilities();

            String restaurantId = ServletUtilities.getRestaurantFromAdmin(sql, username, password);
            Set<String> alreadyPresentTables = new HashSet<>();
            ResultSet rs = sql.SelectSQL(String.format("SELECT tableId FROM TableList WHERE restaurantId=%s;", restaurantId));

            while (rs.next())
            {
                alreadyPresentTables.add(rs.getString(1));
            }

            StringBuilder tableIds = new StringBuilder();
            for (int i = 0; i < inputSize; ++i)
            {
                if (i != 0)
                {
                    tableIds.append(',');
                }

                if (ids[i].equals("0")) // add
                {
                    int rowsAffected = sql.InsertSQL(String.format(
                            "INSERT INTO TableList (restaurantId,x,y,width,height,capacity,filledSeats,booth) VALUES (%s,%s,%s,%s,%s,%s,0,'%s');",
                            restaurantId, x[i], y[i], w[i], h[i], c[i], b[i]));
                    if (!updateTableIdReturns(rowsAffected, tableIds, sql))
                    {
                        writer.append("error");
                        return;
                    }
                }
                else
                // update
                {
                    if (!alreadyPresentTables.contains(ids[i]))
                    {
                        writer.append("error");
                        return;
                    }

                    int rowsAffected = sql.InsertSQL(String.format(
                            "UPDATE TableList SET x=%s,y=%s,width=%s,height=%s,capacity=%s,filledSeats=0,booth='%s' WHERE tableId=%s;",
                            x[i], y[i], w[i], h[i], c[i], b[i], ids[i]));

                    if (rowsAffected != 1)
                    {
                        writer.append("error");
                        return;
                    }

                    tableIds.append(ids[i]);
                    alreadyPresentTables.remove(ids[i]);
                }
            }

            // Delete those not in POST arguments
            for (String tableId : alreadyPresentTables)
            {
                if (1 != sql.DeleteSQL(String.format("DELETE FROM TableList WHERE tableId=%s;", tableId)))
                {
                    writer.append("error");
                    return;
                }
            }

            sql.UpdateSQL(String.format("UPDATE RestaurantList SET tableGridWidth=%s,tableGridHeight=%s WHERE restaurantId=%s;", gridWidth,
                    gridHeight, restaurantId));

            writer.append(tableIds.toString());
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

    private boolean updateTableIdReturns(int rowsAffected, StringBuilder tableIds, MySQLUtilities sql) throws SQLException
    {
        if (rowsAffected == 1)
        {
            String tableId = ServletUtilities.getLastInsertId(sql);
            if (tableId != null)
            {
                tableIds.append(tableId);
                return true;
            }
        }
        return false;
    }
}
