/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DSFRecord.java
 *
 * Created On: Aug 7, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;


import in.ac.iitb.cfilt.common.Utilities;
import in.ac.iitb.cfilt.common.config.GlobalConstants;

import java.io.Serializable;
import java.util.Vector;


/**
 * Class	: DSFRecord
 * Purpose	: This class is the data structure for storing a DSF record.  
 */
public class DSFRecord implements Comparable<Object>, Serializable {

	/**
	 * This field stores the default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This field stores the ID of the Synset.
	 */
	private String strID = null;
	/**
	 * This field stores the category of the Synset.
	 */
	private String strCategory = null;
	/**
	 * This field stores the concept description of the Synset.
	 */
	private String strConcept = null;
	/**
	 * This field stores the examples for the Synset.
	 */
	private String strExample = null;
	/**
	 * This field stores the words belonging to the Synset.
	 */
	private Vector<String> vWords = null;
	/**
	 * This field stores the target language.
	 */
	private String strLanguage = null;
	/**
	 * This field stores the status.
	 */
	private String strStatus = null;
	/**
	 * This field stores the review comments.
	 */
	private String strComments = "";
	/**
	 * This field stores the review comments.
	 */
	private String strOntologyID = null;

	/**
	 * This field stores the relevance of this synset to this domain. 
	 */
	private double dRelevance = 0;
	/**
	 * This field stores the relevance of this synset to this domain. 
	 */
	private String domainName = null;

	/**
	 * This field stores the alignments of the members of this synset with the 
	 * memebers of the target synset.
	 */
	private Vector<AlignmentRecord> vAlignmentRecords = new Vector<AlignmentRecord>();
	
	/**
	 * This field stores teh "Lock" status.
	 */
	private boolean bIsLocked = false;
	
	/**
	 * <b>Constructor</b> Default
	 */
	public DSFRecord() {
	}
	
