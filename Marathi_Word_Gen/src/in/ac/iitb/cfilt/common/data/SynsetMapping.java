/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : EngHinMapping.java
 *
 * Created On: 26-Feb-08
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;
import in.ac.iitb.cfilt.common.io.UTFReader;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * <p><b>Class</b>	: EngHinMapping
 * <p><b>Purpose</b>	: This class is a data structure for representing English Hindi 
 * 						  mappings.
 */
public class SynsetMapping {

	/**
	 * This field stores the English synset id.
	 */
	private String engId = null;
	
	/**
	 * This field stores the English POS
	 */
	private POS1 engPOS = null;
	
	/**
	 * This field stores the Hindi synset id.
	 */
	private String hinId = null;

	/**
	 * This field stores the Hindi POS
	 */
	private POS1 hinPOS = null;
	
	/**
	 * This field stores the comments, if any, for this mapping
	 */
	private String comments = null; 
	
	/**
	 * This field stores the timestamp of the last modification of this synset. 
	 */
	private Timestamp lastModified = null;

	/**
	 * <p><b>Method</b> 	: getEngId
	 * <p><b>Purpose</b>	: Returns the engId.
	 * @return  : engId
	 */
	public String getEngId() {
		return engId;
	}

	/**
	 * <p><b>Method</b> 	: setEngId
	 * <p><b>Purpose</b>	: Sets the value of engId.
	 * @param 	: engId
	 */
	public void setEngId(String engId) {
		this.engId = engId;
	}

	/**
	 * <p><b>Method</b> 	: getEngPOS
	 * <p><b>Purpose</b>	: Returns the engPOS.
	 * @return  : engPOS
	 */
	public POS1 getEngPOS() {
		return engPOS;
	}

	/**
	 * <p><b>Method</b> 	: setEngPOS
	 * <p><b>Purpose</b>	: Sets the value of engPOS.
	 * @param 	: engPOS
	 */
	public void setEngPOS(POS1 engPOS) {
		this.engPOS = engPOS;
	}

	/**
	 * <p><b>Method</b> 	: getHinId
	 * <p><b>Purpose</b>	: Returns the hinId.
	 * @return  : hinId
	 */
	public String getHinId() {
		return hinId;
	}

	/**
	 * <p><b>Method</b> 	: setHinId
	 * <p><b>Purpose</b>	: Sets the value of hinId.
	 * @param 	: hinId
	 */
	public void setHinId(String hinId) {
		this.hinId = hinId;
	}

	/**
	 * <p><b>Method</b> 	: getHinPOS
	 * <p><b>Purpose</b>	: Returns the hinPOS.
	 * @return  : hinPOS
	 */
	public POS1 getHinPOS() {
		return hinPOS;
	}

	/**
	 * <p><b>Method</b> 	: setHinPOS
	 * <p><b>Purpose</b>	: Sets the value of hinPOS.
	 * @param 	: hinPOS
	 */
	public void setHinPOS(POS1 hinPOS) {
		this.hinPOS = hinPOS;
	}

	/**
	 * <p><b>Method</b> 	: getComments
	 * <p><b>Purpose</b>	: Returns the comments.
	 * @return  : comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * <p><b>Method</b> 	: setComments
	 * <p><b>Purpose</b>	: Sets the value of comments.
	 * @param 	: comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * <p><b>Method</b> 	: getLastModified
	 * <p><b>Purpose</b>	: Returns the lastModified.
	 * @return  : lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}

	/**
	 * <p><b>Method</b> 	: setLastModified
	 * <p><b>Purpose</b>	: Sets the value of lastModified.
	 * @param 	: lastModified
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(hinId);
		sb.append(":");
		sb.append(hinPOS);
		sb.append("::");
		sb.append(engId);
		sb.append(":");
		sb.append(engPOS);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof SynsetMapping)) {
			return false;
		}
		SynsetMapping m = (SynsetMapping)obj;
		if(this.engId != null && m.engId != null && this.engId.equals(m.engId)
				&& this.engPOS != null && m.engPOS != null && this.engPOS.equals(m.engPOS)
				&& this.hinId != null && m.hinId != null && this.hinId.equals(m.hinId)
				&& this.hinPOS != null && m.hinPOS != null && this.hinPOS.equals(m.hinPOS)) {
			return true;
		}
		
		return false;
	}
	
	public static  SynsetMapping[] readMappingFile(String inputFileName) {		
		HashMap<String,SynsetMapping> allMappings = new HashMap<String,SynsetMapping>();
		//HashSet<Long> containedIds = new HashSet<Long>();
		
		UTFReader inputFile = new UTFReader(inputFileName);
		try {
			inputFile.open();
			String line = null;
			while((line = inputFile.readLine()) != null) {
				if(line.trim().length() == 0 || line.startsWith("#")) {
					continue;
				}
				String[] data = line.split(";");
				SynsetMapping mapping = new SynsetMapping();
				int engOffset, hinOffset;
				if(data[0].equals("ENG")	) {
					engOffset = 1;
					hinOffset = 3;
				} else {
					engOffset = 3;
					hinOffset = 1;
				}
				try {
					mapping.setHinId(data[hinOffset]);
					mapping.setHinPOS(Utilities.getHindiCommonPOS(data[hinOffset + 1]));
				} catch(ArrayIndexOutOfBoundsException e) {
					mapping.setHinId(null);
					mapping.setHinPOS(null);
				}
				
				try {
					mapping.setEngId(data[engOffset]);
					mapping.setEngPOS(Utilities.getHindiCommonPOS(data[engOffset + 1]));
				} catch (ArrayIndexOutOfBoundsException e) {
					mapping.setEngId(null);
					mapping.setEngPOS(null);
					
				}
				if(data.length > 5){
					mapping.setComments(data[5]);
				} else {
					mapping.setComments(""); 
				}
				
				allMappings.put(mapping.getHinId(), mapping );	// keep the latest mapping
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputFile.close();
		return allMappings.values().toArray(new SynsetMapping[allMappings.size()]);	
	}
}
