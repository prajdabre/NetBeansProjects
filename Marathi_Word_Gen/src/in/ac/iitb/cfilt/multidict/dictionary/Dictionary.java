/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : Dictionary.java
 *
 * Created On: Dec 15, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.multidict.dictionary;

import java.util.HashMap;

import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;

/**
 * <p><b>Class</b>	: Dictionary
 * <p><b>Purpose</b>	: This class acts as a Base Class for FileBackedDictionary
 * 						  and DatabaseBackedDictionary. 
 */
public abstract class Dictionary {

	
	 /** 
	  * This field stores a singleton instance of this class for each language. 
	  */
	private static HashMap<String, Dictionary> instances = new HashMap<String, Dictionary>();
	
	
	String userDictPath = "database/user_dict";
	String sessionDictPath = "database/session_dict";
	String systemDictPath = "database/system_dict";
	
	
	//private static Dictionary instance = null; 
	
	/**
	 * <p><b>Method</b> 	: getInstance
	 * <p><b>Purpose</b>	: Returns a singleton instance of the Dictionary.
	 * 						  Currently it supports only FileBackedDictionary.
	 * <p><b>@return</b>
	 */
	public static Dictionary getInstance() {
		boolean bFile = true;
		if(instances.get("defaultDict") == null) {
			if(bFile) {
				instances.put("defaultDict", new FileBackedDictionary());
			} else {
				instances.put("defaultDict", new DatabaseBackedDictionary());
			}
		}
		return instances.get("defaultDict");
	}
	

	
	/**
	 * <p><b>Method</b> 	: getInstance
	 * <p><b>Purpose</b>	: Returns a singleton instance of the Dictionary.
	 * 						  Currently it supports only FileBackedDictionary.
	 * <p><b>@return</b>
	 */
	public static Dictionary getInstance(String dictName) {
		boolean bFile = true;
		if(dictName.equals("userDict") || dictName.equals("sessionDict") || dictName.equals("NNPDict") ||
				dictName.equals("BilingualDict") || dictName.equals("systemDict") || dictName.equals("defaultDict") ) 
		{
			if(instances.get(dictName) == null) 
			{
				if(bFile) 
					instances.put(dictName, new FileBackedDictionary());
				else 
					instances.put(dictName, new DatabaseBackedDictionary());
			}
			return instances.get(dictName);
		}
		else 
			return null; 	// TODO is this behaviour correct	
	}
	
	
	/**
	 * <p><b>Method</b> 	: initialize
	 * <p><b>Purpose</b>	: Initializes the Dictionary. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract void initialize(String config) throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: initialize
	 * <p><b>Purpose</b>	: Initializes the Dictionary for a specific source language - target language pair. 
	 * <p><b>@param config
	 * <p><b>@param sourceLanguage
	 * <p><b>@param targetLanguage
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract void initialize(String config, Language sourceLanguage, Language targetLanguage) throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: getDSFRecords
	 * <p><b>Purpose</b>	: Gets all the DSFRecords for the given word belonging to the 
	 * 						  belonging to all POS categories in the given language.  
	 * <p><b>@param lemma   - the source word
	 * <p><b>@param language- the source Language
	 * <p><b>@return</b>    - All DSFRecords to which the word belongs.
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract DSFRecord[] getDSFRecords(String lemma, Language language)
	throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: getDSFRecords
	 * <p><b>Purpose</b>	: Gets all the DSFRecord for the given word belonging to the 
	 * 						  given pos category in the given language.
	 * 						  If the POS category is null this method will return synsets
	 * 						  belonging to all categories.  
	 * <p><b>@param lemma   - the source word
	 * <p><b>@param pos     - the category of the word
	 * <p><b>@param language- the source Language
	 * <p><b>@return</b>    - All DSFRecords to which the word belongs.
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract DSFRecord[] getDSFRecords(String lemma, POS1 pos, Language language)
	throws MultilingualDictException;
	
	/**
	 * <p><b>Method</b> 	: getDSFRecord
	 * <p><b>Purpose</b>	: Gets the DSFRecord with the given ID in the given language. 
	 * <p><b>@param synsetId- The synsetId whose details need to be fetched.
	 * <p><b>@param language- The source language.
	 * <p><b>@return</b>	- The DSFRecord with id = synsetId
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract DSFRecord getDSFRecord(String synsetId, Language language)
	throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: getDSFRecord
	 * <p><b>Purpose</b>	: Gets the DSFRecord with the given ID with the given POS in the
	 * 						  given language. 
	 * <p><b>@param synsetId- The synsetId whose details need to be fetched.
	 * <p><b>@param pos		- The POS category of the DSFRecord.
	 * <p><b>@param language- The source language.
	 * <p><b>@return</b>	- The DSFRecord with id = synsetId and category = pos 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract DSFRecord getDSFRecord(String synsetId, POS1 pos, Language language)
	throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: getCrossLinkedWords
	 * <p><b>Purpose</b>	: Gets the translations of the source word in the target language.
	 * 						  A word might have several senses, so this method will return the
	 * 						  translations corresponding to all the senses to which the word 
	 * 						  belongs.	 
	 * <p><b>@param lemma			- The source word.
	 * <p><b>@param sourceLanguage	- The source language.
	 * <p><b>@param targetLanguage	- The target language. 
	 * <p><b>@return				- The translations of the source word in the target language. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract String[] getCrossLinkedWords(
			String lemma, Language sourceLanguage, Language targetLanguage)
	throws MultilingualDictException;
	
	/**
	 * <p><b>Method</b> 	: getCrossLinkedWords
	 * <p><b>Purpose</b>	: Gets the translations of the source word in the target language.
	 * 						  A word might have several senses and several POS categories,
	 * 						  so this method will return the translations corresponding to all the
	 * 					 	  senses to which the word corresponding to the given POS tag.  
	 * <p><b>@param lemma			- The source word.
	 * <p><b>@param sourceLanguage	- The source language.
	 * <p><b>@param pos				- The category of the word
	 * <p><b>@param targetLanguage	- The target language. 
	 * <p><b>@return				- The translations of the source word in the target language. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, POS1 pos) throws MultilingualDictException;
		
	/**
	 * <p><b>Method</b> 	: getCrossLinkedWords
	 * <p><b>Purpose</b>	: Gets the translation of the source word in the target language.
	 * 						  A word might have several senses, so this method will return the
	 * 						  translations corresponding to the given sense.	 
	 * <p><b>@param lemma			- The source word.
	 * <p><b>@param sourceLanguage	- The source language.
	 * <p><b>@param targetLanguage	- The target language. 
	 * <p><b>@param synsetId		- The sense to which the word belongs
	 * <p><b>@return				- The translations of the source word in the target language 
	 * 								  corresponding to the given sense. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract String[] getCrossLinkedWords(
			String lemma, Language sourceLanguage, Language targetLanguage, String synsetId)
	throws MultilingualDictException;

	/**
	 * <p><b>Method</b> 	: getCrossLinkedWords
	 * <p><b>Purpose</b>	: Gets the translation of the source word in the target language.
	 * 						  A word might have several senses, so this method will return the
	 * 						  translations corresponding to the given sense.	 
	 * <p><b>@param lemma			- The source word.
	 * <p><b>@param sourceLanguage	- The source language.
	 * <p><b>@param targetLanguage	- The target language. 
	 * <p><b>@param synsetId		- The sense to which the word belongs
	 * <p>@param pos				- The pos to which the synset/word belongs.	
	 * <p>@return
	 * <p>@throws MultilingualDictException
	 */
	public abstract String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, String synsetId, POS1 pos)
	throws MultilingualDictException; 
	
	/**
	 * <p><b>Method</b> 	: contains
	 * <p><b>Purpose</b>	: Determines of the given lemmma belongs to the given synset in the
	 * 						  given language. 
	 * <p><b>@param synsetId- Synset ID.
	 * <p><b>@param lemma	- lemma
	 * <p><b>@param language- language	
	 * <p><b>@return		- true, if the word belongs to the synset, false otherwise. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract boolean contains (String synsetId, String lemma, Language language) 
	throws MultilingualDictException;
	
	/**
	 * <p><b>Method</b> 	: close
	 * <p><b>Purpose</b>	: Closes the Dictionary. 
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public abstract void close() throws MultilingualDictException;



	/**
	 * Method 	: getUserDictPath
	 * Purpose	: Returns the userDictPath.
	 * @return  : userDictPath
	 */
	public String getUserDictPath() {
		return userDictPath;
	}



	/**
	 * Method 	: setUserDictPath
	 * Purpose	: Sets the value of userDictPath.
	 * @param 	: userDictPath
	 */
	public void setUserDictPath(String userDictPath) {
		this.userDictPath = userDictPath;
	}



	/**
	 * Method 	: getSessionDictPath
	 * Purpose	: Returns the sessionDictPath.
	 * @return  : sessionDictPath
	 */
	public String getSessionDictPath() {
		return sessionDictPath;
	}



	/**
	 * Method 	: setSessionDictPath
	 * Purpose	: Sets the value of sessionDictPath.
	 * @param 	: sessionDictPath
	 */
	public void setSessionDictPath(String sessionDictPath) {
		this.sessionDictPath = sessionDictPath;
	}



	/**
	 * Method 	: getSystemDictPath
	 * Purpose	: Returns the systemDictPath.
	 * @return  : systemDictPath
	 */
	public String getSystemDictPath() {
		return systemDictPath;
	}



	/**
	 * Method 	: setSystemDictPath
	 * Purpose	: Sets the value of systemDictPath.
	 * @param 	: systemDictPath
	 */
	public void setSystemDictPath(String systemDictPath) {
		this.systemDictPath = systemDictPath;
	}
}
