package cse480;

import java.sql.*;

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
	public String Url = "";
	/**
	 * Username is the username
	 */
	public String Username  = "";
	/**
	 * Password, password will be stored here when the object is created
	 */
    public String Password  = "";

    /**
     * MySQL connection, global so every function can use it
     */
	private Connection theConnection;

	/**
	 * Public constructor
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public MySQLUtilities() throws SQLException, ClassNotFoundException
	{
    	Class.forName(jdbcDriver);
    	theConnection = DriverManager.getConnection(Url, Username, Password);
	}

	/**
	 * 
	 * @param SelectSQLQuery The Select SQL Statement will go here
	 * @return Returns a resultSet that way you can get the data
	 * @throws SQLException
	 */
	public ResultSet SelectSQL(String SelectSQLQuery)throws SQLException
	{
	    PreparedStatement thePreparedStatement  = theConnection.prepareStatement(SelectSQLQuery);
	    return thePreparedStatement.executeQuery();
	}

	/**
	 * 
	 * @param UpdateSQLStatement update sql statement will go here
	 * @return returns 1 or 0, whether or not it happened
	 * @throws SQLException
	 */
	public int UpdateSQL(String UpdateSQLStatement)throws SQLException
	{
		PreparedStatement thePreparedStatement  = theConnection.prepareStatement(UpdateSQLStatement);
	    return thePreparedStatement.executeUpdate();
	}
	
	/**
	 * 
	 * @param DeleteSQLStatement The delete statement
	 * @return returns 1 or 0
	 * @throws SQLException
	 */
	public int DeleteSQL(String DeleteSQLStatement)throws SQLException
	{
		PreparedStatement thePreparedStatement  = theConnection.prepareStatement(DeleteSQLStatement);
	    return thePreparedStatement.executeUpdate();
	}
	
	/**
	 * 
	 * @param InsertSQLStatement Insert SQL Statement
	 * @return returns 1 or 0 if it is successful or not
	 * @throws SQLException
	 */
	public int InsertSQL(String InsertSQLStatement)throws SQLException
	{
		PreparedStatement thePreparedStatement  = theConnection.prepareStatement(InsertSQLStatement, Statement.RETURN_GENERATED_KEYS);
		thePreparedStatement.executeUpdate();
		ResultSet theResultSet = thePreparedStatement.getGeneratedKeys();
		if(theResultSet.next())
		{
			return theResultSet.getInt(1);
		}
		else
		{
			return 0;
		}
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
	