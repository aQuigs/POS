package cse480;

import java.sql.*;

public class MySQLUtilities{
	
	  private static String jdbcDriver = "com.mysql.jdbc.Driver";
		public String Url = "";
		public String Username  = "";
	    public String Password  = "";

		private Connection theConnection;

		public MySQLUtilities() throws SQLException, ClassNotFoundException {
	    	Class.forName(jdbcDriver); //set Java database connectivity driver
	    	theConnection = DriverManager.getConnection(Url, Username, Password);
		}

		public ResultSet SelectSQL(String SelectSQLQuery)throws SQLException {
		    PreparedStatement thePreparedStatement  = theConnection.prepareStatement(SelectSQLQuery);
		    return thePreparedStatement.executeQuery();
		}

		public int UpdateSQL(String UpdateSQLStatement)throws SQLException {
			PreparedStatement thePreparedStatement  = theConnection.prepareStatement(UpdateSQLStatement);
		    return thePreparedStatement.executeUpdate();
		}
		
		public int InsertSQL(String InsertSQLStatement)throws SQLException {
			PreparedStatement thePreparedStatement  = theConnection.prepareStatement(InsertSQLStatement, Statement.RETURN_GENERATED_KEYS);
			thePreparedStatement.executeUpdate();
			ResultSet theResultSet = thePreparedStatement.getGeneratedKeys();
			if(theResultSet.next()) {
				return theResultSet.getInt(1);
			} else {
				return 0;
			}
		}
		
		public void close() {
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
		protected void finalize()
		{
			close();
		}
	
}
	