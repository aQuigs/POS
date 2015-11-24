import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/OrderStarted")
public class OrderStarted extends OrderStatusChanged
{
    private static final long serialVersionUID = 1L;

    public OrderStarted()
    {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        super.doChange(request, response, ServletUtilities.OrderStatus.PLACED, ServletUtilities.OrderStatus.STARTED, "kitchen");
    }
}
