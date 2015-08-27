package se.hmpaj.ecommerce.service.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class SqlConnection
{
	private static final String DB_URL = "jdbc:mysql://localhost/JiPA";

	protected Connection getConnection() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(DB_URL, "root", "");
		}
		catch (ClassNotFoundException e)
		{
			throw new SQLException("Driver not found");
		}
	}
}