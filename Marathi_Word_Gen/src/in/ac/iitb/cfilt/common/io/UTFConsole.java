/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : UTFConsole.java
 *
 * Created On: Aug 28, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.io;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Class	: UTFConsole
 * Purpose	: This class provides the the UTF-8 version of system console.
 */
public class UTFConsole {

	/**
	 * This field stores the UTF-8 version of system console.
	 */
	public static PrintStream out = null;

	/**
	 * This field stores the UTF-8 version of system error.
	 */
	public static PrintStream err = null;

	static{
		try {
			out = new PrintStream(System.out, true, "UTF8");
			err = new PrintStream(System.err, true, "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
