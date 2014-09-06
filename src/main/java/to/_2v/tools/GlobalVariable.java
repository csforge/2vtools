package to._2v.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class GlobalVariable {
	private static Properties PROPS = new Properties();
	
	public static Object get(Object key){
		return PROPS.get(key);
	}
	public static void set(Object key,Object value){
		PROPS.put(key, value);
	}
	public static void setProperty(String key,String value){
		PROPS.setProperty(key, value);
	}
	public static String getProperty(String key){
		return PROPS.getProperty(key);
	}
	public static int getIntVariable(String key){
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	public static double getDoubleVariable(String key){
		try {
			return Double.parseDouble(getProperty(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	public static boolean getBooleanVariable(String key) {
        if (getProperty(key) == null) {
            return false;
        }
        if ("1".equals(getProperty(key)) || getProperty(key).equalsIgnoreCase("true") || getProperty(key).equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
	}
	
	public static Hashtable<Object, Object> getVariables(){
		return PROPS;
	}
	public static Properties getProperties(){
		return PROPS;
	}
	public static void load(File file){
		try {
			PROPS.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void store(File file,String comments){
		try {
			PROPS.store(new FileOutputStream(file), comments);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
