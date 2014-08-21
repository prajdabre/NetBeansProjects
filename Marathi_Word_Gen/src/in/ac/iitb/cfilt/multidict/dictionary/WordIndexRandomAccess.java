/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : WordIndexRandomAccess.java
 *
 * Created On: Dec 16, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.multidict.dictionary;

import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.UTFConsole;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * <p><b>Class</b>	: WordIndexRandomAccess
 * <p><b>Purpose</b>	: This class is used to access the dictionary in a random order.
 * 						  There will be a separate dictionary for each language, POS pair.
 */
public class WordIndexRandomAccess {

	/**
	 * This field stores a handle to a RandomAccessFile. 
	 */
	private RandomAccessFile file = null;
	
	private String fileName = null;
	/**
	 * <b>Constructor</b>
	 * @param fileName
	 * @param mode
	 * @throws MultilingualDictException
	 */
	public WordIndexRandomAccess(String fileName, String mode) throws MultilingualDictException {
		try {
			file = 	new RandomAccessFile(fileName, mode);
			this.fileName = fileName;
		} catch (FileNotFoundException e) {
			throw new MultilingualDictException(e);
		}
	}
	
	/**
	 * <p><b>Method</b> 	: getRecordIds
	 * <p><b>Purpose</b>	: Returns the IDs of the DSFRecords to which the lemma belongs.  
	 * <p><b>@param lemma
	 * <p><b>@param pos
	 * <p><b>@param language
	 * <p><b>@return</b>
	 * @throws MultilingualDictException 
	 */
	public String[] getRecordIds(String lemma) throws MultilingualDictException {
		String[] ids = null;
		//long startTime = System.currentTimeMillis();
		synchronized (file) {
			try {
				if (file == null || file.length() == 0) {
					return null;
				}
				long start = 0;
				long stop = file.length();
				//System.out.println(stop);
				long offset = start, midpoint;
				int compare;
				String[] tokens;
				while (true) {
					midpoint = (start + stop) / 2 ;
					//System.out.println(midpoint);
					file.seek(midpoint);
					file.readLine();
					//UTFConsole.out.println(file.readLine());
					offset = file.getFilePointer();
					if (stop == offset) {
						file.seek(start);
						offset = file.getFilePointer();
						while (offset != stop) {
							tokens = readWordLine();
							//UTFConsole.out.println(tokens[0]);
							if (tokens[0].equals(lemma)) {
								ids = new String[tokens.length -1];
								for (int i=0; i< ids.length; i++) {
									ids[i] = tokens[i+1];
								}
								return ids;
							} else {
								//file.readLine();
								offset = file.getFilePointer();
							}
						}
						break;
					}
					tokens = readWordLine();
					//UTFConsole.out.println(tokens);
					compare = tokens[0].compareTo(lemma);
					//UTFConsole.out.println(tokens[0] + compare);
					if (compare == 0) {
						//System.out.println("Found" + tokens.length);
						ids = new String[tokens.length -1];
						for (int i=0; i< ids.length; i++) {
							ids[i] = tokens[i+1];
						}
						return ids;
					} else if (compare > 0) {
						stop = offset;
					} else {
						start = offset;
					}
				}
			//System.out.println("Random Access time = " + (System.currentTimeMillis() - startTime));
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
		return ids;
	}
	
	/**
	 * <p><b>Method</b> 	: readWordLine
	 * <p><b>Purpose</b>	: Reads a complete line.
	 * <p><b>@return
	 * <p><b>@throws IOException</b>
	 */
	private String[] readWordLine() throws IOException {
		byte b;
		Vector<Byte> vBytes = new Vector<Byte>();
		try {
			while (((b = file.readByte()) != -1) && b != '\n') {
				vBytes.add(new Byte(b));
			}
		} catch (EOFException ex) {
			//ignore
		}
		byte arrBytes[] = new byte[vBytes.size()];
		for(int i=0; i<vBytes.size(); i++) {
			arrBytes[i] = vBytes.elementAt(i).byteValue();
		}
		String line = new String(arrBytes, "UTF-8");
		return line.split(" ");
	}
	
	/**
	 * <p><b>Method</b> 	: close
	 * <p><b>Purpose</b>	: Closes the Dictionary File. 
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void close() throws MultilingualDictException {
		try {
			if(file != null)
				file.close();
		} catch (IOException e) {
			throw new MultilingualDictException(e);
		}
	}
	
	/**
	 * <p><b>Method</b> 	: main
	 * <p><b>Purpose</b>	: Dummy main for testing this class. 
	 * <p><b>@param args</b>
	 * @throws MultilingualDictException 
	 */
	public static void main(String[] args) throws MultilingualDictException {
		WordIndexRandomAccess dict 
		= new WordIndexRandomAccess("./database/ENG/noun.idx", "rw");
		String[] ids = dict.getRecordIds("duck's_egg");
		if(ids == null) {
			UTFConsole.out.println("Word not found in dictionary");
			return;
		}
		for(int i=0; i<ids.length; i++) {
			UTFConsole.out.println("ids[" + (i+1) + "] = " + ids[i]);
		}
	}
}