	/**
	 * <b>Constructor</b>
	 * @param synset
	 */
	public DSFRecord(GenericSynset synset) {
		this.setID(new Long(synset.getOffset()).toString());	

		int exampleOffset = synset.getGloss().indexOf("\"");
		if(exampleOffset == -1) {	// No example
			this.setConcept(synset.getGloss());
			this.setExample("");				
		} else {
			String concept = synset.getGloss().substring(0,exampleOffset).trim();
			if(concept.charAt(concept.length()-1) == ':' || 
					concept.charAt(concept.length()-1) == ';'){
				concept = concept.substring(0, concept.length() -1 );
			}				
			this.setConcept(concept);
			this.setExample(synset.getGloss().substring(exampleOffset));
		}

		this.setCategory(Utilities.fullPOSString(synset.getPOS()));
		GenericWord[] words = synset.getWords();
		Vector<String> wordList = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
			wordList.add(words[i].getLemma());
		}
		this.setWords(wordList);
	}
	
	/**
	 * <b>Constructor</b>
	 * @param id
	 * @param category
	 * @param concept
	 * @param example
	 * @param wordList - A comma separated list of words.
	 */
	public DSFRecord (String id, String category, String concept, String example, String wordList) {
		this.strID = id;
		this.strCategory = category;
		this.strConcept = concept;
		this.strExample = example;
		this.setWordsAsAString(wordList);
	}
	
	/**
	 * <b>Constructor</b>
	 * @param id
	 * @param category
	 * @param concept
	 * @param example
	 * @param words - A Vector of words.
	 */
	public DSFRecord (String id, String category, String concept, String example, Vector<String> words) {
		this.strID = id;
		this.strCategory = category;
		this.strConcept = concept;
		this.strExample = example;
		this.setWords(words);
	}

	/**
	 * Method 	: getCategory
	 * Purpose	: Returns the strCategory.
	 * @return  : strCategory
	 */
	public String getCategory() {
		return strCategory;
	}
	/**
	 * Method 	: setCategory
	 * Purpose	: Sets the value of strCategory.
	 * @param 	: category
	 */
	public void setCategory(String category) {
		if(category == null) category = "";
		strCategory = category.toUpperCase();
	}
	/**
	 * Method 	: getConcept
	 * Purpose	: Returns the strConcept.
	 * @return  : strConcept
	 */
	public String getConcept() {
		return strConcept;
	}
	/**
	 * Method 	: getExample
	 * Purpose	: Returns the strExample.
	 * @return  : strExample
	 */
	public String getConceptEscapedCharacters() {
		strConcept = strConcept.replaceAll("\'", "\'\'");
		return strConcept;
	}
	
	/**
	 * Method 	: setConcept
	 * Purpose	: Sets the value of strConcept.
	 * @param 	: concept
	 */
	public void setConcept(String concept) {
		if(concept == null) concept = "";
		strConcept = concept.trim();
	}
	/**
	 * Method 	: getExample
	 * Purpose	: Returns the strExample.
	 * @return  : strExample
	 */
	public String getExample() {
		return strExample;
	}
	/**
	 * Method 	: getExample
	 * Purpose	: Returns the strExample.
	 * @return  : strExample
	 */
	public String getExampleEscapedCharacters() {
		strExample = strExample.replaceAll("\'", "\'\'");
		return strExample;
	}
	
	/**
	 * Method 	: setExample
	 * Purpose	: Sets the value of strExample.
	 * @param 	: example
	 */
	public void setExample(String example) {
		strExample = Utilities.doubleEnqot(example);
	}
	/**
	 * Method 	: getID
	 * Purpose	: Returns the strID.
	 * @return  : strID
	 */
	public String getID() {
		return this.strID;
	}
	/**
	 * Method 	: setID
	 * Purpose	: Sets the value of strID.
	 * @param 	: id
	 */
	public void setID(String id) {
		strID = id.toUpperCase();
	}
	/**
	 * Method 	: getWordsAsAString
	 * Purpose	: Returns the strWords.
	 * @return  : strWords
	 */
	public String getWordsAsAString() {
		if(vWords == null || vWords.size() == 0) {
			return("");
		}
		StringBuffer wordList = new StringBuffer();
		for (int i = 0; i < vWords.size() - 1; i++) {
			String aWord = cleanInput(vWords.get(i)).replaceAll(" ", "_");
			wordList.append( aWord + ", ");
		}
		if(vWords.size() != 0) {
			String aWord = cleanInput(vWords.get(vWords.size()-1)).replaceAll(" ", "_");
			wordList.append(aWord);
		}
		return  wordList.toString();

	}
	
	/**
	 * Method 	: getWordsAsAString
	 * Purpose	: Returns the strWords with escaped quotes.
	 * @return  : strWords
	 */
	public String getWordsAsAStringEscapedCharacters() {
		if(vWords == null || vWords.size() == 0) {
			return("");
		}
		StringBuffer wordList = new StringBuffer();
		for (int i = 0; i < vWords.size() - 1; i++) {
			String aWord = cleanInput(vWords.get(i)).replaceAll(" ", "_");
			wordList.append( aWord + ", ");
		}
		if(vWords.size() != 0) {
			String aWord = cleanInput(vWords.get(vWords.size()-1)).replaceAll(" ", "_");
			wordList.append(aWord);
		}
		String retWordList = wordList.toString().replaceAll("'", "''");
		return  retWordList;

	}
	/**
	 * Method 	: setWordsAsAString
	 * Purpose	: Sets the value of strWords.
	 * @param 	: words
	 */
	public void setWordsAsAString(String words) {
		if(words == null) words = "";
		words = cleanInput(words);
		String strArrWords[] = words.split(GlobalConstants.SYNSET_WORD_SEPARATORS);
		vWords = new Vector<String>(strArrWords.length);
		for(int i=0; i<strArrWords.length; i++) {
			strArrWords[i] = strArrWords[i].trim();
			if(! strArrWords[i].equals("")) {
				if(strArrWords[i].contains(" ")) {
					strArrWords[i] = strArrWords[i].replaceAll(" ", "_");
				}				
				vWords.add(strArrWords[i]);
			}
		}		
	}
	/**
	 * Method 	: getWords
	 * Purpose	: Returns the vWords.
	 * @return  : vWords
	 */
	public Vector<String> getWords() {
		return vWords;
	}
	
	/**
	 * Method 	: getWords
	 * Purpose	: Returns the vWords with escaped quotes.
	 * @return  : vWords
	 */
	public Vector<String> getWordsEscapedCharacters() {
		Vector<String> vWordsEscapedCharacters = new Vector<String>();
		for (int i = 0; i < vWords.size(); i++) {
			vWordsEscapedCharacters.add(vWords.elementAt(i).replaceAll("'", "''"));
		}
		return vWordsEscapedCharacters;
	}
	/**
	 * Method 	: setWords
	 * Purpose	: Sets the value of vWords.
	 * @param 	: words
	 */
	public void setWords(Vector<String> words) {
		vWords = words;
		for (int i = 0; i < vWords.size(); i++) {
			vWords.set(i, cleanInput(vWords.get(i)).replaceAll(" ", "_"));
		}
	}
	/**
	 * Method 	: getLanguage
	 * Purpose	: Returns the strLanguage.
	 * @return  : strLanguage
	 */
	public String getLanguage() {
		return strLanguage;
	}
	/**
	 * Method 	: setLanguage
	 * Purpose	: Sets the value of strLanguage.
	 * @param 	: language
	 */
	public void setLanguage(String language) {
		strLanguage = language.toUpperCase();
	}
	

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * Method 	: getStatus
	 * Purpose	: Gets the status of the DSFRecords, one of 'INCOMPLETE', 'EMPTY', 'REVIEW', 'UNALIGNED' or 'COMPLETE'.
	 * @return 	: status
	 */	
	
	
	public String getStatus() {
		return strStatus;
	}
	
	/**
	 * Method 	: setStatus
	 * Purpose	: Sets the status of the DSFRecords, one of 'INCOMPLETE', 'EMPTY', 'REVIEW', 'UNALIGNED' or 'COMPLETE'.
	 * @param 	: language
	 */	
	public void setStatus(String status) {
		strStatus = status.toUpperCase();
	}
	/**
	 * Method 	: getAlignmentRecords
	 * Purpose	: Returns the vAlignmentRecords.
	 * @return  : vAlignmentRecords
	 */
	public Vector<AlignmentRecord> getAlignmentRecords() {
		return vAlignmentRecords;
	}
	/**
	 * Method 	: setAlignmentRecords
	 * Purpose	: Sets the value of vAlignmentRecords.
	 * @param 	: alignmentRecords
	 */
	public void setAlignmentRecords(Vector<AlignmentRecord> alignmentRecords) {
		vAlignmentRecords = alignmentRecords;
		cleanAlignmentsRecords();
	}
	/**
	 * Method 	: getRelevance
	 * Purpose	: Returns the dRelevance.
	 * @return  : dRelevance
	 */
	public double getRelevance() {
		return dRelevance;
	}
	/**
	 * Method 	: setRelevance
	 * Purpose	: Sets the value of dRelevance.
	 * @param 	: relevance
	 */
	public void setRelevance(double relevance) {
		dRelevance = relevance;
	}
	/**
	 * Method 	: isLocked
	 * Purpose	: Returns the bIsLocked.
	 * @return  : bIsLocked
	 */
	public boolean isLocked() {
		return bIsLocked;
	}
	/**
	 * Method 	: setLocked
	 * Purpose	: Sets the value of bIsLocked.
	 * @param 	: isLocked
	 */
	public void setLocked(boolean isLocked) {
		bIsLocked = isLocked;
	}
	
	@Override
	public String toString() {
		return "[ " + strID + " - {" + getWordsAsAString() +"}]";
	}
	
	/**
	 * Method 	: cleanInput
	 * Purpose	: Remove invalid characters
	 * @param input : String to be cleaned
	 * @return cleaned input string
	 */
	private String cleanInput(String input) {
		//input = input.replaceAll("\'", " ");
		//input = input.replaceAll("\"", " ");
		input = input.trim();
		return input;
	}
	
	public boolean contains (String word) {
		if (vWords == null)
			return false;
		return vWords.contains(word);
	}
	/**
	 * Method 	: getComments
	 * Purpose	: Returns the strComments.
	 * @return  : strComments
	 */
	public String getComments() {
		if(strComments == null) {
			strComments = "";
		}
		return strComments;
	}

	/**
	 * This field stores all possible status of a DSFRecord for comparison of DSFRecords
	 */
	public static Vector<String> statusVector = new Vector<String>();
	
	/**
	 * Method 	: getCommentsEscapedCharacters
	 * Purpose	: Returns the strComments.
	 * @return  : strComments
	 */
	public String getCommentsEscapedCharacters() {
		if(strComments == null) {
			strComments = "";
		}
		strComments = strComments.replaceAll("\'", "\'\'");
		return strComments;
	}

	/**
	 * Method 	: setComments
	 * Purpose	: Sets the value of strComments.
	 * @param 	: comments
	 */
	public void setComments(String comments) {
		strComments = comments;
	}

	/**
	 * Method 	: clean
	 * Purpose	: Removes the alignements for the words which
	 * 			  have been removed from the Synset.
	 * @param name 
	 * @param alignmentRecords
	 * @return
	 */
	public Vector<AlignmentRecord> cleanAlignmentsRecords() {
		int iSize = vAlignmentRecords.size();
		Vector<AlignmentRecord> vUpdatedRecords = new Vector<AlignmentRecord>();
		for(int i=0; i<iSize; i++) {
			if(vWords.contains(vAlignmentRecords.elementAt(i).getTargetWord())) {
				vUpdatedRecords.add(vAlignmentRecords.elementAt(i));
			} 
		}
		vAlignmentRecords = vUpdatedRecords;
		return vUpdatedRecords;
	}


	/**
	 * Method 	: evaluateStatus
	 * Purpose	: Set the status of the DSFRecord based on whether the fields of the DSFRecord are empty or not. 
	 * @param record
	 */
	public void evaluateStatus() {
		if(Utilities.isEmpty(strConcept) &&
			(Utilities.isEmpty(strExample) || strExample.equals("\"\"") )&&
			vWords.size() == 0) {
			strStatus = GlobalConstants.EMPTY;
		} else if(Utilities.isEmpty(strConcept) ||
				Utilities.isEmpty(strExample) || strExample.equals("\"\"") ||
				vWords.size() == 0 ||
				Utilities.isEmpty(strID) ||
				Utilities.isEmpty(strCategory)) {
			strStatus = GlobalConstants.INCOMPLETE;
		} else if (vAlignmentRecords.size() == 0) {
			strStatus =  GlobalConstants.UNALIGNED;
		} else strStatus =  GlobalConstants.COMPLETE;
	}
	
	/**
	 * Method 	: statusBetterThan
	 * Purpose	: Tell whether the status of {@link DSFRecord} is more complete ( requires less work than ) than the input parameter. 
	 * @param inputStatus
	 * @return
	 */
	public boolean statusBetterThan(String inputStatus) {
		if(statusVector.indexOf(strStatus.toUpperCase()) > statusVector.indexOf(inputStatus.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method 	: compareStatus
	 * Purpose	: Tell whether status1 is more complete than status2.  
	 * @param inputStatus
	 * @return an integer in a similar vein as cmpStr
	 */
	public static int compareStatus(String status1, String status2) {
		int statusId1 = statusVector.indexOf(status1.toUpperCase());
		int statusId2 = statusVector.indexOf(status2.toUpperCase());
		if(statusId1 > statusId2 ) {
			return 1;
		} else if(statusId1 < statusId2){
			return -1;
		} else {
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o == null || !(o instanceof DSFRecord))
			return false;
		DSFRecord d = (DSFRecord)o;
		if(d.strCategory.equals(this.strCategory)
			&& d.strComments.equals(this.strComments)
			&& d.strConcept.equals(this.strConcept)
			&& d.strExample.equals(this.strExample)
			&& d.strID.equals(this.strID)
			&& d.strLanguage.equals(this.strLanguage)
			&& d.domainName.equals(this.domainName)
			&& d.strStatus.equals(this.strStatus)
			&& d.getWordsAsAString().equals(this.getWordsAsAString())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method 	: compareAlignmentRecords
	 * Purpose	: Compares the alignment Records of the two DSFRecords. 
	 * @param vAlignmentRecords
	 * @return
	 */
	public boolean compareAlignmentRecords(Vector<AlignmentRecord> argAlignmentRecords) {
		if(this.vAlignmentRecords == null || argAlignmentRecords == null) {
			return false;
		}
		int size = this.vAlignmentRecords.size();
		if(size != argAlignmentRecords.size()) { 
			return false;
		}
		
		for (int i=0; i<size; i++) {
			if(!vAlignmentRecords.contains(argAlignmentRecords.elementAt(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
	    int hash = 1;
	    hash = hash * 31 
	        + (strCategory == null ? 0 : strCategory.hashCode());
/*	    Comments should not be considered while checking if the 
 * 	    record has been altered. 
 * 		hash = hash * 31 
	    	+ (strComments == null ? 0 : strComments.hashCode());
*/	    hash = hash * 31 
        	+ (strConcept == null ? 0 : strConcept.hashCode());
	    hash = hash * 31 
	    	+ (strExample == null ? 0 : strExample.hashCode());
	    hash = hash * 31 
    		+ (strID == null ? 0 : strID.hashCode());
	    hash = hash * 31 
    		+ (strLanguage == null ? 0 : strLanguage.hashCode());
/*	    hash = hash * 31 
    		+ (strStatus == null ? 0 : strStatus.hashCode());
*/	    hash = hash * 31 
			+ (vWords == null ? 0 : vWords.hashCode());
	    return hash;
	}
	/**
	 * Method 	: getOntologyID
	 * Purpose	: Returns the strOntologyID.
	 * @return  : strOntologyID
	 */
	public String getOntologyID() {
		return strOntologyID;
	}
	/**
	 * Method 	: setOntologyID
	 * Purpose	: Sets the value of strOntologyID.
	 * @param 	: ontologyID
	 */
	public void setOntologyID(String ontologyID) {
		strOntologyID = ontologyID;
	}	
	
	public void setAlignmentRecordsM(Vector<AlignmentRecord> alignmentRecords) {
		vAlignmentRecords = alignmentRecords;
	}
	/**
	 * <p><b>Method</b> 	: printInOneLine
	 * <p><b>Purpose</b>	: Prints the record in one line. 
	 * <p><b>@return</b>
	 */
	public String printInOneLine() {
		StringBuffer sb = new StringBuffer();
		sb.append(strID);
		sb.append(":~:");
		sb.append(strCategory);
		sb.append(":~:");
		sb.append(strConcept);
		sb.append(":~:");
		sb.append(strExample);
		sb.append(":~:");
		String words = getWordsAsAString();
		sb.append(words);
		if(words.equals(""))
			sb.append(" ");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(!(o instanceof DSFRecord))
			return 0;
		DSFRecord d = (DSFRecord)o; 
		
		if(this.getRelevance() < d.getRelevance()) {
			return -1;
		} else if(this.getRelevance() > d.getRelevance()) {
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * Initialize statusVector to facilitate comparison. 
	 */
	static {		
		statusVector.add("EMPTY");
		statusVector.add("INCOMPLETE");
		statusVector.add("REVIEW");
		statusVector.add("UNALIGNED");
		statusVector.add("COMPLETE_TO_BE_VERIFIED");
		statusVector.add("COMPLETE_AND_MODIFIED");
		statusVector.add("COMPLETE_AND_REJECTED");
		statusVector.add("COMPLETE_AND_ACCEPTED");
		statusVector.add("COMPLETE");
	}

}
