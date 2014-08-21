/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : EnglishILMapper.java
 *
 * Created On: Dec 18, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.multidict.preprocess;

import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.UTFReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p><b>Class</b>	: EnglishILMapper
 * <p><b>Purpose</b>	: This class is used to access the mapping between English 
 * 						  and IL synset ids.
 */
public class EnglishILMapper {
	
	/**
	 * This field stores the mapping from English synset to IL synset
	 */
	private static HashMap<String, Vector<String>> hmEngToIL = new HashMap<String, Vector<String>>();

	/**
	 * This field stores the mapping from IL synset to English synset
	 */
	private static HashMap<String, Vector<String>> hmILToEng = new HashMap<String, Vector<String>>();

	/**
	 * <b>Constructor</b> Private constructor to prevent instantiation.
	 */
	private EnglishILMapper() {
	}
	
	/**
	 * <p><b>Method</b> 	: initialize
	 * <p><b>Purpose</b>	: Loads the mapping file.
	 * <p><b></b>
	 * @throws MultilingualDictException 
	 */
	public static void initialize(String fileName) throws MultilingualDictException {
		try {
			UTFReader r = new UTFReader(fileName);
			r.open();
			String line = null;
			String pos = null;
			String ilID = null;
			String engID = null;
			int separatorIdex = 0;
			while ((line = r.readLine()) != null) {
				String[] tokens = line.split("::");
				pos = tokens[0];
				engID = tokens[1];
				ilID = tokens[3];
				if(!hmEngToIL.containsKey(pos+engID)) {
					hmEngToIL.put(pos+engID, new Vector<String>());
				} 
				hmEngToIL.get(pos+engID).add(ilID);
				
				if(!hmILToEng.containsKey(pos+ilID)) {
					hmILToEng.put(pos+ilID, new Vector<String>());
				} 
				hmILToEng.get(pos+ilID).add(engID);
			}
		} catch (IOException ex) {
			throw new MultilingualDictException(ex);
		}
	}
	
	/**
	 * <p><b>Method</b> 	: getEnglishToILMapping
	 * <p><b>Purpose</b>	: Gets the mapping from English synset to IL synset. 
	 * <p><b>@param synsetID
	 * <p><b>@return</b>
	 * @throws MultilingualDictException 
	 */
	public static String[] getEnglishToILMapping(String key) 
	throws MultilingualDictException {
		Vector<String> mappedIds = hmEngToIL.get(key);
		return mappedIds == null ? null : mappedIds.toArray(new String[mappedIds.size()]);
	}
	
	/**
	 * <p><b>Method</b> 	: getILToEnglishMapping
	 * <p><b>Purpose</b>	: Gets the mapping from IL synset to English synset. 
	 * <p><b>@param synsetID
	 * <p><b>@return</b>
	 * @throws MultilingualDictException 
	 */
	public static String[] getILToEnglishMapping(String key) 
	throws MultilingualDictException {
		Vector<String> mappedIds = hmILToEng.get(key);
		return mappedIds == null ? null : mappedIds.toArray(new String[mappedIds.size()]);
	}
	
	/**
	 * <p><b>Method</b> 	: isMapped
	 * <p><b>Purpose</b>	: Checks if the given English and Hindi Synset Ids are mapped to
	 * 						  each other.	 
	 * <p>@param engSynsetId
	 * <p>@param hindiSynsetId
	 * <p>@return			- true  if the given English and Hindi Synset Ids are mapped to
	 * 						  each other, false otherwise.
	 * @throws MultilingualDictException 
	 */
	public static boolean isMapped (String engSynsetId, String hinSynsetId, POS1 pos) 
	throws MultilingualDictException {
		if(engSynsetId == null || hinSynsetId == null)
			return false;
		String[] hinIds = getEnglishToILMapping(pos.toString() + engSynsetId);
		if(hinIds != null) {
			for(int i=0; i<hinIds.length; i++) {
				//System.out.println(hinSynsetId + "==" + hinIds[i]);
				if(hinIds[i].equals(hinSynsetId)) {
					return true;
				}
			}
		}
		return false;
	}
}
