import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * MySQL Utilites is the basic Select, Update, Insert and Delete
 * @author Mel Chi and Anthony Quigley 
 *
 */
public class MySQLUtilities{
	
	private static String jdbcDriver = "com.mysql.jdbc.Driver";
	/**
	 * Url is the url of where the server is
	 */
	public static String Url = "jdbc:mysql://ccl5zwj6cgtotm.c6af5bvxbhrs.us-east-1.rds.amazonaws.com:3306/CSE480";
	/**
	 * Username is the username
	 */
	public static String Username  = "cse480username";
	/**
	 * Password, password will be stored here when the object is created
	 */
    public static String Password  = "cse480password";

    /**
     * MySQL connection, global so every function can use it
     */
	private static Connection theConnection = null;

	/**
	 * Public constructor
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	
	private StringUtilities theStringUtilities = new StringUtilities();
	
	public MySQLUtilities() throws SQLException, ClassNotFoundException
	{
	    if (theConnection == null || theConnection.isClosed()) {
        	Class.forName(jdbcDriver);
        	theConnection = DriverManager.getConnection(Url, Username, Password);
	    }
	}
	
	/**
	 * 
	 * @param Url
	 * @param Username
	 * @param Password
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public MySQLUtilities(String Url, String Username, String Password) throws SQLException, ClassNotFoundException
	{
		this.Url = Url;
		this.Username = Username;
		this.Password = Password;
    	Class.forName(jdbcDriver);
    	theConnection = DriverManager.getConnection(Url, Username, Password);		
	}
	
	public int ProcedureAddMenu(String Username, String Password, String MenuName)
    {
        String query = "{CALL AddMenu(?,?,?,?,?)}"
        CallableStatement cs = theConnection.prepareCall(query);
        cs.setString(1, Username);
        cs.setString(2, Password);
        cs.setString(3, MenuName);
        cs.registerOutParameter("OReturnCode", Types.Integer);
        boolean hadResults = cs.execute();
        int outputValue = cs.getInt("OReturnCode");
        return outputValue;
    }
	

	/**
	 * Closes the connection
	 */
	public void close()
	{
		try
	    {
			theConnection.close();
		}
	    catch (SQLException sqlException)
	    {
			sqlException.printStackTrace();
	    	theConnection = null;
	    }
	}
	
	/**
	 * Overrides the finalize object which calls the close function 
	 */
	protected void finalize()
	{
		close();
	}
	
}
	
