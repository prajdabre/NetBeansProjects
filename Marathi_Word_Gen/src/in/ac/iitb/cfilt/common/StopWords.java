/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : StopWords.java
 *
 * Created On: Sep 18, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common;

import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.UTFReader;
import in.ac.iitb.cfilt.cpost.stemmer.StemmerRuleReader;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Class	: StopWords
 * Purpose	: This class maintains the list of stop words.
 */
public class StopWords {

	/**
	 * This field stores Hindi Stop Words
	 */
	private static Set<String> hindiStopWords = new HashSet<String>(); 
	
	/**
	 * This field stores English Stop words
	 */
	private static Set<String> englishStopWords = new HashSet<String>(); 

	/**
	 * This field stores Marathi Stop words
	 */
	private static Set<String> marathiStopWords = new HashSet<String>(); 

	/**
	 * Method 	: load
	 * Purpose	: Loads the stop words. 
	 */
	public static void load(String baseDir) throws MultilingualDictException {
		try {
			UTFReader r = new UTFReader(baseDir + File.separator + "HindiStopWords.txt");
			r.open();
			String line = null;
			while((line = r.readLine()) != null) {
				hindiStopWords.add(line.trim().toLowerCase());
			}
			r.close();
			
			r = new UTFReader(baseDir + File.separator + "EnglishStopWords.txt");
			r.open();
			while((line = r.readLine()) != null) {
				englishStopWords.add(line.trim().toLowerCase());
			}
			r.close();

			r = new UTFReader(baseDir + File.separator + "MarathiStopWords.txt");
			r.open();
			while((line = r.readLine()) != null) {
				marathiStopWords.add(line.trim().toLowerCase());
			}
			r.close();

		} catch (IOException ex) {
			throw new MultilingualDictException(ex);
		}
	}
	
	/**
	 * Method 	: isStopWord
	 * Purpose	: Checks if the input is a stop word.  
	 * @param sWord
	 * @return
	 */
	public static boolean isStopWord(String sWord) {
		if(hindiStopWords.contains(sWord.trim())) {
			return true;
		}
		//StemmerRuleReader stemmerRuleReader = new StemmerRuleReader();
		
		Vector<String> spellingVariations = new Vector<String>();// StemmerRuleReader.getSpellingVariations(sWord);
		if(spellingVariations != null) {
			for(int i=0; i<spellingVariations.size(); i++) {
				if(hindiStopWords.contains(spellingVariations.elementAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p><b>Method</b> 	: isStopWord
	 * <p><b>Purpose</b>	: Checks if the input is a stop word in the given language.
	 * <p><b>@param sWord
	 * <p><b>@param language
	 * <p><b>@return</b>
	 */
	public static boolean isStopWord(String sWord, Language language) {
		if(language.equals(Language.HINDI)) {
			return hindiStopWords.contains(sWord.trim());	
		} else if(language.equals(Language.ENGLISH)) {
			return englishStopWords.contains(sWord.trim().toLowerCase());
		} else if(language.equals(Language.MARATHI)) {
			return marathiStopWords.contains(sWord.trim().toLowerCase());
		}
		return false;
	}

}
