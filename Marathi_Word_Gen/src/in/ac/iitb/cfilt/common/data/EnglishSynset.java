package in.ac.iitb.cfilt.common.data;

import in.ac.iitb.cfilt.common.Utilities;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;

/**
 * Class	: EnglishSynset
 * Purpose	: This class is 
 */
public class EnglishSynset extends GenericSynset implements SerializableData {

	/**
	 * Useful when this class is Serialized
	 */
	static final long serialVersionUID = 34686728478L;
	
	/**
	 * This field stores the handle to the English Synset.
	 */
	private Synset objSynset = null;
	
    /**
     * This field stores the words belonging to this Synset.
     */
    private GenericWord[] objWordsInSynset = null;

    
	/**
	 * Constructor
	 * @param objSynset
	 * 
	 * To create a clone of the JWNL Synset Object
	 */
	public EnglishSynset(Synset objSynset) {
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
		Word[] objWords = objSynset.getWords();
        objWordsInSynset = new GenericWord[objWords.length];
		for(int i=0; i<objWordsInSynset.length; i++) {
            objWordsInSynset[i] = new GenericWord(objWords[i].getLemma(), Utilities.getEnglishPOSString(objWords[i].getPOS()));
		}
		return objWordsInSynset;
	}
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getPOS()
	 */
	public String getPOS() {
		return Utilities.getEnglishPOSString(objSynset.getPOS());
	}

    /* (non-Javadoc)
     * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#containsWord(in.ac.iitb.cfilt.wnlinker.data.GenericWord)
     */
    public boolean containsWord(GenericWord objWord) {
        if(objWordsInSynset == null) {
            objWordsInSynset = getWords();    
        }
        
        for(int i=0; i<objWordsInSynset.length; i++) {
            if(objWord.getLemma().equals(objWordsInSynset[i].getLemma())) {
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
		if (!(obj instanceof EnglishSynset))
			return false;
		final EnglishSynset other = (EnglishSynset) obj;
		if (objSynset == null) {
			if (other.objSynset != null)
				return false;
		} else if (!((objSynset.getOffset() == other.objSynset.getOffset())
					&& objSynset.getPOS() == other.objSynset.getPOS()))
			return false;
		return true;
	}
    
	
	
	/* (non-Javadoc)
	 * @see in.ac.iitb.cfilt.wnlinker.data.GenericSynset#getBaseSynset()
	 */
	public Object getBaseSynset(){
		return objSynset;
	}
	
}
