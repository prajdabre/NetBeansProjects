package in.ac.iitb.cfilt.common.data;

import java.util.Vector;

/**
 * Class : EnglishSynset Purpose : This class is
 */
public class MultiDictSynset extends GenericSynset implements SerializableData {

	/**
	 * This field stores
	 */
	private static final long serialVersionUID = -4566462509962209390L;

	/**
	 * Useful when this class is Serialized
	 */
	

	/**
	 * This field stores the handle to the Multidictionary Synset.
	 */
	private DSFRecord objRecord = null;

	/**
	 * This field stores the words belonging to this Synset.
	 */
	private GenericWord[] objWordsInSynset = null;

	/**
	 * Constructor
	 * 
	 * @param objSynset
	 * 
	 * To create a clone of the JWNL Synset Object
	 */
	public MultiDictSynset(DSFRecord objRecord) {
		this.objRecord = objRecord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getGloss()
	 */
	public String getGloss() {
		return objRecord.getConcept() + "; " + objRecord.getExample();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getOffset()
	 */
	public long getOffset() {
		return Long.parseLong(objRecord.getID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getWords()
	 */
	public GenericWord[] getWords() {
		Vector<String> strWords = objRecord.getWords();
		objWordsInSynset = new GenericWord[strWords.size()];
		for (int i = 0; i < objWordsInSynset.length; i++) {
			objWordsInSynset[i] = new GenericWord(strWords.get(i), objRecord
					.getCategory());
		}
		return objWordsInSynset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getPOS()
	 */
	public String getPOS() {
		return objRecord.getCategory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#containsWord(in.ac.iitb.cfilt.wnlinker.data.GenericWord)
	 */
	public boolean containsWord(GenericWord objWord) {
		if (objWordsInSynset == null) {
			objWordsInSynset = getWords();
		}
		for (int i = 0; i < objWordsInSynset.length; i++) {
			if (objWord.getLemma().equals(objWordsInSynset[i].getLemma())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objRecord == null) ? 0 : objRecord.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MultiDictSynset))
			return false;
		final MultiDictSynset other = (MultiDictSynset) obj;
		if (objRecord == null) {
			if (other.objRecord != null)
				return false;
		} else if (!objRecord.equals(other.objRecord))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String output = "{";

		GenericWord[] words = getWords();
		for (int i = 0; i < words.length; i++) {
			output += words[i] + ", ";
		}
		output += "} -" + getOffset() + "- " + getGloss();
		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getBaseSynset()
	 */
	public Object getBaseSynset() {
		return objRecord;
	}

}
