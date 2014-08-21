/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DatabaseBackedDictionary.java
 *
 * Created On: Dec 16, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.multidict.dictionary;

import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;

import java.util.Vector;

/**
 * <p><b>Class</b>	: DatabaseBackedDictionary
 * <p><b>Purpose</b>	: This class is 
 */
public class DatabaseBackedDictionary extends Dictionary {

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#initialize()
	 */
	@Override
	public void initialize(String config) throws MultilingualDictException {
	}

	@Override
	public void initialize(String config, Language sourceLanguage,
			Language targetLanguage) throws MultilingualDictException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage) throws MultilingualDictException {
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.POS, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, POS1 pos) throws MultilingualDictException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.Language, java.lang.String)
	 */
	@Override
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, String synsetId)
			throws MultilingualDictException {
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecord(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public DSFRecord getDSFRecord(String synsetId, Language language)
			throws MultilingualDictException {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecord(java.lang.String, in.ac.iitb.cfilt.multidict.data.POS, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public DSFRecord getDSFRecord(String synsetId, POS1 pos, Language language)
	throws MultilingualDictException {
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public DSFRecord[] getDSFRecords(String lemma, Language language)
	throws MultilingualDictException {
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecords(java.lang.String, in.ac.iitb.cfilt.multidict.data.POS, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public DSFRecord[] getDSFRecords(String lemma, POS1 pos, Language language)
			throws MultilingualDictException {
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#contains(java.lang.String, java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public boolean contains(String synsetId, String lemma, Language language)
			throws MultilingualDictException {
		DSFRecord record = getDSFRecord(synsetId, language);
		Vector<String> vWords = record.getWords();
		if(vWords.contains(lemma)) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#close()
	 */
	@Override
	public void close() throws MultilingualDictException {
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.common.data.Language, in.ac.iitb.cfilt.common.data.Language, java.lang.String, in.ac.iitb.cfilt.common.data.POS)
	 */
	@Override
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, String synsetId, POS1 pos)
			throws MultilingualDictException {
		// TODO Auto-generated method stub
		return null;
	}
}
