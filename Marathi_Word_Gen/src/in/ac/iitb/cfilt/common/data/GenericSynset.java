/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : GenericSynset.java
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
 * Class	: GenericSynset
 * Purpose	: This is the interface which defines the contract to be followed by the
 * 			  source and the target Synsets.
 */
public abstract class GenericSynset {

	/**
	 * Method 	: getGloss
	 * Purpose	: Returns the gloss of the Synset. 
	 * @return  - String
	 */
	public abstract String getGloss();
	
	/**
	 * Method 	: getOffset
	 * Purpose	: Returns the offset of the Synset  
	 * @return  - long
	 */
	public abstract long getOffset();
	
	/**
	 * Method 	: getWords
	 * Purpose	: Returns the Words belonging to the Synset.
	 * @return  - GenericWord[]
	 */
	public abstract GenericWord[] getWords();
	
	/**
	 * Method 	: getWordsAsString
	 * Purpose	: Get the words in the synset as a String 
	 * @return
	 */
	public String getWordsAsString() {
		GenericWord[] words = getWords();
		if(words == null || words.length == 0) {
			return("");
		}
		StringBuffer wordList = new StringBuffer();
		for (int i = 0; i < words.length - 1; i++) {
			String aWord = words[i].getLemma().replaceAll(" ", "_");
			wordList.append( aWord + ", ");
		}
		if(words.length != 0) {
			String aWord = words[words.length-1].getLemma().replaceAll(" ", "_");
			wordList.append(aWord);
		}
		return  wordList.toString();		
	}      
	
	/**
	 * Method 	: getConcept
	 * Purpose	: Return the concept for this synset 
	 * @return
	 */
	public String getConcept() {
		String gloss = getGloss();
		int exampleOffset = gloss.indexOf("\"");
		if(exampleOffset == -1) {	// No example
			return gloss;							
		} else {
			String concept = gloss.substring(0,exampleOffset).trim();
			if(concept.charAt(concept.length()-1) == ':' || 
					concept.charAt(concept.length()-1) == ';'){
				concept = concept.substring(0, concept.length() -1 );
			}				
			return concept;			
		}
	}
	

	/**
	 * Method 	: getExample
	 * Purpose	: Return the example for this synset
	 * @return
	 */
	public String getExample() {
		String gloss = getGloss();
		int exampleOffset = gloss.indexOf("\"");
		if(exampleOffset == -1) {	// No example
			return "";							
		} else {
			return gloss.substring(exampleOffset);
		}
	}
	
	/**
	 * Method 	: getPOS
	 * Purpose	: Returns the POS tag of the Synset. 
	 * @return  - String
	 */
	public abstract String getPOS();
	
	/**
	 * Method 	: getKey
	 * Purpose	: Returns a key corresponding to the synset which is unique among
	 *  all synsets. Is in effect the POS tag concatenated with synset-id/offset. 
	 * @return  - String
	 */
	public String getKey() {
		return getPOS() + "-" + getOffset();
	}
	
	
	/**
	 * Method 	: toString
	 * Purpose	: Returns a description of the Synset. 
	 * @return  - String
	 */
	public String toString() {
		StringBuilder output = new StringBuilder();
				
		output.append(getOffset());		
		output.append(" - ");
		GenericWord[] words = getWords();
		if(words != null ) {
			output.append( "{ ");
			for (int i = 0; i < words.length - 1; i++) {
				output.append(words[i].getLemma());
				output.append(", ");
			}
			output.append(words[words.length - 1].getLemma());	// append last word
			output.append( " } - ");
		}		
		String pos = getPOS();
		if(pos != null) {
			output.append(pos);
			output.append(" - ");
		}
		output.append(getGloss()) ;
		return output.toString();
				
	}
	
	/**
	 * Method 	: getBaseSynset
	 * Purpose	: Returns the base Wordnet Synset. 
	 * @return  - Object
	 */
	public abstract Object getBaseSynset();

	/**
	 * Method 	: getGlossWords
	 * Purpose	: Get the words in the gloss of the Synset.
	 * @param objSynset
	 * @return
	 */
	public GenericWord[] getGlossWords() {
		String strGloss = this.getGloss();
		String split[] = strGloss.split(" ");
		GenericWord[] objWords = new GenericWord[split.length];
		
		for(int i=0; i<split.length; i++) {
			//We need to do POS tagging here. As a temp fix I am assuming that the tag is Noun. 
			objWords[i] = new GenericWord(Utilities.removeSpecialCharacters(split[i]), "N");
		}		
		return objWords;
	}

    /**
     * Method 	: containsWord
     * Purpose	: Returns true if the input word belongs to this Synset.
     * @param objWord
     * @return
     */
    public abstract boolean containsWord(GenericWord objWord);
    
}
