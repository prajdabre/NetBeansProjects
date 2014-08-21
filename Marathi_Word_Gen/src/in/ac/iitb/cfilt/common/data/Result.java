/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : Result.java
 *
 * Created On: Aug 23, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.data;

import java.util.Vector;

/**
 * Class	: Result
 * Purpose	: This class represents a data structure to store the
 * 			  result obtained from the DB. 
 */
public class Result {

	/**
	 * This field stores source Record.
	 */
	DSFRecord objSourceDSFRecord = null;
	
	/**
	 * This field stores the target Record.
	 */
	DSFRecord objTargetDSFRecord = null;
	
	/**
	 * This field stores the Alignment Records.
	 */
	Vector<AlignmentRecord> vAlignmentRecords = new Vector<AlignmentRecord>();

	/**
	 * Constructor
	 * @param objSourceDSFRecord
	 * @param objSourceDSFRecord
	 * @param vAlignmentRecords
	 */
	public Result(DSFRecord objSourceDSFRecord,
			DSFRecord objTargetDSFRecord,
			Vector<AlignmentRecord> vAlignmentRecords) {
		this.objSourceDSFRecord = objSourceDSFRecord;
		this.objTargetDSFRecord = objTargetDSFRecord;
		if(vAlignmentRecords != null) {
			this.vAlignmentRecords = vAlignmentRecords;
		}
	}
	
	/**
	 * Method 	: getSourceDSFRecord
	 * Purpose	: Returns the objSourceDSFRecord.
	 * @return  : objSourceDSFRecord
	 */
	public DSFRecord getSourceDSFRecord() {
		return objSourceDSFRecord;
	}

	/**
	 * Method 	: setSourceDSFRecord
	 * Purpose	: Sets the value of objSourceDSFRecord.
	 * @param 	: sourceDSFRecord
	 */
	public void setSourceDSFRecord(DSFRecord sourceDSFRecord) {
		objSourceDSFRecord = sourceDSFRecord;
	}

	/**
	 * Method 	: getTargetDSFRecord
	 * Purpose	: Returns the objTargetDSFRecord.
	 * @return  : objTargetDSFRecord
	 */
	public DSFRecord getTargetDSFRecord() {
		return objTargetDSFRecord;
	}

	/**
	 * Method 	: setTargetDSFRecord
	 * Purpose	: Sets the value of objTargetDSFRecord.
	 * @param 	: targetDSFRecord
	 */
	public void setTargetDSFRecord(DSFRecord targetDSFRecord) {
		objTargetDSFRecord = targetDSFRecord;
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
	}
}
