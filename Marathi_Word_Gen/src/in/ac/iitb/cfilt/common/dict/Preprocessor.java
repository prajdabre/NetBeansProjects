/**
 * Project 	 : WSD Engine
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : Preprocessor.java
 *
 * Created On: Dec 15, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.dict;

import in.ac.iitb.cfilt.common.config.AppProperties;
import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.crosslinkH.CrossLinkInfoCreator;
import in.ac.iitb.cfilt.common.data.AlignmentRecord;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.DSFReader;
import in.ac.iitb.cfilt.common.io.UTFReader;
import in.ac.iitb.cfilt.common.io.UTFWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Class	: Preprocessor
 * Purpose	: This class is 
 */
public class Preprocessor {

	private static HashSet<String> FWCategoryList = null;
	
	static
	{
		FWCategoryList = new HashSet<String>();
		BufferedReader FWCatFile=null;
		try {
//			System.out.println(new File(".").getAbsolutePath());
			//TODO: OLD:This absolute path has to be removed and relative path has to be added, solve the prob with adding relative path.
			//XXX FWCatFile = new BufferedReader( new InputStreamReader(new FileInputStream("/media/sda4/workspace/CommonLibrary/src/FWCatList.txt"), "UTF8"), 4096);
			/*while((str = FWCatFile.readLine()) != null)
			{
				FWCategoryList.add(str.trim().toUpperCase());
			}*/
			//TODO: Embed this file in JAR or code to make it portable
			String str=null;
			//XXX while((str = FWCatFile.readLine()) != null)
			

			String[] categories = {"cls", "conj", "intj", "neg", "nst", "num", "particle", "pp", "pron", "quant", "quot"};
			for (int i = 0; i < categories.length; i++) {
				FWCategoryList.add(categories[i].toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method 	: preProcess
	 * Purpose	: Preprocesses a file. If a directory is given as input then 
	 * 			  it processes all the files in the directory 
	 * @param f
	 * @throws MultilingualDictException 
	 * @throws IOException 
	 */
	public void preProcess(File f) throws MultilingualDictException, IOException {
		if (f == null)
			return;
		
		if(f.isDirectory()) {
			File[] children = f.listFiles();
			for(int i=0; i<children.length; i++) {
				preProcess(children[i]);
			}
		} else if( !f.getName().contains("Map") 
				&& f.getName().endsWith(".txt")
				&& !f.getName().endsWith("-words.txt")) {
			
			// Determine Output Directory Name
			String fPath = f.getAbsolutePath();
			int fileExtLocation = fPath.lastIndexOf(".txt");		
			String root = fPath.substring(0, fileExtLocation);
			
			createIndexFiles(f, root);
		}
	}
	
	/**
	 * <p><b>Method</b> 	: createIndexFiles
	 * <p><b>Purpose</b>	: Creates index files for the dictionary.
	 * 			  Two types of indices are created:
	 * 			  <ol>	
	 * 			  <li>For each word in the dictionary an inverted index is created.	 
	 * 			  <li>For synsets an index is created which is sorted on the synsetID and 
	 * 				 contains the synset entryin a single line.
	 * 			  </ol>
	 * <p>@param f
	 * @throws MultilingualDictException 
	 * @throws IOException 
	 */
	private void createIndexFiles(File f, String root) throws MultilingualDictException, IOException {
		if(f == null)
			return;
		
		TreeMap<String, StringBuffer> words = new TreeMap<String, StringBuffer>(new StringComparator());
		TreeMap<String, DSFRecord> synsets = new TreeMap<String, DSFRecord>(new StringComparator());
		TreeMap<String, Vector<String>> tmCrossLinkInfo = new TreeMap<String, Vector<String>>(new StringComparator());
		File file = new File(root);
		file.mkdirs();
		String srcLang = file.getName();
		DSFReader reader = new DSFReader(f);
		DSFRecord record = null;
		reader.open();
		String key = null;
		String recordID = null;
		System.out.println("Reading file " + f.getName());
		while ((record = reader.readEntryFromFile(GlobalConstants.EMPTY)) != null) {
			Vector<String> synsetMembers = record.getWords();
			Vector<AlignmentRecord> vAlignRec = record.getAlignmentRecords();
			recordID = record.getID();
			if(!synsets.containsKey(recordID)) {
				synsets.put(recordID, record);
				Vector<String> clInfo = new Vector<String>();
				for (int i=0; i<synsetMembers.size(); i++) {
					//XXX URDU SPECIFIC CODE ?
					if(f.getName().contains("urd")) {
						if(synsetMembers.elementAt(i).startsWith("_")) {
							synsetMembers.setElementAt(synsetMembers.elementAt(i).substring(1), i);
						}
					}

					key = synsetMembers.elementAt(i) + "\t" + record.getCategory();
					if(words.containsKey(key)) {
						words.get(key).append(" ").append(recordID);
					} else {
						words.put(key, new StringBuffer(" ").append(recordID));
					}
					key = recordID + GlobalConstants.HASH  + i ;
					if(!tmCrossLinkInfo.containsKey(key))
					{
						try{
						String tgtWord = key + "\t" + vAlignRec.get(i).getSourceWord() +"\t" + record.getCategory() + "\t"; 
						//Fix me: Need to put a check for out of bound links
						if(Language.HINDI.toString().toLowerCase().equals(srcLang))
							tgtWord += Language.HINDI.toString() + (i+1);
						else
							tgtWord += vAlignRec.get(i).getTargetWord();

						clInfo.add(tgtWord);
						}catch (ArrayIndexOutOfBoundsException aio)
						{
						}
					}
				}
				tmCrossLinkInfo.put(recordID, clInfo);
			} 
		}
		reader.close();
		
		IndexWriter indexWriter = new IndexWriter(root);
		indexWriter.open();
		Iterator<String> wordsIterator = words.keySet().iterator();
		Iterator<String> synsetsIterator = synsets.keySet().iterator();
		String[] tokens = null;
		while (wordsIterator.hasNext()) {
			key = wordsIterator.next();
			tokens = key.split("\t");
			
			//XXX URDU SPECIFIC CODE ?			
			if(f.getName().contains("urd")) {
				if(tokens[0].startsWith("_")) {
					tokens[0] = tokens[0].substring(1);
					//XXX System.err.println("locha");
				}
			}
				;//U/media/MTP/workspace/CommonLibrary/../resources/Multilingual-Dictionary-database/urdTFConsole.out.println(tokens[1] + ' ' + tokens[0] + ' ' + words +  ' ' + key);
			if (tokens != null && tokens.length==2) {
				indexWriter.writeToWordIndex(getCategory(tokens[1]), tokens[0] + words.get(key));
			}
		}
		
		while (synsetsIterator.hasNext()) {
			key = synsetsIterator.next();
			record = synsets.get(key);
			record.evaluateStatus();
			if(!record.getStatus().equals(GlobalConstants.EMPTY)) {
				indexWriter.writeToSynsetIndex(getCategory(record.getCategory()), record.printInOneLine());
			}
		}
	
		//Stores temporary representation of the cross linking info When SRC Language is not HIN
		String crossLinkeIndexWPath = root + GlobalConstants.CLFILESUFFIX;  
		UTFWriter crossLinkIndexW = new UTFWriter(new File(crossLinkeIndexWPath));
		crossLinkIndexW.open();
		Iterator<String> clIterator = tmCrossLinkInfo.keySet().iterator();
		while (clIterator.hasNext())
		{
			key = clIterator.next();
			Vector<String> clInfo = tmCrossLinkInfo.get(key);
			for(int i=0; i<clInfo.size(); i++)
				crossLinkIndexW.write(clInfo.get(i)+"\n");
		}
		crossLinkIndexW.close();
		indexWriter.close();
	}

	/**
	 * Method 	: preProcess
	 * Purpose	: Preprocesses a file. If a directory is given as input then 
	 * 			  it processes all the files in the directory 
	 * @param f
	 * @throws MultilingualDictException 
	 * @throws IOException 
	 */
	public void updateCrossLinkInfo(File f) throws MultilingualDictException, IOException {
		if (f == null)
			return;
		Vector<String> vFPath = new Vector<String>();
		if(f.isDirectory()) 
		{
			File[] children = f.listFiles();
			for(int i=0; i<children.length; i++) 
			{
				File fchild = children[i];
				if(fchild.isFile() &&  !fchild.getName().contains("Map") 
						&& fchild.getName().endsWith(GlobalConstants.CLFILESUFFIX))
				{
					String fPath = fchild.getAbsolutePath();
					int fileExtLocation = fPath.lastIndexOf(GlobalConstants.CLFILESUFFIX);		
					String root = fPath.substring(0, fileExtLocation);
					
					generateCrossLinkInfo(fchild, root);
					vFPath.add(fPath);
				}
			}
			for(int i=0; i< vFPath.size(); i++)
			{
				(new File(vFPath.get(i))).delete();
			}
		}else if(f.isFile() && !f.getName().contains("Map") && f.getName().endsWith(GlobalConstants.CLFILESUFFIX))
		{
			String fPath = f.getAbsolutePath();
			int fileExtLocation = fPath.lastIndexOf(GlobalConstants.CLFILESUFFIX);
			String root = fPath.substring(0, fileExtLocation);
			generateCrossLinkInfo(f, root);
		}
	}
	
	private void generateCrossLinkInfo(File f, String root) throws MultilingualDictException, IOException 
	{
		String srcLang = f.getName().substring(0, f.getName().lastIndexOf(GlobalConstants.CLFILESUFFIX)); 
		Vector<String> vLangs = new Vector<String>();
		File fParent = f.getParentFile();
		String parentPath = fParent.getAbsolutePath();
		if(fParent != null)
		{
			File[] children = fParent.listFiles();
			for(int i=0; i<children.length; i++)
			{
				String fileName = children[i].getName();
				if(children[i].isFile() && fileName.endsWith(GlobalConstants.CLFILESUFFIX))
					vLangs.add(fileName.substring(0,fileName.lastIndexOf(GlobalConstants.CLFILESUFFIX)));
			}
		}
		System.out.println("\n-----------------------------------------");
		System.out.println("Generating cross-link information for "+srcLang +"\n");
		TreeMap<String, String> crossLinks = new TreeMap<String, String>();
		
		UTFReader srcLangFile = new UTFReader(f);
		srcLangFile.open();
		String strLine = null;
		CrossLinkInfoCreator clInfoCreator = new CrossLinkInfoCreator();
		while((strLine = srcLangFile.readLine()) != null)
		{
			String[] strCLInfo = strLine.split("\\s+");
			if(strCLInfo.length == 4)
			{
				String synsetID = strCLInfo[0].substring(0,strCLInfo[0].lastIndexOf(GlobalConstants.HASH)); 
				String word = strCLInfo[1];
				StringBuilder sb = new StringBuilder();
				sb.append(word);
				sb.append(synsetID);
				sb.append("::");
				sb.append(strCLInfo[2]);
				sb.append("\t");
				for(int lCount=0; lCount< vLangs.size(); lCount++)
				{
					String tgtLang = vLangs.get(lCount);
					if(!tgtLang.toUpperCase().equals(srcLang.toUpperCase()))
					{
						String crossLinkedWord = clInfoCreator.getCrossLinkedWord(word, synsetID, strCLInfo[3],
								srcLang, tgtLang, parentPath);
						if(crossLinkedWord == null)
							crossLinkedWord = "";
						sb.append(tgtLang.toUpperCase());
						sb.append("-");
						sb.append(crossLinkedWord.replaceAll("\n", ""));
						sb.append("\t");
					}
				}
				String line = sb.toString();
				if(!line.contains("::")) 
				{
					continue;
				}
				String key = line.substring(0, line.indexOf("\t"));
				crossLinks.put(key, line.substring(line.indexOf("\t")+1));
			}else
			{
				String msg = "Error: Language:"+srcLang+"\tInvalid data:" + strLine.replaceAll("#.*", "");
				if(!CrossLinkInfoCreator.errMsgs.contains(msg))
				{
					CrossLinkInfoCreator.errMsgs.add(msg);
					System.out.println(msg);
				}
				continue;
			}
		}
		srcLangFile.close();
		CrossLinkInfoCreator.errMsgs.clear();
		IndexWriter indexWriter = new IndexWriter(parentPath+File.separator+srcLang, IndexWriter.CROSSLINKWRITER);
		indexWriter.open(IndexWriter.CROSSLINKWRITER);
		String crossLinkData = null;
		String pos = null;
		Iterator<String> crossLinksIterator = crossLinks.keySet().iterator();
		while (crossLinksIterator.hasNext()) {
			String key = crossLinksIterator.next();
			pos = key.substring(key.indexOf("::")+2);
			crossLinkData = crossLinks.get(key);
			indexWriter.writeToCrossLinkIndex(getCategory(pos),
					key.substring(0, key.indexOf("::")) + "\t" + crossLinkData);
		}
		indexWriter.close(IndexWriter.CROSSLINKWRITER);
	}

	
	/**
	 * <p><b>Method</b> 	: main
	 * <p><b>Purpose</b>	: Main method to run the preprocessor 
	 * <p><b>@param args</b>
	 */
	public static void main(String[] args) {
		try {
			AppProperties.load("../properties/Common.properties");
			Preprocessor p = new Preprocessor();
			p.preProcess(new File("/media/MTP/temp/Demo/database"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	class StringComparator implements Comparator<String> {

		public int compare(String word1, String word2) {
//			word1 = word1.split("::")[0];
//			word2 = word2.split("::")[0];
			return word1.compareTo(word2);
			
/*			if(word1.equals(word2)) {
				return 0;
			}
			String lemma1 = word1.split("\t")[0];
			String lemma2 = word2.split("\t")[0];
			int result = lemma1.compareTo(lemma2);
			if(result == 0) {
				result = word1.compareTo(word2);
			}
			return result;
*/
		}
	}
	private String getCategory(String category)
	{
		if(category.equals("NOUN"))
			return category;
		else if(category.equals("VERB"))
			return category;
		else if(category.equals("ADJECTIVE"))
			return category;
		else if(category.equals("ADVERB"))
			return category;
		else if(category.equals("TAM"))
			return "TAM";
		else if(FWCategoryList.contains(category))
			return "FW";

		return "";
	}
}
