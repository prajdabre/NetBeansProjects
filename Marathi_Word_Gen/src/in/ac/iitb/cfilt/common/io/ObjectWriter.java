/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : ObjectWriter.java
 *
 * Created On: Oct 1, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Class	: ObjectWriter
 * Purpose	: This class is used to serialize objects.
 */
public class ObjectWriter {

	/**
	 * This field stores a handle to the output file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a handle to the object output stream. 
	 */
	private ObjectOutputStream oos = null;
	 
	 /**
	 * Constructor
	 * @param objFile
	 */
	public ObjectWriter(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public ObjectWriter(String strFileName) {
		this.objFile = new File(strFileName);
	}

    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     */
	public void open() throws IOException {		
		this.oos = new ObjectOutputStream(
			new BufferedOutputStream((new FileOutputStream(objFile)), 4096));
		
	}

    /**
     * Method 	: write
     * Purpose	: Writes an Object to the file .
	 * @param 	- o
	 */
	public void write (Object o) throws IOException {
		if(this.oos != null) {
			this.oos.writeObject(o);		
		}
	}

    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     * @throws IOException 
     */
	public void close() throws IOException {
		if(this.oos != null) {
			this.oos.close();
		}
	}
}
