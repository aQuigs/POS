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
	public String Url = "jdbc:mysql://ccl5zwj6cgtotm.c6af5bvxbhrs.us-east-1.rds.amazonaws.com:3306/CSE480";
	/**
	 * Username is the username
	 */
	public String Username  = "cse480username";
	/**
	 * Password, password will be stored here when the object is created
	 */
    public String Password  = "fuckyoubitch";

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
/*		if(theResultSet.next())
		{
			return theResultSet.getInt(1);
		}
		else
		{
			return 0;
		}*/
		return 1;
	}
	
	/**
	 * Inserts into the TableName given and giving a hashmap of <String, String> Column Name and Value
	 * @param TableName
	 * @param TableColumnNameAndValue HashMap < String , String>    which is <Column Name, Value>
	 * @return returns 1 if success, 0 if failure
	 * @throws SQLException
	 */
	public int InsertSQL(String TableName, HashMap<String,String> TableColumnNameAndValue) throws SQLException
	{
    	//sql.InsertSQL("INSERT INTO Mel (melcolumn1, melcolumn2) values ('String1', 'String2')");
		String insideTheColumnInsert = "";
		String insideTheValueInsert = "";
				
		for (Map.Entry<String, String> theMap : TableColumnNameAndValue.entrySet())
		{
			String columnName = theMap.getKey();
			String value = theMap.getValue();
			
			if (insideTheColumnInsert.length() > 0)
			{
				insideTheColumnInsert = " , " + insideTheColumnInsert;
				insideTheValueInsert = " , " + insideTheValueInsert;
			}
			
			insideTheColumnInsert = columnName + insideTheColumnInsert;
			insideTheValueInsert = SQLFormat(value) + insideTheValueInsert;
		}
    	
		String insertSQL = "INSERT INTO " + TableName + " (" + insideTheColumnInsert + ") values (" + insideTheValueInsert + ")";
		
		return InsertSQL(insertSQL);
		
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
	 * Formatting value with ' and '
	 * @param Value
	 * @return
	 */
	public String SQLFormat(String Value)
	{
		if(Value.length() > 0)
		{
			return "'"+ Value + "'";
		}
		else
		{
			return "''";
		}
	}
	
	/**
	 * Generic Table without null or ascending declarations
	 * @param TableName
	 * @param TableColumnNameAndType
	 * @return 0 if can not do, 1 if successful
	 * @throws SQLException
	 */
	public int CreateTableNoRestrictions(String TableName, HashMap<String,Object> TableColumnNameAndType) throws SQLException
	{
		if (TableName.length() <= 0 || TableColumnNameAndType.size() == 0)
		{
			return 0;
		}
		String createTableSQL = "CREATE TABLE " + TableName + " (";
		String insideTheInsert = "";

		for (Map.Entry<String, Object> theMap : TableColumnNameAndType.entrySet())
		{
			String preparedAddition = "";
			String columnName = theMap.getKey();
			Object columnType = theMap.getValue();

			preparedAddition = preparedAddition + columnName + " ";
			
			if (columnType == String.class)
			{
				preparedAddition = preparedAddition + " VARCHAR(255)";
				
			}
			else if (columnType == int.class)
			{
				preparedAddition = preparedAddition + " INT(5)";
			}
			else if (columnType == double.class)
			{
				preparedAddition = preparedAddition + " DOUBLE(5, 2)";
			}
			else if (columnType == Date.class)
			{
				preparedAddition = preparedAddition + " DATETIME()";
			}
			else
			{
				preparedAddition = preparedAddition + " TEXT";
			}
			
			if (insideTheInsert.length() > 0)
			{
				preparedAddition = preparedAddition + " , ";
			}
			
			insideTheInsert = preparedAddition + insideTheInsert;
	
		}
		
		createTableSQL = createTableSQL + insideTheInsert + ")";
		PreparedStatement thePreparedStatement  = theConnection.prepareStatement(createTableSQL);
	    thePreparedStatement.executeUpdate();
		
		return 1;
	}
	
	/**
	 * Private dropTable
	 * @param TableName
	 * @return
	 * @throws SQLException
	 */
	private int dropTable(String TableName) throws SQLException
	{
		PreparedStatement thePreparedStatement  = theConnection.prepareStatement("DROP TABLE " + TableName);
		thePreparedStatement.executeUpdate();
		return 1;
	}
	
	/**
	 * 
	 * @param TableName TableName
	 * @param Password Password to get the drop table
	 * @return
	 * @throws SQLException
	 */
	public int DropTable(String TableName, String Password) throws SQLException
	{
		if (Password == "POS")
		{
			return dropTable(TableName);
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
	
