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

import beans.DBConfigBean;
import beans.Item;
import beans.Store;
import beans.StoreItem;
import beans.User;
import beans.UserItem;
import beans.UserStore;

/**
 * @author Chris Allard
 */
public class DBManager {
	private Logger logger = Logger.getLogger(getClass().getName());
	private Connection connection = null;
	private DBProperties props = null;
	private DBConfigBean configs = null;
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
			logger.log(Level.SEVERE,
					"Unable to connect to database using configurations.",
					e.getMessage());
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
			DataSource ds = (DataSource) envCtx.lookup("jdbc/pos");
			// Allocate and use a connection from the pool
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Unable to connect to database! using pooling.",
					e.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error!", e.getMessage());
		}
	}

	public void openConnection() {
		// connect();
		unitTestConnect();
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
		String[] tableNames = { "items", "stores", "users", "store_items",
				"user_stores", "user_items" };
		String tableCode = "";
		boolean exists = false;
		for (String tableName : tableNames) {
			try {
				openConnection();
				meta = connection.getMetaData();
				tables = meta.getTables(null, null, tableName, null);
				exists = tables.next();
			} catch (SQLException e) {
				logger.log(Level.SEVERE,
						"Error while checking table existence.", e.getMessage());
			}

			// Outside of try-catch b/c what's inside doesn't get optimised.
			if (!exists) {
				switch (tableName) {
				case "items":
					tableCode = "create table items("
							+ "item_id INT AUTO_INCREMENT PRIMARY KEY,"
							+ "name VARCHAR(150) NOT NULL,"
							+ "cost_price FLOAT(3,2) NOT NULL,"
							+ "list_price FLOAT(3,2) NOT NULL,"
							+ "sale_price FLOAT(3,2) NOT NULL,"
							+ "active INT(1) NOT NULL)";
					break;
				case "stores":
					tableCode = "create table stores("
							+ "store_id INT AUTO_INCREMENT PRIMARY KEY,"
							+ "name VARCHAR(25) NOT NULL UNIQUE,"
							+ "address VARCHAR(50)," + "city VARCHAR(50),"
							+ "province_state VARCHAR(25),"
							+ "country VARCHAR(2)," + "postal_code VARCHAR(6),"
							+ "currency VARCHAR(3),"
							+ "active INT(1) NOT NULL)";
					break;
				case "users":
					tableCode = "create table users("
							+ "user_id INT AUTO_INCREMENT PRIMARY KEY,"
							+ "username VARCHAR(25) NOT NULL UNIQUE,"
							+ "password VARCHAR(25) NOT NULL,"
							+ "title VARCHAR(25)," + "first_name VARCHAR(20),"
							+ "last_name VARCHAR(20)," + "address VARCHAR(50),"
							+ "city VARCHAR(50),"
							+ "province_state VARCHAR(25),"
							+ "country VARCHAR(2)," + "postal_code VARCHAR(6),"
							+ "phone_number VARCHAR(12),"
							+ "email_address VARCHAR(45) UNIQUE,"
							+ "active INT(1) NOT NULL)";
					break;
				case "user_stores":
					tableCode = "create table user_stores("
							+ "user_id INT(11),"
							+ "store_id INT(11),"
							+ "role VARCHAR(25),"
							+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
							+ "FOREIGN KEY (store_id) REFERENCES stores(store_id))";
					break;
				case "store_items":
					tableCode = "create table store_items("
							+ "store_id INT(11),"
							+ "item_id INT(11),"
							+ "quantity INT(11),"
							+ "cost_price DECIMAL(5,2) NOT NULL,"
							+ "list_price DECIMAL(5,2) NOT NULL,"
							+ "sale_price DECIMAL(5,2) DEFAULT 0,"
							+ "FOREIGN KEY (store_id) REFERENCES stores(store_id),"
							+ "FOREIGN KEY (item_id) REFERENCES items(item_id))";
					break;
				case "user_items":
					tableCode = "create table user_items("
							+ "user_id INT(11),"
							+ "store_id INT(11),"
							+ "item_id INT(11),"
							+ "FOREIGN KEY (user_id) REFERENCES users(user_id),"
							+ "FOREIGN KEY (store_id) REFERENCES stores(store_id),"
							+ "FOREIGN KEY (item_id) REFERENCES items(item_id))";
				}

				try {
					statement = connection.createStatement();
					statement.execute(tableCode);
					statement.close();
				} catch (SQLException e) {
					logger.log(Level.SEVERE, "Error creating database table", e);
				} finally {
					try {
						statement.close();
					} catch (SQLException e) {
						logger.log(Level.SEVERE,
								"Error closing table creation statement.", e);
					}

					try {
						close();
					} catch (Exception e) {
						logger.log(Level.SEVERE,
								"Error closing table creation connection.", e);
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
					+ " phone_number, email_address, active)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
			statement.setInt(14, user.getActive());

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
				logger.log(Level.WARNING,
						"Error closing statement after inserting user.",
						e.getMessage());
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
					+ " currency, active) VALUES (?,?,?,?,?,?,?,?,?)");

			statement.setString(1, null);
			statement.setString(2, store.getName());
			statement.setString(3, store.getAddress());
			statement.setString(4, store.getCity());
			statement.setString(5, store.getProvinceOrState());
			statement.setString(6, store.getCountry());
			statement.setString(7, store.getPostalCode());
			statement.setString(8, store.getCurrency());
			statement.setInt(9, store.getActive());

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
				logger.log(Level.WARNING,
						"Error closing statement after inserting store.",
						e.getMessage());
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
					.prepareStatement("INSERT INTO items (item_id, name, cost_price, list_price, sale_price, active) VALUES (?,?,?,?,?,?)");
			statement.setString(1, null);
			statement.setString(2, item.getName());
			statement.setDouble(3, item.getCost_price());
			statement.setDouble(4, item.getList_price());
			statement.setDouble(5, item.getSale_price());
			statement.setInt(6, item.getActive());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error inserting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after inserting item.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean insertStoreItem(StoreItem item) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection
					.prepareStatement("INSERT INTO store_items (store_id, item_id, quantity, cost_price, list_price, sale_price) VALUES (?,?,?,?,?,?)");
			statement.setInt(1, item.getStoreId());
			statement.setInt(2, item.getItemId());
			statement.setInt(3, item.getQuantity());
			statement.setDouble(4, item.getCostPrice());
			statement.setDouble(5, item.getListPrice());
			statement.setDouble(6, item.getSalePrice());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error inserting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after inserting item.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean insertUserStore(UserStore store) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection
					.prepareStatement("INSERT INTO user_stores (user_id, store_id, role) VALUES (?,?,?)");
			statement.setInt(1, store.getUserId());
			statement.setInt(2, store.getStoreId());
			statement.setString(3, store.getRole());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error inserting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after inserting item.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean insertUserItem(UserItem item) {
		boolean valid = true;
		try {
			openConnection();
			statement = connection
					.prepareStatement("INSERT INTO user_items (user_id, store_id, item_id) VALUES (?,?,?)");
			statement.setInt(1, item.getUserId());
			statement.setInt(2, item.getStoreId());
			statement.setInt(3, item.getUserId());

			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error inserting item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after inserting item.",
						e.getMessage());
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
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error retrieving users.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after retrieving users.",
						e.getMessage());
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
			logger.log(Level.WARNING, "Error retrieving stores.",
					e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after retrieving stores.",
						e.getMessage());
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
				logger.log(Level.WARNING,
						"Error closing statement after retrieving items.",
						e.getMessage());
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
					+ user.getEmailAddress() + "', active='" + user.getActive()
					+ "' WHERE user_id=" + user.getId();
			openConnection();
			statement = connection.prepareStatement(query);
			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating user.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after updating user.",
						e.getMessage());
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
					+ "', cost_price=" + item.getCost_price() + ", list_price="
					+ item.getList_price() + ", sale_price="
					+ item.getSale_price() + " WHERE item_id=" + item.getId();
			openConnection();
			statement = connection.prepareStatement(query);
			int records = statement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error updating item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after updating item.",
						e.getMessage());
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
			logger.log(Level.SEVERE, "Error updating store.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after updating store.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	// DELETEs

	public boolean toggleItem(Item item) {
		boolean valid = true;
		int active = 0;
		ResultSet results;
		String query = "SELECT * FROM items WHERE item_id = " + item.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			results = statement.executeQuery();

			if (results.next())
				active = Integer.parseInt(results.getString(3));

			query = "UPDATE items SET active = " + (active == 1 ? 0 : 1)
					+ " WHERE item_id =" + item.getId();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error toggling item.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after deleting item.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean toggleStore(Store store) {
		boolean valid = true;
		int active = 0;
		ResultSet results;
		String query = "SELECT * FROM stores WHERE store_id = " + store.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			results = statement.executeQuery();

			if (results.next())
				active = Integer.parseInt(results.getString(9));

			query = "UPDATE stores SET active = " + (active == 1 ? 0 : 1)
					+ " WHERE store_id =" + store.getId();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error toggling store.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after deleting store.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public boolean toggleUser(User user) {
		boolean valid = true;
		int active = 0;
		ResultSet results;
		String query = "SELECT * FROM users WHERE user_id = " + user.getId();
		try {
			openConnection();
			statement = connection.prepareStatement(query);
			results = statement.executeQuery();

			if (results.next())
				active = Integer.parseInt(results.getString(14));

			query = "UPDATE users SET active = " + (active == 1 ? 0 : 1)
					+ " WHERE user_id =" + user.getId();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.SEVERE, "Error toggling user.", e.getMessage());
			valid = false;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after deleting user.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return valid;
	}

	public ArrayList<UserStore> getUserStores(String userId) {
		ArrayList<UserStore> stores = new ArrayList<UserStore>();
		Statement statement = null;
		// Parsing userId to int as a cheap form of validation cause lazy right
		// now.
		String query = "SELECT * FROM user_stores WHERE user_id="
				+ Integer.parseInt(userId);
		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				stores.add(new UserStore(results.getInt(1), results.getInt(2),
						results.getString(3)));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.WARNING, "Error retrieving users.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(
						Level.WARNING,
						"Error closing statement after retrieving users' stores.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return stores;
	}

	public ArrayList<UserItem> getUserPurchases(String userId) {
		ArrayList<UserItem> stores = new ArrayList<UserItem>();
		Statement statement = null;
		// Parsing userId to int as a cheap form of validation cause lazy right
		// now.
		String query = "SELECT * FROM user_items WHERE user_id="
				+ Integer.parseInt(userId);
		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				stores.add(new UserItem(results.getInt(1), results.getInt(2),
						results.getInt(3)));
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error retrieving users.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after retrieving users.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return stores;
	}

	public ArrayList<StoreItem> getStoreItems(String storeId) {
		ArrayList<StoreItem> stores = new ArrayList<StoreItem>();
		Statement statement = null;
		// Parsing userId to int as a cheap form of validation cause lazy right
		// now.
		String query = "SELECT * FROM store_items WHERE store_id="
				+ Integer.parseInt(storeId);
		try {
			openConnection();
			statement = connection.createStatement();
			results = statement.executeQuery(query);
			while (results.next())
				stores.add(new StoreItem(results.getInt(1), results.getInt(2),
						results.getInt(3), results.getDouble(4), results
								.getDouble(5), results.getDouble(6)));
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error retrieving users.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after retrieving users.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
		return stores;
	}

	// FOR TESTING PURPOSES ONLY
	public void dropTables() {
		String[] queries = { "DROP TABLE user_stores",
				"DROP TABLE store_items", "DROP TABLE user_items",
				"DROP TABLE stores", "DROP TABLE items", "DROP TABLE users" };
		try {
			openConnection();
			for (int i = 0; i < queries.length; i++) {
				statement = connection.prepareStatement(queries[i]);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			logger.log(Level.SEVERE, "Error dropping tables.", e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING,
						"Error closing statement after dropping tables.",
						e.getMessage());
			}
			if (connection != null)
				close();
		}
	}
}