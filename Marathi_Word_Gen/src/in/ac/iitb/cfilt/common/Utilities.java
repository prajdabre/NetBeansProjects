/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : Utilities.java
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common;

import in.ac.iitb.cfilt.common.config.AppProperties;
import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.AlignmentRecord;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.POS1;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;



/**
 * Class	: Utilities
 * Purpose	: This class contains all Utilities functions which will be used 
 *            by different modules in the application. 
 */
public class Utilities {

	/**
	 * This field stores the instance of the HashMap
	 * which stores the language --> language code mapping.
	 */
	private static HashMap<String, String> langCodes = null;
	
	static {
		langCodes = new HashMap<String, String>();
		langCodes.put("hindi", "HIN");
		langCodes.put("english", "ENG");
		langCodes.put("marathi", "MAR");
		langCodes.put("bengali", "BEN");
		langCodes.put("malayalam", "MAL");
		langCodes.put("punjabi", "PAN");
		langCodes.put("tamil", "TAM");
		langCodes.put("telugu", "TEL");
		langCodes.put("gujarati", "GUJ");
		langCodes.put("kannada", "KAN");
		langCodes.put("urdu", "URD");
		langCodes.put("oriya", "ORI");
	}
	
	/**
	 * Method 	: toUnicodeString
	 * Purpose	: Converts a String to unicode String. 
	 * @param str
	 * @return
	 */
	public static String toUnicodeString(String str) {
		StringBuffer buffer = new StringBuffer();
		for(int i=2; i<str.length()-2; i++) {
			char ch = (char)Integer.parseInt(str.substring(i), 16);
			buffer.append(ch);
		}
		return buffer.toString();
	}
	
/*	*//**
	 * Method 	: removeSpecialCharacters
	 * Purpose	: Will remove any special characters in the String.
	 * @param str
	 * @return
	 *//*
	public static String removeSpecialCharacters(String str) {
		return str.replaceAll("[~!@#$%^&*()_+-=||\\{}\\[\\];':\",.<>?/]+","");
	}*/

	/**
	 * Method 	: removeDigits
	 * Purpose	: Will remove any digits in the String.
	 * @param str
	 * @return
	 */
	public static String removeDigits(String str) {
		return str.replaceAll("[0-9,\\.]+","").trim();
	}

	public static String enumerateAndLink(int iStart,
			String strWord, int iEnd, boolean bFirst) {
		StringBuilder sb = new StringBuilder();
		if(bFirst) {
			sb.append(iStart + 1);
			sb.append(". ");
			sb.append(strWord);
			sb.append(' ');
			sb.append(iEnd + 1);
		} else {
			sb.append(strWord);
			sb.append(',');
			sb.append(iEnd + 1);
		}
		return sb.toString();
	}
	
	/**
	 * Method 	: getCommonMessage
	 * Purpose	: Returns a Common Message to be appended to all exception messages. 
	 * @return  - A Common Message to be appended to all exception messages.
	 */
	public static String getCommonMessage() {
		return "\nPlease try again or contact System Administrators if the problem persists.";
	}
	
	/**
	 * Method 	: enqot
	 * Purpose	: surround the input string by single quotes for SQL literal values
	 * @param input
	 * @return String with surrounding single quotes.
	 */
	public static String enqot(String input) {
		if(input == null) return input;
		return "'" + input + "'";
	}

	/**
	 * Method 	: enqot
	 * Purpose	: surround the input string by single quotes for SQL literal values
	 * 			  and replace "'"	with "'\"
	 * @param input
	 * @return String with surrounding single quotes.
	 */
	public static String enqotAndAddEscapeChar(String input) {
		if(input == null) return input;
		input = input.replaceAll("\'", "\'\'");
		return enqot(input);
	}
	
	/**
	 * Method 	: doubleEnqot
	 * Purpose	: surround the input string by double quotes for SQL literal values
	 * @param input
	 * @return String with surrounding single quotes.
	 */
	public static String doubleEnqot(String input) {
		if(input == null || input.length() == 0) {
			return "\"\"";
		}
		input = input.trim();
		if(!input.startsWith("\"")) {
			input = "\"" + input;
		}
		if(!input.endsWith("\"")) {
			input = input + "\"";
		}
		return input;
	}

	/**
	 * Method 	: getLanguageCode
	 * Purpose	: Get the full language name corresponding to this language code. 
	 * @param langName
	 * @return Three letter ISO 639-2 Language Code
	 */
	public static String getLanguageCode(String langName) {
		return langCodes.get(langName.toLowerCase());
	}

	/**
	 * Method 	: getAllLanguages
	 * Purpose	: Returns all languages 
	 * @return
	 */
	public static String[] getAllLanguages() {
		Set<String> keys = langCodes.keySet();
		Iterator<String> iterator = keys.iterator();
		String[] languages = new String[keys.size()];
		int i=0;
		for (; iterator.hasNext();) {
			languages[i++] = getLanguageCode((String) iterator.next());
		}
		return languages;
	}

