package da;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import beans.DBConfig;
import beans.User;
import beans.Item;
import beans.Store;

/**
 * @author Chris Allard
 */
public class DBManager {
	private Logger logger = Logger.getLogger(getClass().getName());
	private Connection connection = null;
	private DBProperties props = null;
	private DBConfig configs = null;
	private PreparedStatement statement = null;
	private ResultSet results = null;

	public DBManager() {
		props = new DBProperties();
		configs = props.loadProperties();
		checkCreateTables();
	}

	public DBManager(String realPath) {
		props = new DBProperties();
		props.setRealPath(realPath);
		configs = props.loadProperties();
		checkCreateTables();
	}

	private void unitTestConnect() {
		try {
			String url = "jdbc:mysql://" + configs.getServer() + ":"
					+ configs.getPort() + "/" + configs.getDatabase();
			String user = configs.getLogin();
			String password = configs.getPassword();
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to connect to database using configurations.", e.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error!", e.getMessage());
		}
	}

	private void connect() {
		try {
			// Obtain environment naming context
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			// Look up data source
			DataSource ds = (DataSource) envCtx.lookup("jdbc/g2w13");
			// Allocate and use a connection from the pool
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to connect to database! using pooling.", e.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error!", e.getMessage());
		}
	}

	public void openConnection() {
		connect();
	}

	private void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to close connection to database!",
					e);
		}
	}

	private void checkCreateTables() {
		Statement statement = null;
		ResultSet tables;
		DatabaseMetaData meta;
		// To add new table, add table name to this array, then table code in
		// switch.
		String[] tableNames = { "items", "stores", "ursers", "user_store",
				"user_item" };
		String tableCode = "";
		boolean exists = false;
		for (String tableName : tableNames) {

			try {
				openConnection();
				meta = connection.getMetaData();
				tables = meta.getTables(null, null, tableName, null);
				exists = tables.next();
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Error while checking table existence.", e.getMessage());
			}

			// Outside of try-catch b/c what's inside doesn't get optimised.
			if (exists) {
				switch (tableName) {
				case "items":
					tableCode = "create table items("
							+ "item_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
							+ "name VARCHAR(150) NOT NULL)";
					return;
				case "stores":
					tableCode = "create table stores("
							+ "store_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
							+ "name VARCHAR(25) NOT NULL UNIQUE,"
							+ "address VARCHAR(50)," + "city VARCHAR(50),"
							+ "province_state VARCHAR(25),"
							+ "country VARCHAR(2)," + "postal_code VARCHAR(6),"
							+ "currency VARCHAR(3))";
					return;
				case "users":
					tableCode = "create table users("
							+ "user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
							+ "username VARCHAR(25) NOT NULL UNIQUE,"
							+ "password VARCHAR(25) NOT NULL,"
							+ "title VARCHAR(25)," + "first_name VARCHAR(20),"
							+ "last_name VARCHAR(20)," + "address VARCHAR(50),"
							+ "city VARCHAR(50),"
							+ "province_state VARCHAR(25),"
							+ "country VARCHAR(2)," + "postal_code VARCHAR(6),"
							+ "phone_number VARCHAR(12),"
							+ "email_address VARCHAR(45) UNIQUE)";
					return;
				case "user_store":
					tableCode = "create table user_store("
							+ "user_id INT(11),"
							+ "store_id INT(11),"
							+ "role VARCHAR(25),"
							+ "is_active BOOLEAN,"
							+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
							+ "FOREIGN KEY (store_id) REFERENCES stores(store_id))";
					return;
				case "user_item":
					tableCode = "create table store_item("
							+ "store_id INT(11),"
							+ "item_id INT(11),"
							+ "quantity INT(11),"
							+ "cost_price DECIMAL(5,2) NOT NULL,"
							+ "list_price DECIMAL(5,2) NOT NULL,"
							+ "sale_price DECIMAL(5,2) DEFAULT 0,"
							+ "PRIMARY KEY (store_id, item_id),"
							+ "FOREIGN KEY (store_id) REFERENCES stores(store_id),"
							+ "FOREIGN KEY (item_id) REFERENCES items(item_id))";
					return;
				}

				try {
					statement = connection.createStatement();
					statement.execute(tableCode);
					statement.close();
				} catch (SQLException e) {
					logger.log(Level.SEVERE, "Error creating database table",	e);
				} finally {
					try {
						statement.close();
					} catch (SQLException e) {
						logger.log(Level.SEVERE, "Error closing table creation statement.",	e);
					}

					try {
						close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Error closing table creation connection.",	e);
					}
				}
			}
		}
	}

	// INSERTs

	public boolean insertUser(User user) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection.prepareStatement("INSERT INTO users "
					+ "(user_id, username, password, title,"
					+ " first_name, last_name, address,"
					+ " city, province_state, country, postal_code,"
					+ " phone_number, email_address)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");

			statement.setString(1, null);
			statement.setString(2, user.getUsername());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getTitle());
			statement.setString(5, user.getFirst_name());
			statement.setString(6, user.getLast_name());
			statement.setString(7, user.getAddress());
			statement.setString(8, user.getCity());
			statement.setString(9, user.getProvinceOrState());
			statement.setString(10, user.getCountry());
			statement.setString(11, user.getPostalCode());
			statement.setString(12, user.getPhoneNumber());
			statement.setString(13, user.getEmailAddress());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error inserting user.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after inserting user.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean insertStore(Store store) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection.prepareStatement("INSERT INTO stores "
					+ "(store_id, name, address, city,"
					+ " province_state, country, postal_code,"
					+ " currency) VALUES (?,?,?,?,?,?,?,?)");

			statement.setString(1, null);
			statement.setString(2, store.getName());
			statement.setString(3, store.getAddress());
			statement.setString(4, store.getCity());
			statement.setString(5, store.getProvinceOrState());
			statement.setString(6, store.getCountry());
			statement.setString(7, store.getPostalCode());
			statement.setString(8, store.getCurrency());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error inserting store.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after inserting store.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean insertItem(Item item) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection
					.prepareStatement("INSERT INTO items (item_id, name) VALUES (?,?)");
			statement.setString(1, null);
			statement.setString(2, item.getName());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error inserting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after inserting item.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	// SELECTs

	public ArrayList<String> getUsers() {
		ArrayList<String> users = new ArrayList<String>();
		Statement statement = null;
		String query = "SELECT * FROM users";

		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				users.add(results.getString(1));
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error retrieving users.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after retrieving users.", e.getMessage());
			}
			if (connection != null)
				close();
		}

		return users;
	}

	public ArrayList<String> getStores() {
		ArrayList<String> stores = new ArrayList<String>();
		Statement statement = null;
		String query = "SELECT * FROM stores";

		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				stores.add(results.getString(1));
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error retrieving stores.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after retrieving stores.", e.getMessage());
			}
			if (connection != null)
				close();
		}

		return stores;
	}

	public ArrayList<String> getItems() {
		ArrayList<String> items = new ArrayList<String>();
		Statement statement = null;
		String query = "SELECT * FROM users";

		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				items.add(results.getString(1));
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error retrieving items.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after retrieving items.", e.getMessage());
			}
			if (connection != null)
				close();
		}

		return items;
	}

	// UPDATEs

	public boolean updateUser(User user) {
		boolean valid = true;
		try {
			String query = "UPDATE users " + "SET username='"
					+ user.getUsername() + "', password='" + user.getPassword()
					+ "', title='" + user.getTitle() + "', first_name='"
					+ user.getFirst_name() + "', last_name='"
					+ user.getLast_name() + "', address='" + user.getAddress()
					+ "', city='" + user.getCity() + "', province_state='"
					+ user.getProvinceOrState() + "', country='"
					+ user.getCountry() + "', postal_code='"
					+ user.getPostalCode() + "', phone_number='"
					+ user.getPhoneNumber() + "', email_address='"
					+ user.getEmailAddress() + "' WHERE user_id="
					+ user.getId();
			openConnection();
			statement = connection.prepareStatement(query);
			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating user.",
					e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after updating user.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean updateItem(Item item) {
		boolean valid = true;
		try {
			String query = "UPDATE items " + "SET name='" + item.getName()
					+ "' WHERE item_id=" + item.getId();
			openConnection();
			statement = connection.prepareStatement(query);
			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating item.",
					e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after updating item.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean updateStore(Store store) {
		boolean valid = true;
		try {
			String query = "UPDATE stores " + "SET name='" + store.getName()
					+ "', address='" + store.getAddress() + "', city='"
					+ store.getCity() + "', province_state='"
					+ store.getProvinceOrState() + "', country='"
					+ store.getCountry() + "', postal_code='"
					+ store.getPostalCode() + "', currency='"
					+ store.getCurrency() + "' WHERE store_id=" + store.getId();
			openConnection();
			statement = connection.prepareStatement(query);
			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating store.",
					e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after updating store.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	// DELETEs

	public boolean deleteItem(Item item) {
		boolean valid = true;
		String query = "DELETE FROM items WHERE item_id=" + item.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error deleting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after deleting item.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean deleteStore(Store store) {
		boolean valid = true;
		String query = "DELETE FROM stores WHERE store_id=" + store.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unexpected error deleting store.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after deleting store.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean deleteUser(User user) {
		boolean valid = true;
		String query = "DELETE FROM users WHERE user_id=" + user.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error deleting user.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Error closing statement after deleting user.", e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}
}