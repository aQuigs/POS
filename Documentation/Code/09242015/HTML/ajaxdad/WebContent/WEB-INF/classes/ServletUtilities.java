import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class ServletUtilities
{
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
}
