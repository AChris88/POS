package da;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import beans.DBConfigBean;

public class DBProperties {
	private String propFileName;
	private Properties prop = null;

	/**
	 * Constructor
	 */
	public DBProperties() {
		super();
		propFileName = "src/DBConfig.properties";
		prop = new Properties();
	}
	
	public void setRealPath(String path){
		propFileName = path;
	}

	public DBConfigBean loadProperties() {

		DBConfigBean dbConfig = new DBConfigBean();
		FileInputStream propFileStream = null;
		File propFile = new File(propFileName);

		if (propFile.exists()) {
			try {
				propFileStream = new FileInputStream(propFile);
				prop.load(propFileStream);
				propFileStream.close();
				
				// Store the properties in a DBConfigBean
				dbConfig.setServer(prop.getProperty("server"));
				dbConfig.setPort(Integer.parseInt(prop.getProperty("port")));
				dbConfig.setDatabase(prop.getProperty("database"));
				dbConfig.setLogin(prop.getProperty("login"));
				dbConfig.setPassword(prop.getProperty("password"));
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"The properties file has not been found.",
						"Missing Properties File", JOptionPane.ERROR_MESSAGE);
				dbConfig = null;
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"There was an error reading the Properties file.",
						"Properties File Read Error", JOptionPane.ERROR_MESSAGE);
				dbConfig = null;
				e.printStackTrace();
			}
		} else
			dbConfig = null;

		return dbConfig;
	}

	public boolean writeProperties(DBConfigBean dbConfigData) {
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
			prop.store(propFileStream, "-- Database Properties --");
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