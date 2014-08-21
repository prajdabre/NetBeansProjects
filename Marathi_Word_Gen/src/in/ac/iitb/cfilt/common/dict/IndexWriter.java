/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : IndexWriter.java
 *
 * Created On: Dec 15, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.dict;

import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.UTFWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p><b>Class</b>	: IndexWriter
 * <p><b>Purpose</b>	: This class is used to write the index files. 
 */
public class IndexWriter {

	public static final int WORDWRITER = 1;
	public static final int SYNSETWRITER = 2;
	public static final int CROSSLINKWRITER = 3;
	
	/**
	 * This field stores a word Index writer corresponding to each category
	 */
	private HashMap<String, UTFWriter> wordWriters = new HashMap<String, UTFWriter>();

	/**
	 * This field stores a cross-link Index writer corresponding to each category
	 */
	private HashMap<String, UTFWriter> crossLinksWriters = new HashMap<String, UTFWriter>();

	/**
	 * This field stores a synset Index writer corresponding to each category
	 */
	private HashMap<String, UTFWriter> synsetWriters = new HashMap<String, UTFWriter>();

	/**
	 * <b>Constructor</b>
	 * @param root
	 */
	public IndexWriter(String root) {
		for(int i=0; i<POS1.CATEGORIES.length; i++) {
			wordWriters.put(getKey(i), getWordWriter(root, i));
			crossLinksWriters.put(getKey(i), getCrossLinksWriter(root, i));
			synsetWriters.put(getKey(i), getSynsetWriter(root, i));
		}
	}
	
	/**
	 * <b>Constructor</b>
	 * @param root
	 * @param indexType
	 */
	public IndexWriter(String root, int indexType) {
		switch(indexType)
		{
		case WORDWRITER:
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
				wordWriters.put(getKey(i), getWordWriter(root, i));
			break;
		case SYNSETWRITER:
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
				synsetWriters.put(getKey(i), getSynsetWriter(root, i));
			break;
		case CROSSLINKWRITER:
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
				crossLinksWriters.put(getKey(i), getCrossLinksWriter(root, i));
			break;
		default:
			return;
		}
		return;
	}
	
