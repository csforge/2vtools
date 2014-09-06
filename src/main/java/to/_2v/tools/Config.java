package to._2v.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import to._2v.tools.util.Configurator;

public class Config {

	private static Properties PROPS = new Properties();
	private static File FILE;
	private static boolean ready = false;
	public static void configure(){
		configure("config.properties");
	}
	public static void configure(String resource){
		InputStream is = Configurator.getResourceAsStream(resource, Config.class);
		try {
			if (is != null){
				PROPS.load(is);
				ready = true;
			}
			else
				System.out.println("Cannot find the '" + resource + "' in classpath.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void configure(File file){
		setProperties(file);
	}
	public static boolean isReady() {
		return ready;
	}
	public static void setProperties(File file){
		FILE = file;
		load();
	}
	public static void load(){
		try {
			PROPS.load(new FileInputStream(FILE));
			ready = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isNotBlank(String key) {
		if (PROPS.getProperty(key) == null || PROPS.getProperty(key).equals(""))
			return false;
		return true;
	}
	public static String getProperty(String key){
		if(!ready)
			configure();
		return PROPS.getProperty(key).trim();
	}
	public static int getIntProperty(String key) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	public static double getDoubleProperty(String key) {
		try {
			return Double.parseDouble(getProperty(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	public static final boolean getBooleanProperty(String key) {
        if (getProperty(key) == null) {
            return false;
        }
        if ("1".equals(getProperty(key)) || getProperty(key).equalsIgnoreCase("true") || getProperty(key).equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }
//	public static void store(String comments){
//		try {
//			PROPS.store(new FileOutputStream(FILE), comments);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public static Properties getProperties(){
		return PROPS;
	}
}
