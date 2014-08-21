/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : FileBackedDictionary.java
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
import in.ac.iitb.cfilt.multidict.preprocess.IndexReader;

import java.util.Vector;

/**
 * <p><b>Class</b>	: FileBackedDictionary
 * <p><b>Purpose</b>	: This class provides a handle to the file backed dictionary.
 */
public class FileBackedDictionary extends Dictionary {

	/**
	 * This field stores a handle to the word index file.
	 */
	private IndexReader indexReader = null;

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#initialize()
	 */
	@Override
	public void initialize(String databasePath) throws MultilingualDictException {
		indexReader = new IndexReader(databasePath);
		indexReader.open();
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#initialize(java.lang.String, in.ac.iitb.cfilt.common.data.Language, in.ac.iitb.cfilt.common.data.Language)
	 */
	public void initialize(String databasePath, Language sourceLanguage, Language targetLanguage) throws MultilingualDictException {
		indexReader = new IndexReader(databasePath, sourceLanguage, targetLanguage);
		//EnglishILMapper.initialize(databasePath + File.separator + "EnglishILMap.txt");
		indexReader.open();
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage) throws MultilingualDictException {
		return indexReader.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.common.data.Language, in.ac.iitb.cfilt.common.data.Language, in.ac.iitb.cfilt.common.data.POS)
	 */
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, POS1 pos) throws MultilingualDictException {
		return indexReader.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, pos);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.Language, java.lang.String)
	 */
	@Override
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, String synsetId)
			throws MultilingualDictException {
		if (contains(synsetId, lemma, sourceLanguage)) {
			return indexReader.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId);	
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getCrossLinkedWords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language, in.ac.iitb.cfilt.multidict.data.Language, java.lang.String)
	 */
	public String[] getCrossLinkedWords(String lemma, Language sourceLanguage,
			Language targetLanguage, String synsetId, POS1 pos)
			throws MultilingualDictException {
		if (contains(synsetId, lemma, sourceLanguage)) {
			return indexReader.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId, pos);	
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecord(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public DSFRecord getDSFRecord(String synsetId, Language language)
			throws MultilingualDictException {
		return indexReader.getDSFRecord(synsetId, language);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecord(java.lang.String, in.ac.iitb.cfilt.multidict.data.POS, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public DSFRecord getDSFRecord(String synsetId, POS1 pos, Language language)
	throws MultilingualDictException {
		return indexReader.getDSFRecord(synsetId, pos, language);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecords(java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public DSFRecord[] getDSFRecords(String lemma, Language language)
	throws MultilingualDictException {
		return indexReader.getDSFRecords(lemma, language);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#getDSFRecords(java.lang.String, in.ac.iitb.cfilt.multidict.data.POS, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	@Override
	public DSFRecord[] getDSFRecords(String lemma, POS1 pos, Language language)
			throws MultilingualDictException {
		return indexReader.getDSFRecords(lemma, pos, language);
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#contains(java.lang.String, java.lang.String, in.ac.iitb.cfilt.multidict.data.Language)
	 */
	public boolean contains(String synsetId, String lemma, Language language)
			throws MultilingualDictException {
		DSFRecord record = getDSFRecord(synsetId, language);
		if (record != null) {
			Vector<String> vWords = record.getWords();
			if(vWords.contains(lemma)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.multidict.dictionary.Dictionary#close()
	 */
	@Override
	public void close() throws MultilingualDictException {
		indexReader.close();
	}

}