	/**
	 * <p><b>Method</b> 	: open
	 * <p><b>Purpose</b>	: Opens all the writers. 
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void open() throws MultilingualDictException {
		for(int i=0; i<POS1.CATEGORIES.length; i++) {
			try {
				wordWriters.get(getKey(i)).open();
				crossLinksWriters.get(getKey(i)).open();
				synsetWriters.get(getKey(i)).open();
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
	}
	
	/**
	 * <p><b>Method</b> 	: open
	 * <p><b>Purpose</b>	: Opens all the writers. 
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void open(int indexType) throws MultilingualDictException {
		if(indexType == WORDWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					wordWriters.get(getKey(i)).open();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}else if (indexType == SYNSETWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					synsetWriters.get(getKey(i)).open();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}else if (indexType == CROSSLINKWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					crossLinksWriters.get(getKey(i)).open();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}
		return;
	}
	
	/**
	 * <p><b>Method</b> 	: writeToWordIndex
	 * <p><b>Purpose</b>	: Writes data to the word index file.
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void writeToWordIndex(String pos, String data) throws MultilingualDictException {
		pos = clean(pos);
		if(pos != null) {
			try {
				wordWriters.get(pos).write(data + "\n");
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
	}
	
	/**
	 * <p><b>Method</b> 	: writeToSynsetIndex
	 * <p><b>Purpose</b>	: Writes data to the synset index file.
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void writeToSynsetIndex(String pos, String data) throws MultilingualDictException {
		pos = clean(pos);
		if(pos != null) {
			try {
				synsetWriters.get(pos).write(data + "\n");
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
	}

	/**
	 * <p><b>Method</b> 	: writeToWordIndex
	 * <p><b>Purpose</b>	: Writes data to the word index file.
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void writeToCrossLinkIndex(String pos, String data) throws MultilingualDictException {
		pos = clean(pos);
		if(pos != null) {
			try {
				crossLinksWriters.get(pos).write(data + "\n");
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
	}

	/**
	 * <p><b>Method</b> 	: close
	 * <p><b>Purpose</b>	: Closes all the writers. 
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void close() throws MultilingualDictException {
		for(int i=0; i<POS1.CATEGORIES.length; i++) {
			try {
				wordWriters.get(getKey(i)).close();
				crossLinksWriters.get(getKey(i)).close();
				synsetWriters.get(getKey(i)).close();
			} catch (IOException ex) {
				throw new MultilingualDictException(ex);
			}
		}
	}
	/**
	 * <p><b>Method</b> 	: close
	 * <p><b>Purpose</b>	: Closes all the writers. 
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public void close(int indexType) throws MultilingualDictException {
		if(indexType == WORDWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					wordWriters.get(getKey(i)).close();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}else if (indexType == SYNSETWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					synsetWriters.get(getKey(i)).close();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}else if (indexType == CROSSLINKWRITER)
		{
			for(int i=0; i<POS1.CATEGORIES.length; i++) 
			{
				try {
					crossLinksWriters.get(getKey(i)).close();
				} catch (IOException ex) {
					throw new MultilingualDictException(ex);
				}
			}
		}
		return;
	}

	/**
	 * <p><b>Method</b> 	: clean
	 * <p><b>Purpose</b>	: Removes junk characters, if any, in the POS category. 
	 * <p><b>@param pos
	 * <p><b>@return</b>
	 */
	private String clean (String pos) {
		pos = pos.toUpperCase();
		if(pos.contains("NOUN"))
			return POS1.NOUN.toString();
		if(pos.contains("ADJECTIVE"))
			return POS1.ADJECTIVE.toString();
		if(pos.contains("ADVERB"))
			return POS1.ADVERB.toString();
		if(pos.contains("VERB"))
			return POS1.VERB.toString();
		if(pos.contains("FW"))
			return POS1.FW.toString();
		if(pos.contains("TAM"))
			return POS1.TAM.toString();
		
		return null;
	}
	
	/**
	 * <p><b>Method</b> 	: getKey
	 * <p><b>Purpose</b>	: Gets the key corresponding to the i-th category. 
	 * <p><b>@param i
	 * <p><b>@return</b>
	 */
	private String getKey(int i) {
		return POS1.CATEGORIES[i].toString();
	}

	/**
	 * <p><b>Method</b> 	: getCrossLinksWriter
	 * <p><b>Purpose</b>	: Gets the Cross Links Index Writer corresponding to the i-th category.
	 * <p><b>@param root
	 * <p><b>@param i
	 * <p><b>@return</b>
	 */
	private UTFWriter getCrossLinksWriter(String root, int i) {
		return new UTFWriter(new File(root, GlobalConstants.CROSSLINK_INDEX_FILES[i]));
	}

	/**
	 * <p><b>Method</b> 	: getWordWriter
	 * <p><b>Purpose</b>	: Gets the Word Index Writer corresponding to the i-th category.
	 * <p><b>@param root
	 * <p><b>@param i
	 * <p><b>@return</b>
	 */
	private UTFWriter getWordWriter(String root, int i) {
		return new UTFWriter(new File(root, GlobalConstants.WORD_INDEX_FILES[i]));
	}

	/**
	 * <p><b>Method</b> 	: getSynsetReader
	 * <p><b>Purpose</b>	: Gets the Synset Index Writer corresponding to the i-th category.
	 * <p><b>@param root
	 * <p><b>@param i
	 * <p><b>@return</b>
	 */
	private UTFWriter getSynsetWriter(String root, int i) {
		return new UTFWriter(new File(root, GlobalConstants.DATA_FILES[i]));
	}

}
