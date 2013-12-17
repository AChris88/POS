package da;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import beans.DBConfig;

public class DBProperties {
	private String propFileName;
	private Properties prop = null;

	/**
	 * Constructor
	 */
	public DBProperties() {
		super();
		this.propFileName = "WebContent/DBConfig.properties";
		//this.propFileName = "/DBConfig.properties";
		prop = new Properties();
	}
	
	public void setRealPath(String path){
		this.propFileName = path;
	}

	public DBConfig loadProperties() {

		DBConfig dbConfigData = new DBConfig();
		FileInputStream propFileStream = null;
		File propFile = new File(propFileName);

		if (propFile.exists()) {
			try {
				propFileStream = new FileInputStream(propFile);
				prop.load(propFileStream);
				propFileStream.close();
				
				// Store the properties in a mailConfigData object
				dbConfigData.setServer(prop.getProperty("server"));
				dbConfigData.setPort(Integer.parseInt(prop.getProperty("port")));
				dbConfigData.setDatabase(prop.getProperty("database"));
				dbConfigData.setLogin(prop.getProperty("login"));
				dbConfigData.setPassword(prop.getProperty("password"));
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"The properties file has not been found.",
						"Missing Properties File", JOptionPane.ERROR_MESSAGE);
				dbConfigData = null;
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"There was an error reading the Properties file.",
						"Properties File Read Error", JOptionPane.ERROR_MESSAGE);
				dbConfigData = null;
				e.printStackTrace();
			}
		} else
			dbConfigData = null;

		return dbConfigData;
	}

	public boolean writeProperties(DBConfig dbConfigData) {
		boolean retVal = true;

		prop.setProperty("server", dbConfigData.getServer());
		prop.setProperty("port", ""+dbConfigData.getPort());
		prop.setProperty("database", dbConfigData.getDatabase());
		prop.setProperty("login", dbConfigData.getLogin());
		prop.setProperty("password", dbConfigData.getPassword());
		
		FileOutputStream propFileStream = null;
		File propFile = new File(propFileName);
		try {
			propFileStream = new FileOutputStream(propFile);
			prop.store(propFileStream, "-- MailConfig Properties --");
			propFileStream.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"The properties file has not been found.",
					"Missing Properties File", JOptionPane.ERROR_MESSAGE);
			retVal = false;
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"There was an error writing the Properties file.",
					"Properties File Write Error", JOptionPane.ERROR_MESSAGE);
			retVal = false;
			e.printStackTrace();
		}
		return retVal;
	}

	public void displayProperties() {
		prop.list(System.out);
	}
}