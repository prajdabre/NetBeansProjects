/**
 * Project 	 : English-Hindi Wordnet Linking
 * 
 * Team 	 : CFILT, IIT Bombay.
 *
 * File Name : DictionaryWriter.java
 *
 * Created On: Sep 23, 2007
 *
 * Revision History:
 * Modification Date 	Modified By		Comments
 * 
 */
package in.ac.iitb.cfilt.common.dict;

import in.ac.iitb.cfilt.common.Utilities;
import in.ac.iitb.cfilt.common.config.AppProperties;
import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.database.MultiDictDatabaseLayer;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.DSFWriter;
import in.ac.iitb.cfilt.common.io.DirectoryCopy;
import in.ac.iitb.cfilt.common.io.UTFWriter;
import in.ac.iitb.cfilt.getopt.Getopt;
import in.ac.iitb.cfilt.getopt.LongOpt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class	: DictionaryWriter
 * Purpose	: This class is used to generate the dictionary.
 */
public class DictionaryWriter {

	/**
	 * Method 	: generateDictionaryWithIndices
	 * Purpose	: Generates the Dictionary for a particular language along with all the indices for faster access.    
	 * @param language
	 * @throws MultilingualDictException 
	 * @throws IOException 
	 */
	public static void generateDictionaryWithIndices (Language language, String baseDir) 
	throws MultilingualDictException, IOException {
		MultiDictDatabaseLayer database = MultiDictDatabaseLayer.getSingletonInstance();
		Vector<String> vWords = null;
		int iNoOfRecords = 0;
		DSFRecord record = null;
		StringBuilder sb = new StringBuilder();
		File synsetFile = new File(baseDir + File.separator + language.toString().toLowerCase() + ".txt");
		File wordFile = new File(baseDir + File.separator + language.toString().toLowerCase() + "-words.txt");
		synsetFile.getParentFile().mkdirs();
		DSFWriter dsfWriter = new DSFWriter(synsetFile);
		UTFWriter wordsWriter = new UTFWriter(wordFile);
		dsfWriter.open();
		wordsWriter.open();
		int wordCount = 0;
		boolean firstFetch = true;
		while((record = database.fetchAllRecordsOneByOne(language.toString(), firstFetch)) != null) {
			firstFetch = false;
			iNoOfRecords++;
			dsfWriter.writeEntryToFile(record, false);
			vWords = record.getWords();
			for(int j=0; j<vWords.size(); j++) {
				sb = new StringBuilder();
				sb.append(vWords.elementAt(j).replaceAll("\n", ""));
				sb.append(record.getID());
				sb.append("::");
				sb.append(record.getCategory());
				sb.append("\t");
				wordCount++;
				System.err.print("\rNo. of words for " + language + " \t= " + wordCount);
				for(int k=0; k<Language.ALL_LANGS.length; k++) {
					if(language.equals(Language.ALL_LANGS[k])) {
						continue;
					}
					//System.err.println("Fetching for word " + vWords.elementAt(j) 
					//		+ " in language " + Language.ALL_LANGS[k]);
					String crossLinkedWord = database.getCrossLinkedWord(vWords.elementAt(j),
							record.getID(),
							language,
							Language.ALL_LANGS[k]);
					if(crossLinkedWord ==  null) {
						crossLinkedWord = "";
					}
					sb.append(Language.ALL_LANGS[k].toString());
					sb.append("-");
					sb.append(crossLinkedWord.replaceAll("\n", ""));
					sb.append("\t");
				}
				sb.append("\n");
				wordsWriter.write(sb.toString());
			}
		}
		System.err.println("\nNo. of words for " + language + " \t= " + wordCount);
		System.err.println("No. of synsets for " + language + " \t= " + iNoOfRecords);
		dsfWriter.close();
		wordsWriter.close();
	}

	/**
	 * Method 	: generateDictionaryWithIndices
	 * Purpose	: Generates the Dictionary for all languages.    
	 * @throws MultilingualDictException 
	 * @throws IOException 
	 */
	public static void generateDictionaryWithIndices (String baseDir) throws MultilingualDictException, IOException {
		System.err.println("Start Time \t\t= "  + Utilities.getTimeStamp());
		for(int i=0; i<Language.ALL_LANGS.length; i++) {
			System.err.println("\nStart Time for " + Language.ALL_LANGS[i] +"\t= "  + Utilities.getTimeStamp());
			generateDictionaryWithIndices(Language.ALL_LANGS[i], baseDir);
		}
		//generateDictionaryWithIndices(Language.URDU.toString());
	}

