/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : ObjectReader.java
 *
 * Created On: Oct 1, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Class	: ObjectReader
 * Purpose	: This class is used to read serilized objects.
 */
public class ObjectReader {

	/**
	 * This field stores a handle to the output file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a handle to the object output stream. 
	 */
	private ObjectInputStream ois = null;
	 
	 /**
	 * Constructor
	 * @param objFile
	 */
	public ObjectReader(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public ObjectReader(String strFileName) {
		this.objFile = new File(strFileName);
	}

    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     */
	public void open() throws IOException {		
		this.ois = new ObjectInputStream(
				new BufferedInputStream((new FileInputStream(objFile)), 4096));		
	}

    /**
     * Method 	: read
     * Purpose	: Reads an Object from the file .
     * @throws ClassNotFoundException 
	 */
	public Object read () throws IOException, ClassNotFoundException {
		if(this.ois != null) {
			return this.ois.readObject();
		}
		return null;
	}

    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     */
	public void close() throws IOException {
		if(this.ois != null) {
			this.ois.close();
		}
	}
}
