/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : AlignmentRecord.java
 *
 * Created On: Aug 7, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;



/**
 * Class	: AlignmentRecord
 * Purpose	: This class is 
 */
public class AlignmentRecord {
	/**
	 * This field stores the source word to be aligned.
	 */
	private String strSourceWord = null;
	/**
	 * This field stores the target word with which the source 
	 * word is to be aligned.
	 */
	private String strTargetWord = null;
	/**
	 * This field stores the source language.
	 */
	private String strSrcLang 	 = null;
	/**
	 * This field stores the target language.
	 */
	private String strTargetLang = null;

	/**
	 * This field stores the target language.
	 */
	private String strSynsetID	 = null;

	/**
	 * Method 	: getSourceWord
	 * Purpose	: Returns the strSourceWord.
	 * @return  : strSourceWord
	 */
	public String getSourceWord() {
		return strSourceWord;
	}

	/**
	 * Method 	: getEsacapedSourceWord
	 * Purpose	: Returns the strSourceWord.
	 * @return  : strSourceWord
	 */
	public String getEscapedSourceWord() {
		if(strSourceWord != null) {
			strSourceWord = strSourceWord.replaceAll("\'", "\'\'");	
		}
		return strSourceWord;
	}

	/**
	 * Method 	: setSourceWord
	 * Purpose	: Sets the value of strSourceWord.
	 * @param 	: sourceWord
	 */
	public void setSourceWord(String sourceWord) {
		strSourceWord = Utilities.removeDigits(sourceWord).trim();
	}

	/**
	 * Method 	: getSrcLang
	 * Purpose	: Returns the strSrcLang.
	 * @return  : strSrcLang
	 */
	public String getSrcLang() {
		return strSrcLang;
	}

	/**
	 * Method 	: setSrcLang
	 * Purpose	: Sets the value of strSrcLang.
	 * @param 	: srcLang
	 */
	public void setSrcLang(String srcLang) {
		strSrcLang = srcLang;
	}

	/**
	 * Method 	: getSynsetID
	 * Purpose	: Returns the strSynsetID.
	 * @return  : strSynsetID
	 */
	public String getSynsetID() {
		return strSynsetID;
	}

	/**
	 * Method 	: setSynsetID
	 * Purpose	: Sets the value of strSynsetID.
	 * @param 	: synsetID
	 */
	public void setSynsetID(String synsetID) {
		strSynsetID = synsetID;
	}

	/**
	 * Method 	: getTargetLang
	 * Purpose	: Returns the strTargetLang.
	 * @return  : strTargetLang
	 */
	public String getTargetLang() {
		return strTargetLang;
	}

	/**
	 * Method 	: setTargetLang
	 * Purpose	: Sets the value of strTargetLang.
	 * @param 	: targetLang
	 */
	public void setTargetLang(String targetLang) {
		strTargetLang = targetLang;
	}

	/**
	 * Method 	: getTargetWord
	 * Purpose	: Returns the strTargetWord.
	 * @return  : strTargetWord
	 */
	public String getTargetWord() {
		return strTargetWord;
	}
	
	/**
	 * Method 	: getEscapedTargetWord
	 * Purpose	: Returns the strTargetWord.
	 * @return  : strTargetWord
	 */
	public String getEscapedTargetWord() {
		if(strTargetWord != null) {
			strTargetWord = strTargetWord.replaceAll("\'", "\'\'");	
		}
		return strTargetWord;
	}

	/**
	 * Method 	: setTargetWord
	 * Purpose	: Sets the value of strTargetWord.
	 * @param 	: targetWord
	 */
	public void setTargetWord(String targetWord) {
		strTargetWord = Utilities.removeDigits(targetWord).trim();
	}
	
	public void setTargetWordWithoutDigitRemoval(String targetWord)
	{
		strTargetWord = targetWord.trim();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o == null || !(o instanceof AlignmentRecord))
			return false;
		AlignmentRecord a = (AlignmentRecord)o;
		if(a.strSourceWord.equals(this.strSourceWord)
			&& a.strSrcLang.equalsIgnoreCase(this.strSrcLang)
			&& a.strSynsetID.equals(this.strSynsetID)
			&& a.strTargetLang.equalsIgnoreCase(this.strTargetLang)
			&& a.strTargetWord.equals(this.strTargetWord)) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString(java.lang.Object)
	 */
	
	public String toString() {
		return strSynsetID + "-[" + strSrcLang +"-" + strTargetLang +"]-" +
				"{" +strSourceWord +"->"+ strTargetWord+"}";
	}
}