	/**
	 * <p><b>Method</b> 	: generateEnglishILMapping
	 * <p><b>Purpose</b>	: Dumps the English IL Mappings.
	 * <p><b>@param baseDir</b>
	 * @throws IOException 
	 * @throws MultilingualDictException 
	 */
	private static void generateEnglishILMapping(String baseDir) throws IOException, MultilingualDictException {
		UTFWriter engILMappingWriter = new UTFWriter(baseDir + File.separator + "EnglishILMap.txt");
		engILMappingWriter.open();
		Vector<String> vMappings = MultiDictDatabaseLayer.getSingletonInstance().fetchAllEngILMappings();
		for(int i=0; i<vMappings.size(); i++) {
			engILMappingWriter.write(vMappings.elementAt(i) + "\n");
		}
		engILMappingWriter.close();
	}
	/**
	 * <p><b>Method</b> 	: generateDictionary
	 * <p><b>Purpose</b>	: Generates dictionary for a given language. 
	 * <p><b>@param language
	 * <p><b>@throws MultilingualDictException</b>
	 */
	public static void generateDictionary (String language) throws MultilingualDictException {
		MultiDictDatabaseLayer database = MultiDictDatabaseLayer.getSingletonInstance();
		HashMap<String, DSFWriter> hmWriters = new HashMap<String, DSFWriter>();
		int iNoOfRecords = 0;

		//Open separate writers for each category.
		for(int i=0; i<POS1.CATEGORIES.length; i++) {
			File f = new File("." + File.separator + "Dictionary" + File.separator 
					+ language.toLowerCase() + File.separator + POS1.CATEGORIES[i] + ".txt");
			f.getParentFile().mkdirs();
			DSFWriter dsfWriter = new DSFWriter(f);
			hmWriters.put(POS1.CATEGORIES[i].toString(), dsfWriter);
			dsfWriter.open();
		}

		DSFWriter dsfWriter = null;
		DSFRecord record = null;
		boolean firstFetch = true;
		while((record = database.fetchAllRecordsOneByOne(language, firstFetch)) != null) {
			firstFetch = false;
			iNoOfRecords++;
			dsfWriter = hmWriters.get(record.getCategory());
			if(dsfWriter != null) {
				dsfWriter.writeEntryToFile(record, false);
			}
		}
		System.out.println("No. of synsets for " + language + " = " + iNoOfRecords);

		//Close all handles.
		for(int i=0; i<POS1.CATEGORIES.length; i++) {
			hmWriters.get(POS1.CATEGORIES[i].toString()).close();
		}		
	}

	public static void prevMain(String args[]) throws MultilingualDictException, IOException {
		//ApplicationController.initialize();
		AppProperties.load("../properties/Common.properties");
		AppProperties.setProperty("source.language.choice", "");
		AppProperties.setProperty("target.language.choice", "");
		AppProperties.setProperty("project.name", GlobalConstants.PROJECT_ELIL_MT);
		MultiDictDatabaseLayer.getSingletonInstance().initialize();
		MultiDictDatabaseLayer.getSingletonInstance().initDB();
/*		generateDictionaryWithIndices(".." + File.separator + "resources" +  File.separator + "Multilingual-Dictionary-database");
		Preprocessor p = new Preprocessor();
		p.preProcess(new File(".." + File.separator + "resources" +  File.separator + "Multilingual-Dictionary-database"));
*/
		generateEnglishILMapping(".." + File.separator + "resources" +  File.separator + "Multilingual-Dictionary-database");
	}