	/**
	 * Method 	: getLanguageName
	 * Purpose	: Get the full language name corresponding to this language code. 
	 * @param langCode: Three letter ISO 639-2 Language Code
	 * @return Full language name
	 */
	public static String getLanguageName(String langCode) {
		if(langCodes.containsValue(langCode)) {
			Set<String> langNameSet = langCodes.keySet(); 
			for (Iterator<String> iter = langNameSet.iterator(); iter.hasNext();) {
				String langName = (String) iter.next();
				if(getLanguageCode(langName).equals(langCode)) {
					return langName;
				}				
			}			
		}
		return null;	// No matching language name found 		
	}

	/**
	 * Method 	: isEmpty
	 * Purpose	: Checks if str is empty 
	 * @param str
	 * @return  - true if str is empty, false otherwise
	 */
	public static boolean isEmpty (String str) {
		return str == null || str.trim().equals("");		
	}
	
	/**
	 * Method 	: clean
	 * Purpose	: Removes the alignements for the words which
	 * 			  have been removed from the Synset.
	 * @param name 
	 * @param alignmentRecords
	 * @return
	 */
	public static Vector<AlignmentRecord> clean(
			Vector<String> words, Vector<AlignmentRecord> alignmentRecords) {
		DSFRecord record = new DSFRecord();
		record.setWords(words);
		record.setAlignmentRecords(alignmentRecords);
		return record.getAlignmentRecords();
	}
	
	public static boolean showOntology() {
		boolean showOntology =  AppProperties.INTERNAL_USE 
		&& (AppProperties.getProperty("type.of.synsets").equals(GlobalConstants.COMPLETE_TO_BE_VERIFIED)
			||	AppProperties.getProperty("type.of.synsets").equals(GlobalConstants.COMPLETE_AND_ACCEPTED)
			||	AppProperties.getProperty("type.of.synsets").equals(GlobalConstants.COMPLETE_AND_MODIFIED)
			||	AppProperties.getProperty("type.of.synsets").equals(GlobalConstants.COMPLETE_AND_REJECTED));
		return showOntology;
	}
	
	
	
