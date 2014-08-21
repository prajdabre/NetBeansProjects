/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : SynsetRandomAccess.java
 *
 * Created On: Dec 16, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.multidict.dictionary;

import in.ac.iitb.cfilt.common.config.AppProperties;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.UTFConsole;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * <p><b>Class</b>	: SynsetRandomAccess
 * <p><b>Purpose</b>	: This class is 
 */
public class SynsetRandomAccess {

	/**
	 * This field stores a handle to a RandomAccessFile. 
	 */
	private RandomAccessFile file = null;
	
	private String fileName = null;
	/**
	 * This field stores a boolean value to indicate that the EOF has been reached.
	 */
	private boolean EOF = false;
	/**
	 * <b>Constructor</b>
	 * @param fileName
	 * @param mode
	 * @throws MultilingualDictException
	 */
	public SynsetRandomAccess(String fileName, String mode) throws MultilingualDictException {
		try {
			file = 	new RandomAccessFile(fileName, mode);
			this.fileName = fileName;
			//System.out.println(fileName);
		} catch (FileNotFoundException e) {
			throw new MultilingualDictException(e);
		}
	}
	
	/**
	 * <p><b>Method</b> 	: getRecord
	 * <p><b>Purpose</b>	: Returns the DSFRecords having the given ID.  
	 * <p><b>@param synsetId
	 * <p><b>@return
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public DSFRecord getRecord(String synsetId, boolean keepLinkageInfo) throws MultilingualDictException {
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
				String[] tokens = null;
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
							tokens = readSynsetLine();
							//UTFConsole.out.println(tokens[0]);
							if (tokens[0].equals(synsetId)) {
								return constructDSFRecord(tokens, keepLinkageInfo);
							} else {
								//file.readLine();
								offset = file.getFilePointer();
							}
							if(EOF) {
								offset = stop;
							}
						}
						break;
					}
					tokens = readSynsetLine();
					//UTFConsole.out.println(fileName + " " + tokens[0]);
					compare = tokens[0].compareTo(synsetId);
					if (compare == 0) {
						//System.out.println("Found" + tokens.length);
						return constructDSFRecord(tokens, keepLinkageInfo);
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
		return null;
	}

	
	/**
	 * <p><b>Method</b> 	: readSynsetLine
	 * <p><b>Purpose</b>	: Reads a complete line corresponding to a synset.
	 * <p><b>@return
	 * <p><b>@throws IOException</b>
	 */
	private String[] readSynsetLine() throws IOException {
		byte b;
		Vector<Byte> vBytes = new Vector<Byte>();
		try {
			while ((b = file.readByte()) != '\n') {
				vBytes.add(new Byte(b));
			}
		} catch (EOFException ex) {
			//ex.printStackTrace();
			EOF = true;
		}
		byte arrBytes[] = new byte[vBytes.size()];
		for(int i=0; i<vBytes.size(); i++) {
			arrBytes[i] = vBytes.elementAt(i).byteValue();
		}
		String line = new String(arrBytes, "UTF-8");
		//UTFConsole.out.println(line);
		return line.split(":~:");
	}
	
	/**
	 * <p><b>Method</b> 	: constructDSFRecord
	 * <p><b>Purpose</b>	: Constructs a DSFRecord from the tokens read from 
	 * 						  the file.  
	 * <p><b>@param tokens
	 * <p><b>@return</b>
	 */
	private DSFRecord constructDSFRecord(String[] tokens, boolean keepLinkageInfo) {
		DSFRecord record = new DSFRecord();
		record.setID(tokens[0]);			
		record.setCategory(tokens[1]);
		record.setConcept(tokens[2]);
		record.setExample(tokens[3]);
		if(keepLinkageInfo) {
			record.setWordsAsAString(tokens[4]);
		} else {
			record.setWordsAsAString(tokens[4].replaceAll("/HW[0-9]*", ""));
		}
		return record;
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
	 * <p><b>Purpose</b>	: Dummy main method to test this class.
	 * <p><b>@param args</b>
	 * @throws MultilingualDictException 
	 */
	public static void main(String[] args) throws MultilingualDictException {
		AppProperties.load("./properties/MultiDict.properties");
		System.out.println((short)'\n');
		SynsetRandomAccess dict 
		= new SynsetRandomAccess("./database/HIN/data.noun", "rw");
		UTFConsole.out.println(dict.getRecord("12273", false).printInOneLine());
	}
}
