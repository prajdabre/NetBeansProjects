/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DSFWriter.java
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.io;


import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.AlignmentRecord;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * Class	: DSFWriter
 * Purpose	: This class handles all file output operations.
 *            
 */
public class DSFWriter {
	
	/**
	 * This field stores a handle to the output file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a boolean value to indicate whether
	 * to open the file in append mode or not.
	 */
	private boolean bAppend = false;

	/**
	 * This field stores a handle to a BufferedWriter
	 */
	private BufferedWriter objBufWriter = null;

	/**
	 * Constructor
	 * @param objFile
	 */
	public DSFWriter(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public DSFWriter(String strFileName) {
		this.objFile = new File(strFileName);
	}

	/**
	 * Constructor
	 * @param strFileName
	 * @param bAppend
	 */
	public DSFWriter(String strFileName, boolean bAppend) {
		this.objFile = new File(strFileName);
		this.bAppend = bAppend;
	}

	/**
	 * Constructor
	 * @param objFile
	 * @param bAppend
	 */
	public DSFWriter(File objFile, boolean bAppend) {
		this.objFile = objFile;
		this.bAppend = bAppend;
	}

    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     */
	public void open() throws MultilingualDictException {
		try {
			this.objBufWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(objFile, bAppend), "UTF8"), 4096);
		} catch (IOException ex) {
			throw new MultilingualDictException(
					"Error while opening the file " +
					objFile.getAbsolutePath()
					, ex);
		}
	}

	/**
	 * Method 	: writeEntryToFile
	 * Purpose	: Writes an DSF entry to the specified file. The boolean flag determines if
	 *  the synset-id is zero-padded (as they are in the English WordNet) 
	 * @param record
	 * @param langDescription
	 * @param outputFile
	 * @return
	 * @throws IOException
	 */
	public DSFRecord writeEntryToFile(DSFRecord record,
			boolean zeroPadding) throws MultilingualDictException {
		try {
			objBufWriter.write(GlobalConstants.FIELD_NAMES[0]+"\t\t:: " + record.getID() + " \n");
			objBufWriter.write(GlobalConstants.FIELD_NAMES[1]+"\t\t::  " + record.getCategory() + " \n");
			objBufWriter.write(GlobalConstants.FIELD_NAMES[2]+"\t\t::  " + record.getConcept().replaceAll("\n", " ") + " \n");
			objBufWriter.write(GlobalConstants.FIELD_NAMES[3]+"\t\t::  " + record.getExample().replaceAll("\n", " ") + " \n");
			objBufWriter.write(GlobalConstants.FIELD_NAMES[4]+record.getLanguage() + "\t:: " + getAlignmentInfo(record) + " \n");
			objBufWriter.write("\n");
			objBufWriter.flush();
			
		} catch (IOException e) {
			throw new MultilingualDictException("Error while writing DSFRecord", e);
		}
		
		return record;
	}

	/**
	 * Method 	: getAlignmentInfo
	 * Purpose	: Appends the alignment info for words.  
	 * @param record
	 * @return  - Words with the alignment info.
	 */
	private String getAlignmentInfo(DSFRecord record) {
		Vector<AlignmentRecord> vAlignmentRecords = record.getAlignmentRecords();
		if(vAlignmentRecords == null || vAlignmentRecords.size() == 0) {
			return record.getWordsAsAString().replaceAll("\n", "");
		}
		Vector<String> vWords = record.getWords();
		StringBuilder sb = new StringBuilder();
		int iSize = vWords.size();
		boolean bAlignmentFound = false;
		for(int i=0; i<iSize; i++) {
			bAlignmentFound = false;
			for(int j=0; j<vAlignmentRecords.size(); j++) {
				if(vAlignmentRecords.elementAt(j).getTargetWord()
						.equals(vWords.elementAt(i))) {
					sb.append(vWords.elementAt(i));
					sb.append("/");
					sb.append(vAlignmentRecords.elementAt(j).getSourceWord());
					sb.append(", ");
					bAlignmentFound = true;
					break;
				}
			}
			if(!bAlignmentFound) {
				sb.append(vWords.elementAt(i));
				sb.append(", ");
			}
		}
		if(sb.length() > 1) {
			sb.deleteCharAt(sb.length() -1); //delete last space.
			sb.deleteCharAt(sb.length() -1); //delete last comma.
		}
		return sb.toString().replaceAll("\n", "");
	}
	
    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     */
	public void close() throws MultilingualDictException {
		try {
			if(this.objBufWriter != null) {
				this.objBufWriter.close();
			}
		} catch (IOException ex) {
			throw new MultilingualDictException(
					"Error while closing the file " +
					objFile.getAbsolutePath() +
					"\nData may not have been written corectly."
					, ex);
		}
	}

	/**
	 * Method 	: getBufWriter
	 * Purpose	: Returns the objBufWriter.
	 * @return  : objBufWriter
	 */
	public BufferedWriter getBufWriter() {
		return objBufWriter;
	}

	/**
	 * Method 	: setBufWriter
	 * Purpose	: Sets the value of objBufWriter.
	 * @param 	: bufWriter
	 */
	public void setBufWriter(BufferedWriter bufWriter) {
		objBufWriter = bufWriter;
	}
}
