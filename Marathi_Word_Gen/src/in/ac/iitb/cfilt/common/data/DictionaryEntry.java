/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DictionaryEntry
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;

/**
 * Class	: DictionaryEntry
 * Purpose	: This class stores a Bilingual Dictionary Entry.
 */
public class DictionaryEntry implements SerializableData {

    /**
     * This field stores a random serial ID.
     * Does not have any significance. Its just to avoid warnings.
     */
    public static final long serialVersionUID = 2354616L;
    
    /**
     * This field stores the source language word.
     */
    private String strSourceWord 	= "";
    
    /**
     * This field stores the translation in the target language.
     */
    private String strTranslation 	= "";
    
    /**
     * This field stores the information about the word.
     */
    private String strWordInfo 		= "";
    
    /**
     * This field stores the POS tag of the word.
     */
    private String strPOSTag 		= "";
    
    /**
     * This field stores the "icl" class of the word.
     */
    private String strIncClass 		= "";

    /**
     * This field stores the "icl" class of the word.
     */
    private String strEngGloss 		= "";

    /**
     * This field stores the "icl" class of the word.
     */
    private String strHindiGloss 		= "";

    /**
     * Constructor
     * @param strSourceWord
     * @param strTranslation
     * @param strWordInfo
     * @param strPOSTag
     * @param strClass
     */
    public DictionaryEntry(String strSourceWord, 
    		String strTranslation, 
    		String strWordInfo, 
    		String strPOSTag, 
    		String strClass) {
    	this.strSourceWord = strSourceWord;
    	this.strTranslation = strTranslation;
    	this.strWordInfo = strWordInfo;
    	this.strPOSTag = Utilities.fullPOSString(strPOSTag);
    	this.strIncClass = strClass;
    }
    
    
	/**
	 * Constructor
	 * @param strSourceWord
	 * @param strTranslation
	 * @param strWordInfo
	 * @param strPOSTag
	 * @param strIncClass
	 * @param strEngGloss
	 * @param strHindiGloss
	 */
	public DictionaryEntry(String strSourceWord, String strTranslation, String strWordInfo, String strPOSTag, String strIncClass, String strEngGloss, String strHindiGloss) {
		super();
		this.strSourceWord = strSourceWord;
		this.strTranslation = strTranslation;
		this.strWordInfo = strWordInfo;
		this.strPOSTag = Utilities.fullPOSString(strPOSTag);
		this.strIncClass = strIncClass;
		this.strEngGloss = strEngGloss;
		this.strHindiGloss = strHindiGloss;
	}

	/**
	 * @return Returns the strClass.
	 */
	public String getIncClass() {
		return strIncClass;
	}

	/**
	 * @param strClass The strClass to set.
	 */
	public void setIncClass(String strClass) {
		this.strIncClass = strClass;
	}

	/**
	 * @return Returns the POSTag, one of N, V, ADJ, ADV, Interj etc
	 */
	public String getPOSTag() {
		/*  Convert POS tags VT and VI (transitive, intransitive verbs) to V */
		if(strPOSTag.equals("VT") || strPOSTag.equals("VI")){
			return "V";
		}
		return strPOSTag;
	}

	/**
	 * @return Returns the POSTag, in its full qualified form
	 * Contains one of N, V, ADJ or ADV and
	 * VT (Transitive Verb), VI (Intransitive Verb) Interj
	 */
	public String getFullPOSTag() {
		return strPOSTag;
	}
	
	/**
	 * @param strPOSTag The strPOSTag to set.
	 */
	public void setPOSTag(String strPOSTag) {
		this.strPOSTag = Utilities.fullPOSString(strPOSTag);
	}

	/**
	 * @return Returns the strSourceWord.
	 */
	public String getSourceWord() {
		return strSourceWord;
	}

	/**
	 * @param strSourceWord The strSourceWord to set.
	 */
	public void setSourceWord(String strSourceWord) {
		this.strSourceWord = strSourceWord;
	}

	/**
	 * @return Returns the strTranslation.
	 */
	public String getTranslation() {
		return strTranslation;
	}

	/**
	 * @param strTranslation The strTranslation to set.
	 */
	public void setTranslation(String strTranslation) {
		this.strTranslation = strTranslation;
	}

	/**
	 * @return Returns the strWordInfo.
	 */
	public String getStrWordInfo() {
		return strWordInfo;
	}

	/**
	 * @param strWordInfo The strWordInfo to set.
	 */
	public void setWordInfo(String strWordInfo) {
		this.strWordInfo = strWordInfo;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Source = " + strSourceWord);
    	sb.append("\tTrans = " + strTranslation);
    	sb.append("\tWord Info= " + strWordInfo);
    	sb.append("\tPOS = " + strPOSTag);
    	sb.append("\tIncClass = " + strIncClass);
    	/*    	  
    	 sb.append("\tSource Gloss = " + strEngGloss);
    	 sb.append("\tTarget Gloss   = " + strHindiGloss);
    	*/
    	return sb.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof DictionaryEntry))
    		return false;
    	DictionaryEntry d = (DictionaryEntry)o;
    	if(this.strTranslation.equals(d.getTranslation())) {
    		return true;
    	}
    	return false;
    }


	/**
	 * Method 	: getStrEngGloss
	 * Purpose	: Returns the strEngGloss.
	 * @return  : strEngGloss
	 */
	public String getStrEngGloss() {
		return strEngGloss;
	}


	/**
	 * Method 	: setStrEngGloss
	 * Purpose	: Sets the value of strEngGloss.
	 * @param 	: strEngGloss
	 */
	public void setStrEngGloss(String strEngGloss) {
		this.strEngGloss = strEngGloss;
	}


	/**
	 * Method 	: getStrHindiGloss
	 * Purpose	: Returns the strHindiGloss.
	 * @return  : strHindiGloss
	 */
	public String getStrHindiGloss() {
		return strHindiGloss;
	}


	/**
	 * Method 	: setStrHindiGloss
	 * Purpose	: Sets the value of strHindiGloss.
	 * @param 	: strHindiGloss
	 */
	public void setStrHindiGloss(String strHindiGloss) {
		this.strHindiGloss = strHindiGloss;
	}
}
