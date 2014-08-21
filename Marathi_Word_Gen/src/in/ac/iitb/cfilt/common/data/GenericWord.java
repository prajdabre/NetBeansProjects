/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : GenericWord.java
 *
 * Created On: Jun 10, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;

/**
 * Class	: GenericWord
 * Purpose	: This class is 
 */
public class GenericWord {

	/**
	 * This field stores the Lemma of the Word.
	 */
	private String strLemma = null;
	
	/**
	 * This field stores the POS tag of the word
	 */
	private String strPOSTag = null;
	
	/**
	 * Constructor
	 * @param strLemma
	 * @param strPOSTag
	 */
	public GenericWord(String strLemma, String strPOSTag) {
		this.strLemma = strLemma;
		this.strPOSTag = Utilities.fullPOSString(strPOSTag);
	}

	/**
	 * Constructor
	 * @param strLemma
	 */
	public GenericWord(String strLemma) {
		this.strLemma = strLemma;
	}

	/**
	 * Method 	: getLemma
	 * Purpose	: Returns the strLemma.
	 * @return  : strLemma
	 */
	public String getLemma() {
		return strLemma;
	}

	/**
	 * Method 	: setLemma
	 * Purpose	: Sets the value of strLemma.
	 * @param 	: strLemma
	 */
	public void setLemma(String strLemma) {
		this.strLemma = strLemma;
	}

	/**
	 * Method 	: getStrPOSTag
	 * Purpose	: Returns the strPOSTag.
	 * @return  : strPOSTag
	 */
	public String getPOSTag() {
		return strPOSTag;
	}

	/**
	 * Method 	: setPOSTag
	 * Purpose	: Sets the value of strPOSTag.
	 * @param 	: strPOSTag
	 */
	public void setPOSTag(String strPOSTag) {
		this.strPOSTag = Utilities.fullPOSString(strPOSTag);
	}
	
	public String toString() {
		return	"[" + getLemma() + ", " + getPOSTag() + "]";
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((strLemma == null) ? 0 : strLemma.hashCode());
		result = PRIME * result + ((strPOSTag == null) ? 0 : strPOSTag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GenericWord other = (GenericWord) obj;
		if (strLemma == null) {
			if (other.strLemma != null)
				return false;
		} else if (!strLemma.equals(other.strLemma))
			return false;
		if (strPOSTag == null) {
			if (other.strPOSTag != null)
				return false;
		} else if (!strPOSTag.equals(other.strPOSTag))
			return false;
		return true;
	}
}