	public static void main(String[] args) throws MultilingualDictException, IOException {
		if(true) {
			//prevMain(args);
			System.exit(0);
		}
		//ApplicationController.initialize();
		
		//XXX commented out for Dictionary Update Tool
		/*AppProperties.load("../properties/Common.properties");
		AppProperties.setProperty("source.language.choice", "");
		AppProperties.setProperty("target.language.choice", "");
		AppProperties.setProperty("project.name", GlobalConstants.PROJECT_ILIL_MT);
		*/
		//MultiDictDatabaseLayer.getSingletonInstance().initialize();
		//MultiDictDatabaseLayer.getSingletonInstance().initDB();
		//generateDictionaryWithIndices();


		String inputDirectoryName = null, outputDirectoryName = null;
		Language sourceLanguage = null, targetLanguage = null;

		LongOpt[] longopts = new LongOpt[1];		
		longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
		Getopt g = new Getopt("DictionaryWriter", args, "-:i:o:s:t:h", longopts);
		g.setOpterr(false);
		int c;

		try {
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 0:
				case 'h':	
					printUsage();			
					System.exit(0);

				case 1:
				case 'o':
					outputDirectoryName = g.getOptarg();
					break;
					//
				case 2:
				case 'i':
					inputDirectoryName = g.getOptarg();
					break;
					//
				case 3:
				case 's':
					sourceLanguage = Language.getLanguage(g.getOptarg());
					break;
					//
				case 4:
				case 't':
					targetLanguage = Language.getLanguage(g.getOptarg());
					break;
					//

				case ':':
					System.out.println("You need an argument for option "
							+ (char) g.getOptopt());
					System.exit(-3);
					//
				case '?':
					System.out.println("The option '" + (char) g.getOptopt()
							+ "' is not valid");
					System.exit(-3);
					//
				default:
					System.out.println("getopt() returned " + c);
				break;
				}
			}
		} catch(IllegalArgumentException e) {
			printUsage();
			System.exit(-2);
		}

		if(inputDirectoryName == null || outputDirectoryName == null) {
			printUsage();
			System.exit(-1);
		}

		if(sourceLanguage == null && targetLanguage != null || 
				sourceLanguage != null && targetLanguage == null ) {
			printUsage();
			System.exit(-1);
		}


		Preprocessor p = new Preprocessor();

		File inputDirectory = new File(inputDirectoryName);		
		if(sourceLanguage == null && targetLanguage == null) {	// No language specified. Process for all languages
			p.preProcess(inputDirectory);

			if(inputDirectory.isDirectory()) {
				File[] children = inputDirectory.listFiles();
				for(int i=0; i<children.length; i++) {
					if( !children[i].getName().contains("Map") && children[i].getName().endsWith(".txt")) {
						String languageFileName = children[i].getName();												
						int fileExtLocation = languageFileName.lastIndexOf(".txt");		
						String currLangDirName = languageFileName.substring(0, fileExtLocation);						

						try {
							if(!inputDirectoryName.equals(outputDirectoryName)) {
								DirectoryCopy.moveDirectory(
										new File(inputDirectoryName + File.separator + currLangDirName),	
										new File(outputDirectoryName + File.separator + currLangDirName));
							}
						} catch (IOException e) {
							System.err.println("Failed whild creating output directory or files. " +
									"Please check whether you have sufficient permission and free space " +
							"for the specified output directory.");

						}
					}
				}
			} 

		} else {	// Work on individual language files
			String inputLangDir = sourceLanguage.toString().toLowerCase(); 
			String outputLangDir = targetLanguage.toString().toLowerCase();
			p.preProcess(new File(inputDirectoryName + File.separator + inputLangDir + ".txt"));			
			p.preProcess(new File(inputDirectoryName + File.separator + outputLangDir + ".txt"));				
			try {
				if(!inputDirectoryName.equals(outputDirectoryName)) {
					DirectoryCopy.moveDirectory(
							new File(inputDirectoryName + File.separator + inputLangDir),
							new File(outputDirectoryName + File.separator + inputLangDir)
							);
					DirectoryCopy.moveDirectory(
							new File(inputDirectoryName + File.separator + outputLangDir),
							new File(outputDirectoryName + File.separator + outputLangDir)
							);
				}
			} catch (IOException e) {
				System.err.println("Failed whild creating output directory or files. " +
						"Please check whether you have sufficient permission and free space " +
				"for the specified output directory.");
			}
		}
		
		System.out.println("\nINFO: \t Updated all files at target location.");
	}

	private static void printUsage() {
		System.out.println("Usage:\n java in.ac.iitb.cfilt.common.dict.DictionaryWriter " +
				" [-options]\n" +
				" where options include\n" +
				" -h --help\t\t for help on sample usage\n" +
				" -i \t\t input directory (mandatory) \n" +
				" -o \t\t output directory (mandatory) \n" +
				" -s \t\t source language (3-letter language code, optional) \n" +
				" -t \t\t target language (3-letter language code, optional) \n" +
				"If source and target languages are not specified, all files " +
				"present in input directory are processed.");		
	}	
}
