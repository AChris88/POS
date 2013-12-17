package beans;

import java.io.Serializable;

public class DBConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6592513391951980738L;
	private String server;
	private int port;
	private String database;
	private String login;
	private String password;
	
	public DBConfig(){}
	
	public DBConfig(String server, int port, String database, String login,
			String password) {
		super();
		this.server = server;
		this.port = port;
		this.database = database;
		this.login = login;
		this.password = password;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}