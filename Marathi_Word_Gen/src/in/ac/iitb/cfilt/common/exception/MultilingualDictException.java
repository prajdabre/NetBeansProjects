/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : MultiDictException.java
 *
 * Created On: Aug 21, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.exception;

/**
 * Class	: MultiDictException
 * Purpose	: This class is the exception class for the application. 
 */
public class MultilingualDictException extends Exception {

	/**
	 * This field stores the default serialVersionUID. 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public MultilingualDictException() {
	}

	/**
	 * Constructor
	 * @param arg0
	 */
	public MultilingualDictException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * @param arg0
	 */
	public MultilingualDictException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * @param arg0
	 * @param arg1
	 */
	public MultilingualDictException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
