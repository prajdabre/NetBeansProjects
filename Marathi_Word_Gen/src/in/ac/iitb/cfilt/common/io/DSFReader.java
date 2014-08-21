/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DSFReader.java
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
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Class	: DSFReader
 * Purpose	: This class handles all file input operations.
 *            
 */
public class DSFReader {
	
	/**
	 * This field stores a handle to the input file
	 */
	private File objFile = null;
	
	/**
	 * This field stores a handle to a BufferedWriter
	 */
	private BufferedReader objBufReader = null;

	/**
	 * This field stores a boolean flag for DEBUG functionality.
	 */
	private static boolean DEBUG = true;

	private long lineNo = 0;
	
	/**
	 * Constructor
	 * @param objFile
	 */
	public DSFReader(File objFile) {
		this.objFile = objFile;
	}

	/**
	 * Constructor
	 * @param strFileName
	 */
	public DSFReader(String strFileName) {
		this.objFile = new File(strFileName);
	}

    /**
     * Method 	: open
     * Purpose	: Opens a handle to the file .
     */
	public void open() throws MultilingualDictException {
		try {
			this.objBufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(objFile), "UTF8"), 4096);
		} catch (IOException ex) {
			throw new MultilingualDictException(
					"Error while opening the file " +
					objFile.getAbsolutePath()
					, ex);
		}
	}

	/**
	 * Method 	: readEntryFromFile
	 * Purpose	: Reads a DSFRecord from the mentioned file. 
	 * @param defaultStatus
	 * @return
	 */
	public DSFRecord readEntryFromFile(String defaultStatus) throws MultilingualDictException {
		
		try {
			DSFRecord retValue = new DSFRecord();
			String[][] inputLineData = new String[5][2];	
			/* Read all 5 data lines for this entry */
			for (int i = 0; i < inputLineData.length; i++) {
				String inputLine = this.readLine();
				
				/*	Skip empty lines and comments */
				while(inputLine != null && (inputLine.trim().length() == 0 || inputLine.trim().startsWith("//"))) {	
					inputLine = this.readLine();
				}
				if(inputLine == null ) {
					if(DEBUG) System.out.println("End of file reached.");
					return null;
				}
				inputLineData[i] = inputLine.split("::");
				
				// Validate Line
				if( !(inputLineData[i][0].trim().startsWith(GlobalConstants.FIELD_NAMES[i]))) {
					if(DEBUG) System.out.println("Error: Invalid Line "+ this.lineNo +": " + inputLineData[i][0]);
					//return null;
				}
			}			
			
			/* TODO : Should we read the entry-delimiting blank line here ? */
			
			/* Store data items in structure */			
			retValue.setID( (inputLineData[0].length > 1) ? inputLineData[0][1].trim() : "");			
			retValue.setCategory((inputLineData[1].length > 1) ? inputLineData[1][1].trim() : "");
			retValue.setConcept((inputLineData[2].length > 1) ? inputLineData[2][1].trim() : "");
			retValue.setExample((inputLineData[3].length > 1) ? inputLineData[3][1].trim() : "");
//			retValue.setWordsAsAString((inputLineData[4].length > 1) ? inputLineData[4][1].trim():"");

			if(defaultStatus != null ) {
				retValue.setStatus(defaultStatus);
			} else {
				retValue.evaluateStatus();
			}
			
			String langDescription = inputLineData[4][0].trim();
			retValue.setLanguage(langDescription.substring(langDescription.indexOf('-') + 1));
			
			Vector<AlignmentRecord> alignmentRecords = new Vector<AlignmentRecord>();
			
			String[] synsetMembers = ((inputLineData[4].length > 1) ? inputLineData[4][1].trim() : "").split(GlobalConstants.SYNSET_WORD_SEPARATORS);
			Vector<String> vWords = new Vector<String>();
			for(int i=0; i< synsetMembers.length; i++)
			{
				if(synsetMembers[i].equals(""))
					continue;
				String[] mem = synsetMembers[i].split(GlobalConstants.FRONTSLASH);
				if(mem.length == 2)
				{
					vWords.add(mem[0]);
					mem[1] = mem[1].toUpperCase();
					if(mem[1].matches(Language.HINDI.toString()+"\\d+") || mem[1].matches(Language.TAMIL.toString()+"\\d+"))
					{
						AlignmentRecord record = new AlignmentRecord();
						record.setSourceWord(mem[0].trim());
						record.setTargetWordWithoutDigitRemoval(mem[1].trim());
						alignmentRecords.add(record);
					}
					else
						if(DEBUG) System.out.println("Invalid Line "+ this.lineNo +": ^" + mem[1]);
				}else if (mem.length == 1)
				{
					vWords.add(mem[0]);
					AlignmentRecord record = new AlignmentRecord();
					record.setSourceWord(mem[0].trim());
					record.setTargetWordWithoutDigitRemoval(Language.HINDI.toString()+"1");
					alignmentRecords.add(record);
				}
				else
					if(DEBUG) throw new MultilingualDictException("Invalid Line "+ this.lineNo +": ^" + synsetMembers[i]);
			}
			retValue.setWords(vWords);
			retValue.setAlignmentRecordsM(alignmentRecords);
//			retValue.setAlignmentRecords(alignmentRecords);
						
			return retValue;
		} catch (IOException e) {
			new MultilingualDictException("Error Reading DSF Entry: ",  e);
		} catch (NumberFormatException e) {
			new MultilingualDictException("Error Reading DSF Entry: ",  e);
		} catch (ArrayIndexOutOfBoundsException e) {	
			new MultilingualDictException("Error Reading DSF Entry: ",  e);
		}
		return null;
	}
	
	/*
	public static void uploadCrossLinkedDSFFile(String directoryPath) throws MultilingualDictException {
		AppProperties.setProperty("source.language.choice", "hindi");
		AppProperties.setProperty("target.language.choice", "bengali");
		AppProperties.setProperty("database.user", "multidict");
		AppProperties.setProperty("database.password", "vsfg45d");
		AppProperties.setProperty("database.dbname", "multilingual_dictionary");
		AppProperties.setProperty("database.host", "www.cfilt.iitb.ac.in");
		
		dbLayer = MiddleLayer.getSingletonInstance();
		dbLayer.initDB();
		dbLayer.initialize();
		//System.out.println("xyz/HW1, aaa/HW12".replaceAll("/HW[0-9]*", ""));

		DSFRecord inputRecord;	
		BufferedReader inputFile = null;
		int recordCount = 0;

		//Read all file and eliminate duplicates
		try {

			File directory = new File (directoryPath);

			//BufferedWriter outputFile = new BufferedWriter( new OutputStreamWriter(new FileOutputStream("out.txt"), "UTF8"), 4096);				

			if( directory.isDirectory()) {
				String[] files = directory.list();
				for (int i = 0; i < files.length; i++) {				
					lineNumber = 0;
					String prevRecordId = "";
					System.out.println("Reading file " + files[i]);
					inputFile = new BufferedReader( new InputStreamReader(new FileInputStream(directoryPath+ File.separator +files[i]), "UTF8"), 4096);
					outputFile = new BufferedWriter( new OutputStreamWriter(new FileOutputStream("output"+ File.separator + files[i]), "UTF8"), 4096);
					corruptFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("corrupt.txt"), "UTF8"), 4096);
					while((inputRecord = readEntryFromFileV1(inputFile)) != null){

						recordCount++;

						if(! inputRecord.getLanguage().equals("TELUGU")) {
							System.out.println("bad language name on line " + lineNumber);
							inputRecord.setLanguage("TELUGU");
						}

						//System.out.println(inputRecord);
						if(inputRecord.getID().equalsIgnoreCase("invalidId")) {
							System.err.println("Error in file : " + files[i] + 
									" reading record num " + recordCount+" at line: " + lineNumber);
							System.out.println(" Previous record id :" + prevRecordId);
							//System.exit(-1);							
						}
						prevRecordId = inputRecord.getID();
						try {
							Long.parseLong(inputRecord.getID());
						} catch (NumberFormatException e) { // invalid synset-id

							System.err.println(" Fatal error in file : " + files[i] + 
									" reading record num " + recordCount+" at line: " + lineNumber);
							System.out.println(inputRecord);
							System.exit(-666);	// Fatal error.
						}
						//System.out.println(inputRecord.getID());
						boolean bCorrupt = false;
						Vector<String> vWords = inputRecord.getWords();
						Vector<AlignmentRecord> alignmentRecords = new Vector<AlignmentRecord>();
						for (int j=0; j<vWords.size(); j++) {
							String targetWord = vWords.elementAt(j);
							String sourceWord = null;
							int index = 0;
							if(targetWord.contains("HW")) {
								index = Integer.parseInt(targetWord.substring(targetWord.indexOf("HW") + 2).trim());
								targetWord = targetWord.replaceAll("/HW[0-9]*", "");
								DSFRecord sourceRecord = fetchFromDB(inputRecord.getID());
								if(sourceRecord.getWords().size() < index) {
									System.err.println("Corrupt " + inputRecord.getID());
									writeEntryToFile(inputRecord, inputRecord.getLanguage(), corruptFile, false);
									bCorrupt = true;
									break;
								} else {
									sourceWord = sourceRecord.getWords().elementAt(index-1);
									AlignmentRecord alignmentRecord = new AlignmentRecord();
									alignmentRecord.setSourceWord(sourceWord);
									alignmentRecord.setSrcLang(AppProperties.getProperty("source.language.choice"));
									alignmentRecord.setTargetWord(targetWord);
									alignmentRecord.setTargetLang(AppProperties.getProperty("target.language.choice"));
									alignmentRecord.setSynsetID(inputRecord.getID());
									alignmentRecords.add(alignmentRecord);
								}
							}
						}
						if(!bCorrupt) {
							String words = inputRecord.getWordsAsAStringEscapedCharacters();
							words = words.replaceAll("/HW[0-9]*", "");
							words = words.replaceAll("HW[0-9]*", "");
							inputRecord.setWordsAsAString(words);
							inputRecord.setAlignmentRecords(alignmentRecords);
							inputRecord.evaluateStatus();
							if(!synsetIDs.containsKey(inputRecord.getID()) ) {
								synsetIDs.put(inputRecord.getID(), inputRecord);
							} else {
								if(inputRecord.statusBetterThan(synsetIDs.get(inputRecord.getID()).getStatus())) {
									synsetIDs.put(inputRecord.getID(), inputRecord);
								}
								//System.err.println("Duplicate found" + inputRecord.getID());
							}

						} 
					}
					System.out.println("Done");
				}
				//outputFile.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{	
			try {
				inputFile.close();
				//outputFile.close();
				//corruptFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}						
			System.out.println("Closing DSFInputOutput");
		}

		//Upload to database
		Iterator<String> iter = synsetIDs.keySet().iterator();
		DSFRecord record = null;
		int updateCount = 0;
		System.out.println("Total Records = " + synsetIDs.size());
		while (iter.hasNext()) {
			record = synsetIDs.get(iter.next());
			DSFRecord existingRecord = dbLayer.getDSFRecord(record.getID(), Helper.getLanguageCode(record.getLanguage()));
			if("08528604".equals(record.getID())) {
				System.out.println("Wait");
			}
			if(existingRecord == null || record.statusBetterThan(existingRecord.getStatus()) ) {
				updateCount++;
				dbLayer.storeDSF(record);
			}
			writeEntryToFile(record, record.getLanguage(), outputFile, false);
		}
		System.out.println("Records updated = " + updateCount);

		try {
			if(outputFile != null)
				outputFile.close();
			if(corruptFile != null)
				corruptFile.close();
			if(dbLayer != null)
				dbLayer.closeDB();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	

    /**
     * Method 	: close
     * Purpose	: Closes the handle to the file .
     */
	public void close() {
		try {
			if(this.objBufReader != null) {
				this.objBufReader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			//closing error can be ignored
		}
	}
	
	private String readLine() throws IOException
	{
		this.lineNo++;
		return objBufReader.readLine();
	}
}