    /**
     * Method 	: containsSpecialCharsOrDigits
     * Purpose	: This method checks whether the Input String contains
     *            any digits or special characters. 
     * @param   - str
     * @return  - true if the Input String contains any digits or special 
     *            characters, false otherwise.
     */
    public static boolean containsSpecialCharsOrDigits(String str) {
        char chr[] = null;
        if (str != null)
            chr = str.toCharArray();

        for (int i = 0; i < chr.length; i++) {
            if (!(chr[i] >= 'a' && chr[i] <= 'z')
                    && !(chr[i] >= 'A' && chr[i] <= 'Z')) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method 	: getEnglishPOS
     * Purpose	: Gets the POS object corresponding to the Label strPOS. 
     * @param   - strPOS
     * @return	- the POS object corresponding to the Label strPOS.
     */
    public static net.didion.jwnl.data.POS getEnglishPOS (String strPOS) {
    	if(strPOS.equals("N") || strPOS.equalsIgnoreCase("NOUN"))
    		return net.didion.jwnl.data.POS.NOUN;
    	if(strPOS.equals("ADJ") || strPOS.equalsIgnoreCase("ADJECTIVE"))
    		return net.didion.jwnl.data.POS.ADJECTIVE;
    	if(strPOS.equals("ADV") || strPOS.equalsIgnoreCase("ADVERB"))
    		return net.didion.jwnl.data.POS.ADVERB;
    	if(strPOS.equals("V") || strPOS.equalsIgnoreCase("VERB"))
    		return net.didion.jwnl.data.POS.VERB;
    	return null;
    }

    /**
     * Method 	: getEnglishPOS
     * Purpose	: Gets the POS object corresponding to the Label strPOS. 
     * @param   - strPOS
     * @return	- the POS object corresponding to the Label strPOS.
     */
    public static in.ac.iitb.cfilt.jhwnl.data.POS getHindiPOS (String strPOS) {
    	if(strPOS == null ){
    		return null;
    	}
    	if(strPOS.equalsIgnoreCase("N")|| strPOS.equalsIgnoreCase("NOUN"))
    		return in.ac.iitb.cfilt.jhwnl.data.POS.NOUN;
    	if(strPOS.equalsIgnoreCase("ADJ")|| strPOS.equalsIgnoreCase("ADJECTIVE"))
    		return in.ac.iitb.cfilt.jhwnl.data.POS.ADJECTIVE;
    	if(strPOS.equalsIgnoreCase("ADV")|| strPOS.equalsIgnoreCase("ADVERB"))
    		return in.ac.iitb.cfilt.jhwnl.data.POS.ADVERB;
    	if(strPOS.equalsIgnoreCase("V")|| strPOS.equalsIgnoreCase("VERB"))
    		return in.ac.iitb.cfilt.jhwnl.data.POS.VERB;
    	return null;
    }
    
    /**
     * <p><b>Method</b> 	: getHindiCommonPOS
     * <p><b>Purpose</b>	: Gets the POS object corresponding to the Label strPOS.
     * <p>@param strPOS
     * <p>@return
     */
    public static POS1 getHindiCommonPOS (String strPOS) {
    	if(strPOS == null ){
    		return null;
    	}
    	strPOS = strPOS.trim();
    	//System.out.println("**" + strPOS + "**");
    	if(strPOS.equalsIgnoreCase("N")|| strPOS.equalsIgnoreCase("NOUN") || strPOS.startsWith("N"))
    		return POS1.NOUN;
    	if(strPOS.equalsIgnoreCase("ADJ")|| strPOS.equalsIgnoreCase("ADJECTIVE") || strPOS.startsWith("JJ"))
    		return POS1.ADJECTIVE;
    	if(strPOS.equalsIgnoreCase("ADV")|| strPOS.equalsIgnoreCase("ADVERB") || strPOS.startsWith("RB"))
    		return POS1.ADVERB;
    	if(strPOS.equalsIgnoreCase("V")|| strPOS.equalsIgnoreCase("VERB") || strPOS.startsWith("V"))
    		return POS1.VERB;
    	return null;
    }

    /**
     * Method 	: getHindiPOSString
     * Purpose	: Returns the POS label. 
     * @param 	- objPOS
     * @return  - The POS label.
     */
    public static String getHindiPOSString (in.ac.iitb.cfilt.jhwnl.data.POS objPOS) {
    	if(objPOS == null ){
    		return null;
    	}
    	if(objPOS.equals(in.ac.iitb.cfilt.jhwnl.data.POS.NOUN))
    		return "NOUN";
    	if(objPOS.equals(in.ac.iitb.cfilt.jhwnl.data.POS.ADJECTIVE))
    		return "ADJECTIVE";
    	if(objPOS.equals(in.ac.iitb.cfilt.jhwnl.data.POS.ADVERB))
    		return "ADVERB";
    	if(objPOS.equals(in.ac.iitb.cfilt.jhwnl.data.POS.VERB))
    		return "VERB";
    	return "";
    }

    /**
     * Method 	: getEnglishPOSString
     * Purpose	: Returns the POS label. 
     * @param 	- objPOS
     * @return  - The POS label.
     */
    public static String getEnglishPOSString (net.didion.jwnl.data.POS objPOS) {
    	if(objPOS == null ){
    		return null;
    	}
    	if(objPOS.equals(net.didion.jwnl.data.POS.NOUN))
    		return "NOUN";
    	if(objPOS.equals(net.didion.jwnl.data.POS.ADJECTIVE))
    		return "ADJECTIVE";
    	if(objPOS.equals(net.didion.jwnl.data.POS.ADVERB))
    		return "ADVERB";
    	if(objPOS.equals(net.didion.jwnl.data.POS.VERB))
    		return "VERB";
    	return "";
    }
    
    /**
     * Method 	: removeSpecialCharacters
     * Purpose	: Removes any special characters that may be present in the String. 
     * @param 	- str
     * @return  - A clean String.
     */
    public static String removeSpecialCharacters(String str) {		
		str = str.replaceAll(REGEX_SPECIAL_CHARACTERS, "");		
		str = str.replaceAll(OTHER_SPECIAL_CHARACTERS, "");		
		return str;		
    }
    
	/**
	 * Method 	: getCategory
	 * Purpose	: 
	 * @param strCategory
	 * @return
	 */
	public static String getCategory(String strCategory) {
		if(strCategory == null)
			return "noun";
		if(strCategory.startsWith("N") || strCategory.equalsIgnoreCase("NOUN"))
		//if(strCategory.startsWith("N"))	
			return "noun";
		if(strCategory.startsWith("JJ")|| strCategory.equalsIgnoreCase("ADJECTIVE"))
		//if(strCategory.startsWith("JJ"))
			return "adjective";
		if(strCategory.startsWith("RB")|| strCategory.equalsIgnoreCase("ADVERB"))
		//if(strCategory.startsWith("RB"))
			return "adverb";
		if(strCategory.startsWith("V")|| strCategory.equalsIgnoreCase("VERB"))
		//if(strCategory.startsWith("V"))
			return "verb";
		return "noun";
	}

	public static void main(String[] args) {
		//AppProperties.load();
		System.out.println(removeSpecialCharacters("a1111\"b\'c..*$.[.).#.^];d"));
	}

	/**
	 * <p><b>Method</b> 	: getTimeStamp
	 * <p><b>Purpose</b>	: Returns the current timestamp 
	 * <p><b>@return</b>
	 */
	public static String getTimeStamp() {
		Calendar cal = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append('_');
		sb.append(cal.get(Calendar.MONTH));
		sb.append('_');
		sb.append(cal.get(Calendar.YEAR));
		sb.append('_');
		sb.append(cal.get(Calendar.HOUR));
		sb.append('_');
		sb.append(cal.get(Calendar.MINUTE));
		return sb.toString();
	}
	
	/**
	 * Method 	: fullPOSString
	 * Purpose	: Return full pos name from abbreviated form
	 * @param pos
	 * @return
	 */
	public static String fullPOSString(String pos) {
		if(pos == null) {
			return null;
		}
		if(pos.equalsIgnoreCase("N")) {
			return "NOUN";
		} else if (pos.equalsIgnoreCase("V")) {
			return "VERB";
		}  else if (pos.equalsIgnoreCase("ADJ")) {
			return "ADJECTIVE";
		} else if (pos.equalsIgnoreCase("ADV")) {
			return "ADVERB";
		}		
		return pos;	// Return the original string
	}	
	
	/**
	 * <p><b>Method</b> 	: leftPadZeroes
	 * <p><b>Purpose</b>	: Left pads with zeroes. 
	 * <p>@param id
	 * <p>@param length
	 * <p>@return
	 */
	public static String leftPadZeroes(String id, int length) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length - id.length(); i++) {
			sb.append('0');
		}
		sb.append(id);
		return sb.toString();
	}
	
	
	
	
	/**
	 * Method 	: recursiveDirectoryLister
	 * Purpose	: Recursively enumerates all files below the given directory satisfying the specified Regular Expression filter
	 * @param path	: Directory to start enumeration from. If it is a normal file, only that particular file is returned.
	 * @param filter : The regular expression filter to be applied to files
	 * @return
	 */
	public static String[] recursiveDirectoryLister(String path, String filter){
		return recursiveDirectoryListerImpl(path, filter, true);
	}
	
	
	
