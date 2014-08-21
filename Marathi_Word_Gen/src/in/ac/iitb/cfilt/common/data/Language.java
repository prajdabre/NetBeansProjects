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

/**
 * Class	: Language
 * Purpose	: This class represents a Language. 
 */
public class Language {

	private int languageCode=0;

	/**
	 * Constructor to initialize the language string and its language code
	 * @param lang Integer representing the language string
	 */
	public Language(int lang){
		languageCode = lang;
	}

	public static final Language BENGALI = new Language(0);
	public static final Language ENGLISH = new Language(1);
	public static final Language GUJRATI = new Language(2);
	public static final Language HINDI = new Language(3);
	public static final Language MALAYALAM = new Language(4);
	public static final Language MARATHI = new Language(5);
	public static final Language PUNJABI = new Language(6);
	public static final Language TELUGU = new Language(7);
	public static final Language TAMIL = new Language(8);
	public static final Language KANADA = new Language(9);
	public static final Language URDU = new Language(10);
	public static final Language ORIYA = new Language(11);
	
	/**
	 * Array of all the languages in the system
	 */
	public static final Language ALL_LANGS[]=
	{
		BENGALI,
		ENGLISH,
		GUJRATI,
		HINDI,
		MALAYALAM,
		MARATHI,
		PUNJABI,
		TELUGU,
		TAMIL,
		KANADA,
		URDU,
		ORIYA,
	};

	/**
	 * String representation of the object. Overrides the toString method in java.lang
	 */
	public String toString(){
		switch(languageCode){
			case 0:return("BEN");
			case 1:return("ENG");
			case 2:return("GUJ");
			case 3:return("HIN");
			case 4:return("MAL");
			case 5:return("MAR");
			case 6:return("PAN");
			case 7:return("TEL");
			case 8:return("TAM");
			case 9:return("KAN");
			case 10:return("URD");
			case 11:return("ORI");
			default:return null;
		}
	}
	
	public static Language getLanguage(String lang){
		if("BEN".equalsIgnoreCase(lang)) 
			return BENGALI;
		if("ENG".equalsIgnoreCase(lang)) 
			return ENGLISH;
		if("GUJ".equalsIgnoreCase(lang)) 
			return GUJRATI;
		if("HIN".equalsIgnoreCase(lang)) 
			return HINDI;
		if("MAL".equalsIgnoreCase(lang)) 
			return MALAYALAM;
		if("MAR".equalsIgnoreCase(lang)) 
			return MARATHI;
		if("PAN".equalsIgnoreCase(lang)) 
			return PUNJABI;
		if("TEL".equalsIgnoreCase(lang)) 
			return TELUGU;
		if("TAM".equalsIgnoreCase(lang)) 
			return TAMIL;
		if("KAN".equalsIgnoreCase(lang)) 
			return KANADA;
		if("URD".equalsIgnoreCase(lang)) 
			return URDU;
		if("ORI".equalsIgnoreCase(lang)) 
			return ORIYA;
		throw new IllegalArgumentException("The argument \'" + lang + "\' is not a valid language");
	}
}
