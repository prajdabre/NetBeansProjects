/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : GlobalConstants.java
 *
 * Created On: Aug 27, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.config;


/**
 * Class	: GlobalConstants
 * Purpose	: This class stores the constants which will be 
 * 		 	  used globally across the application. 
 */
public class GlobalConstants {
	public static final String SYNSET_WORD_SEPARATORS = "[,،]";
	public static final String EMPTY = "Empty";
	public static final String INCOMPLETE = "Incomplete";
	public static final String UNALIGNED = "Unaligned";
	public static final String COMPLETE = "Complete";
	public static final String REVIEW = "Review";
	public static final String COMPLETE_AND_ACCEPTED = "Complete_and_Accepted";
	public static final String COMPLETE_AND_REJECTED = "Complete_and_Rejected";
	public static final String COMPLETE_AND_MODIFIED = "Complete_and_Modified";
	public static final String COMPLETE_TO_BE_VERIFIED = "Complete_to_be_Verified";
	
	public static final String PROJECT_ELIL_MT = "EL-IL-MT/CLIA";
	public static final String PROJECT_ILIL_MT = "IL-IL-MT";
	
//	public static final String 	DOMAIN_HEALTH = "HEALTH";
//	public static final String 	DOMAIN_TOURISM = "TOURISM";
	
	public static final String NOUN_IDX_FILE = "noun.idx";
	public static final String VERB_IDX_FILE = "verb.idx";
	public static final String ADJ_IDX_FILE = "adj.idx";
	public static final String ADV_IDX_FILE = "adv.idx";
	public static final String FW_IDX_FILE = "fw.idx";
	public static final String TAM_IDX_FILE = "tam.idx";
	public static final String PRON_IDX_FILE = "pron.idx";
	public static final String NOUN_CROSS_LINKS_IDX_FILE = "noun-crossLinks.idx";
	public static final String VERB_CROSS_LINKS_IDX_FILE = "verb-crossLinks.idx";
	public static final String ADJ_CROSS_LINKS_IDX_FILE = "adj-crossLinks.idx";
	public static final String ADV_CROSS_LINKS_IDX_FILE = "adv-crossLinks.idx";
	public static final String FW_CROSS_LINKS_IDX_FILE = "fw-crossLinks.idx";
	public static final String TAM_CROSS_LINKS_IDX_FILE = "tam-crossLinks.idx";
	public static final String PRON_CROSS_LINKS_IDX_FILE = "pron-crossLinks.idx";
	public static final String NOUN_DATA_FILE = "data.noun";
	public static final String VERB_DATA_FILE = "data.verb";
	public static final String ADJ_DATA_FILE = "data.adj";
	public static final String ADV_DATA_FILE = "data.adv";
	public static final String FW_DATA_FILE = "data.fw";
	public static final String TAM_DATA_FILE = "data.tam";
	public static final String PRON_DATA_FILE = "data.pron";

	public static final String[] CROSSLINK_INDEX_FILES = new String[]{
		NOUN_CROSS_LINKS_IDX_FILE, VERB_CROSS_LINKS_IDX_FILE, ADV_CROSS_LINKS_IDX_FILE, ADJ_CROSS_LINKS_IDX_FILE, FW_CROSS_LINKS_IDX_FILE, TAM_CROSS_LINKS_IDX_FILE};
	public static final String[] WORD_INDEX_FILES = new String[]{
		NOUN_IDX_FILE, VERB_IDX_FILE, ADV_IDX_FILE, ADJ_IDX_FILE, FW_IDX_FILE, TAM_IDX_FILE};
	public static final String[] DATA_FILES = new String[]{
		NOUN_DATA_FILE, VERB_DATA_FILE, ADV_DATA_FILE, ADJ_DATA_FILE, FW_DATA_FILE, TAM_DATA_FILE};
	/**
	 * This field stores the names of the fields in a DSFRecord.
	 */
	public static final String[] FIELD_NAMES = {"ID",
		"CAT",
		"CONCEPT",
		"EXAMPLE",
		"SYNSET-"};
	/* TODO: we have to add domainName field here */

	public static final String TILDA = "~";
	public static final String COMMA = ",";
	public static final String CARAT = "^";
	public static final String HASH = "#";
	public static final String FRONTSLASH = "/";

	public static final String CLFILESUFFIX = "-CLTemp.txt";
	public static final String[] getStatus() {
		String status[] = null;
		if(AppProperties.INTERNAL_USE) {
			status = new String[] {
					EMPTY, INCOMPLETE, UNALIGNED, REVIEW, 
					COMPLETE_TO_BE_VERIFIED, COMPLETE_AND_ACCEPTED,
					COMPLETE_AND_REJECTED, COMPLETE_AND_MODIFIED, COMPLETE};
		} else {
			status = new String[] {
					EMPTY, INCOMPLETE, UNALIGNED, COMPLETE, REVIEW};
		}
		return status;
	}
}