	/**
	 * This field stores the list of files under directory path
	 */
	static Vector<String> allFiles = null;
	/**
	 * Method 	: recursiveDirectoryLister
	 * Purpose	: Implements {@link}recursiveDirectoryLister
	 * @param path	: Directory to start enumeration from. If it is a normal file, only that particular file is returned.
	 * @param filter : The regular expression filter to be applied to files
	 * @param initialCall : variable to check whether this call is the initial call
	 * @return
	 */
	private static String[] recursiveDirectoryListerImpl(String path, String filter, boolean initialCall){
		
		if(initialCall) {
			allFiles = new Vector<String>();
		}
		
		File pathRoot = new File (path);
		
		try {
			if( pathRoot.isDirectory()) {
				allFiles.add(pathRoot.getCanonicalPath());
				String[] files = pathRoot.list();
				for (int i = 0; i < files.length; i++) {
					File thisFile = new File(path+ File.separator +files[i]);
					if(thisFile.isDirectory()) {	// Recursively Process directories
						recursiveDirectoryListerImpl(thisFile.getCanonicalPath(), filter, false);
					} else if(thisFile.isFile() && thisFile.getName().matches(filter)) {	// Filter filename 
						//System.out.println("\t Processing file no." + (++fileNumber) + " : " + thisFile.getName());
						allFiles.add(thisFile.getCanonicalPath());					
					}
				}
			} else if(pathRoot.isFile()){
				allFiles.add(pathRoot.getCanonicalPath());			
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return allFiles.toArray(new String[allFiles.size()]);
		}
		if(initialCall) {
			return allFiles.toArray(new String[allFiles.size()]);
		} else {
			return null;
		}
	}

	
	/**
	 * <p><b>Method</b> 	: getLanguageDirectory
	 * <p><b>Purpose</b>	: Returns the name of the language specific directory.  
	 * @return String
	 */
	public static String getLanguageDirName(String languageDirName, String domain, int fold) {
		languageDirName = ".." + File.separator 
		+ "resources" + File.separator 
		+"SupWSDResources" + File.separator 
		+ domain + File.separator
		+ languageDirName + File.separator
		+ "Fold" + (fold);
		new File(languageDirName).mkdirs();
		return languageDirName;
	}
	
	public static final String REGEX_SPECIAL_CHARACTERS = "[\\.\\*\\?\\(\\)\\{\\}\\`\\\"\\|\\<\\>\\\\]";
	public static final String OTHER_SPECIAL_CHARACTERS = "[@$!%^&=+:;,\'’-]";
	public static final String HINDI_PUNCTUATION = "[।]";
	public static final String HINDI_DIGITS = "[०-९]";
	public static final String ROMAN_DIGITS = "[0-9]";

}

