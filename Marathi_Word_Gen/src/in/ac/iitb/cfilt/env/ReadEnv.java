/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : ReadEnv.java
 *
 * Created On: Nov 13, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.env;

import in.ac.iitb.cfilt.common.exception.MultilingualDictException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Class	: ReadEnv
 * Purpose	: This class reads the Environment variables.
 */
public class ReadEnv {

	/**
	 * This field stores the Environment Variables.
	 */
	private static Properties envVars = new Properties();
	
	/**
	 * This field stores the initialization status.
	 */
	private static boolean INITIALIZED = false;

	/**
	 * Method 	: getEnvVars
	 * Purpose	: Gets the environment variables.
	 * @return
	 * @throws MultilingualDictException 
	 * @throws Throwable
	 */
	public static void load() throws MultilingualDictException {
		try {
			if(INITIALIZED) {
				return;
			}
			Process p = null;
			Runtime r = Runtime.getRuntime();
			String OS = System.getProperty("os.name").toLowerCase();
			// System.out.println(OS);
			if (OS.indexOf("windows 9") > -1) {
				p = r.exec("command.com /c set");
			} else if ((OS.indexOf("nt") > -1) || (OS.indexOf("windows 2000") > -1)
					|| (OS.indexOf("windows xp") > -1)) {
				// thanks to JuanFran for the xp fix!
				p = r.exec("cmd.exe /c set");
			} else {
				// our last hope, we assume Unix (thanks to H. Ware for the fix)
				p = r.exec("env");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				int idx = line.indexOf('=');
				String key = line.substring(0, idx);
				String value = line.substring(idx + 1);
				envVars.setProperty(key, value);
				//System.out.println( key + " = " + value );
			}
			INITIALIZED = true;
		} catch (IOException ex) {
			throw new MultilingualDictException(ex);
		}
	}

	/**
	 * Method 	: getProperty
	 * Purpose	: Returns the value of the Environment variable.
	 * @param envVar
	 * @return
	 */
	public static String getProperty(String envVar) {
		return envVars.getProperty(envVar);
	}
	
	public static void main(String args[]) {
		try {
			ReadEnv.load();
			System.out.println("the current value of TEMP is : "
					+ ReadEnv.getProperty("USER_HOME"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
