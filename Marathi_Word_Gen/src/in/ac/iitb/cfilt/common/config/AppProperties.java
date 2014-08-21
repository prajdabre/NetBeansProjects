package in.ac.iitb.cfilt.common.config;



import in.ac.iitb.cfilt.common.crypto.EncryptionException;
import in.ac.iitb.cfilt.common.crypto.StringCrypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Class	: AppProperties
 * Purpose	: This class will contain all the configuration parameters
 *            which are read from a properties/config file created by the 
 *            user.
 *            A properties/config file would contain information like:
 *            SRC Language
 *            DEST Language
 *            PATH of WN
 *            PATH of Bilingual Dictionary      
 */
public class AppProperties {

    /**
     * This field stores a handle to the Application's Properties file. 
     */
    private static LinkedHashMap<String, Properties> hmProperties = null;
    
    /**
     * This field stores a boolean value to indicate that the tool is 
     * being used for internal (IITB) purpose.  
     */
    public static boolean INTERNAL_USE = false;
    
    /**
     * Method 	: load
     * Purpose	: Loads the Application's Properties file.
     */
    public static void load(String fileNames) {
    	String files[] = fileNames.split(";");
    	for (int i=0; i<files.length; i++) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(files[i]));
                if(properties.getProperty("internal.use") != null 
                		&& properties.getProperty("internal.use").equals("true")) {
                	INTERNAL_USE = true;
                }
                if (hmProperties == null) {
                	hmProperties = new LinkedHashMap<String, Properties>();
                }
                hmProperties.put(files[i], properties);
            } catch (FileNotFoundException e) {
    			JOptionPane.showMessageDialog(null, 
    					"Error while opening Application Properties File." +
    					"\nPlease ensure that the file" +
    					files[i] +
    					" is present in the properties directory in the installation directory.",
    					"Error",
    					JOptionPane.ERROR_MESSAGE);
    			System.exit(1);
            } catch (IOException e) {
    			JOptionPane.showMessageDialog(null, 
    					"Application Properties File Not Found." +
    					"\nPlease ensure that the file" +
    					files[i] +
    					" is present in the properties directory in the installation directory.",
    					"Error",
    					JOptionPane.ERROR_MESSAGE);
    			System.exit(1);
            }
    	}
    }
    
    /**
     * Method 	: getProperty
     * Purpose  : Returns the value of strProperty.  
     * @param   - strProperty
     * @return  - The value of strProperty.
     */
    public static String getProperty(String strProperty) {
    	boolean decrypt= strProperty.contains("password");
    	Iterator<String> iterator = hmProperties.keySet().iterator();
    	while (iterator.hasNext()) {
    		String key = iterator.next();
    		if(hmProperties.get(key).containsKey(strProperty)) {
    			if(decrypt) {
    				try {
        				return (new StringCrypto()).decrypt(hmProperties.get(key).getProperty(strProperty));
    				} catch (EncryptionException ex) {
    					//ignore and return non-decrypted version.
    				}
    			}
    			return hmProperties.get(key).getProperty(strProperty);
    		}
    	}
        return null;
    }

    /**
     * Method 	: setProperty
     * Purpose  : Sets the value of strProperty.  
     * @param   - strProperty
     * @param   - strValue
     */
    public static void setProperty(String strProperty, String strValue) {
		boolean encrypt= strProperty.contains("password");
		Iterator<String> iterator = hmProperties.keySet().iterator();
		String firstKey = null;
		boolean exitLoop = false;
		do {
			String key = null;
			if(iterator.hasNext()) {
				key = iterator.next();
				if(firstKey == null) {
					firstKey = key;
				}
			} else if(firstKey != null){
				key = firstKey;
				exitLoop = true;	// Store the key in first file and exit
			} else {
				return;	// No property file !!!
			}
			
			if(hmProperties.get(key).containsKey(strProperty) || exitLoop) {
				if(encrypt) {
					try {
						hmProperties.get(key).setProperty(strProperty, new StringCrypto().encrypt(strValue));
						return;
					} catch (EncryptionException ex) {
						//ignore and return plain-text version.
					}
				} else {
					hmProperties.get(key).setProperty(strProperty, strValue);
					return;
				}
			}
		} while (! exitLoop);				
    }
    
    /**
     * Method 	: setProperty
     * Purpose  : Sets the value of strProperty in the specified filename.  
     * @param   - strProperty
     * @param   - strValue
     * @param	- strFileName
     */
    public static void setProperty(String strProperty, String strValue, String strFileName) {
    	if(hmProperties.containsKey(strFileName)) {
    		if(strProperty.contains("password")) {
    			try {
    				hmProperties.get(strFileName).setProperty(strProperty, new StringCrypto().encrypt(strValue));
    				return;
    			} catch (EncryptionException ex) {
    				//ignore and return plain-text version.
    			}
    		} else {
    			hmProperties.get(strFileName).setProperty(strProperty, strValue);
    			return;
    		}
    	}    	
    }
    
    /**
     * Method 	: store
     * Purpose	: Stores the Application's Properties file.
     */
    public static void store() {
		Iterator<String> iterator = hmProperties.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			try {
				hmProperties.get(key).store(new FileOutputStream(key), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    public static void main (String args[]) {
    	AppProperties.load("./properties/Trial.properties;./properties/WNLinker.properties");
    	//AppProperties.setProperty("database.password", "mitesh123");
    	System.out.println(AppProperties.getProperty("database.password"));
    	AppProperties.setProperty("database.password","somedb");
    	System.out.println(AppProperties.getProperty("database.password"));
    	System.out.println(AppProperties.getProperty("source.language"));
    	AppProperties.setProperty("source.language","hebrew","./properties/WNLinker.properties");
    	System.out.println(AppProperties.getProperty("source.language"));
    	AppProperties.store();
    }
}
