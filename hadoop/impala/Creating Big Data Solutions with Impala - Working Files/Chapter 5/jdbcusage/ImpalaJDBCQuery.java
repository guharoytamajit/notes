package jdbcusage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImpalaJDBCQuery {
	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;

		try {
			// Register the Hive (Impala) JDBC driver
			Class.forName("org.apache.hive.jdbc.HiveDriver");

			// Open the connection
			connection = DriverManager.getConnection("jdbc:hive2://localhost:21050/;auth=noSasl");

			// Execute the query
			statement = connection.createStatement();
			String sqlQuery = "SELECT card, suit FROM cards LIMIT 10";
			ResultSet rs = statement.executeQuery(sqlQuery);

			// Iterate through the result set
			while (rs.next()) {
				// Retrieve by column name
				String card = rs.getString("card");
				String suit = rs.getString("suit");

				// Do something
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
