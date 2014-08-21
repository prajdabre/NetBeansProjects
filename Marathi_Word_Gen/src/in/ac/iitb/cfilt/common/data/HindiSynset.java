package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;
import in.ac.iitb.cfilt.jhwnl.data.POS;
import in.ac.iitb.cfilt.jhwnl.data.Synset;
import in.ac.iitb.cfilt.jhwnl.data.Word;

public class HindiSynset extends GenericSynset implements SerializableData {

	/**
	 * Useful when this class is Serialized
	 */
	static final long serialVersionUID = 3468678446778L;
	
	/**
	 * This field stores the handle to the Hindi Synset.
	 */
	private Synset objSynset = null;
	
    /**
     * This field stores the words belonging to this Synset.
     */
    private GenericWord[] arrWordsInSynset = null;

	/**
	 * Constructor
	 * @param objSynset
	 * 
	 * To create a clone of the JWNL Synset Object
	 */
	public HindiSynset(Synset objSynset) {		
		this.objSynset = objSynset;		
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getGloss()
	 */
	public String getGloss() {
		return objSynset.getGloss();
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getOffset()
	 */
	public long getOffset() {
		return objSynset.getOffset();
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getWords()
	 */
	public GenericWord[] getWords() {
        if(arrWordsInSynset != null) {
            return arrWordsInSynset;    
        }

		Word[] objWords = objSynset.getWords();
		if(objWords == null) {
			return null;
		}
		arrWordsInSynset = new GenericWord[objWords.length];
		for(int i=0; i<arrWordsInSynset.length; i++) {
			arrWordsInSynset[i] = new GenericWord(objWords[i].getLemma(), Utilities.getHindiPOSString(objWords[i].getPOS()));
		}
		return arrWordsInSynset;		
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getPOS()
	 */
	public String getPOS() {
		POS pos = objSynset.getPOS();
		if(pos != null) {
			return Utilities.getHindiPOSString(objSynset.getPOS());
		} else { 
			return null;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getBaseSynset()
	 */
	public Object getBaseSynset(){
		return objSynset;
	}
    
    /* (non-Javadoc)
     * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#containsWord(in.ac.iitb.cfilt.wnlinker.data.GenericWord)
     */
    public boolean containsWord(GenericWord objWord) {
        if(arrWordsInSynset == null) {
            arrWordsInSynset = getWords();    
        }
        
        for(int i=0; i<arrWordsInSynset.length; i++) {
            if(objWord.getLemma().equals(arrWordsInSynset[i].getLemma())) {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objSynset == null) ? 0 : objSynset.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HindiSynset))
			return false;
		final HindiSynset other = (HindiSynset) obj;
		if (objSynset == null) {
			if (other.objSynset != null)
				return false;
		} else if (!(objSynset.getOffset() == (other.objSynset.getOffset())))
			return false;
		return true;
	}
}
