/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DBProperties.java
 *
 * Created On: 02-Jun-07
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import java.io.Serializable;

/**
 * Class	: POS
 * Purpose	: This class represents a POS category 
 */
public final class POS1 extends Object implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final POS1 NOUN = new POS1(1);
	public static final POS1 VERB = new POS1(2);
	public static final POS1 ADVERB = new POS1(3);
	public static final POS1 ADJECTIVE = new POS1(4);
	public static final POS1 FW = new POS1(5); // Function Word
	public static final POS1 TAM = new POS1(6); // For TAM dictionary
	public static final POS1 CONJ = new POS1(7);
	public static final POS1 PRON = new POS1(8);
	public static final POS1[] CATEGORIES= {NOUN, VERB, ADVERB, ADJECTIVE, FW, TAM};
	int posvalue;
	
	/**
	 * Constructor
	 * @param value
	 */
	public POS1(int value) {
		posvalue = value;
	}

	/**
	 * <p><b>Method</b> 	: intValue
	 * <p><b>Purpose</b>	: Returns the integer value of the POS category.
	 * <p>@return
	 */
	public int intValue() {
		return posvalue;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		switch(this.posvalue) {
			case 1:	 return("NOUN");
			case 2:	 return("VERB");
			case 3:	 return("ADVERB");
			case 4:	 return("ADJECTIVE");
			case 5:	 return("FUNCTION_WORD");
			case 6:	 return("TAM_WORD");
			case 7:	 return("CONJUNCTION");
			case 8:	 return("PRONOUN");
			default: return(null); 
		}
	}                                                                                                                            
}
