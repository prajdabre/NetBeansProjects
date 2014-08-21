package in.ac.iitb.cfilt.lte;



import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.dict.Preprocessor;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.common.io.DirectoryCopy;
import in.ac.iitb.cfilt.getopt.Getopt;
import in.ac.iitb.cfilt.getopt.LongOpt;

import java.io.File;
import java.io.IOException;

public class UpdateDictionary {
	public static void main(String[] args) throws MultilingualDictException, IOException {
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
		Getopt g = new Getopt("UpdateDictionary", args, "-:i:o:s:t:h", longopts);
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
			try{
				p.preProcess(inputDirectory);
				p.updateCrossLinkInfo(inputDirectory);
			}catch (MultilingualDictException e){
				System.out.println(e.getMessage());
			}
			finally
			{
				File[] children = inputDirectory.listFiles();
				for(int i=0; i< children.length; i++)
				{
					File child = children[i];
					if(child.isFile() && child.getName().endsWith(GlobalConstants.CLFILESUFFIX))
					{
						child.delete();
					}
				}
			}

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
			String prefix = inputDirectoryName + File.separator;
			String srcCLFileName = prefix + inputLangDir+GlobalConstants.CLFILESUFFIX;
			String tgtCLFileName = prefix + outputLangDir+GlobalConstants.CLFILESUFFIX;
			try
			{
				p.preProcess(new File(prefix + inputLangDir + ".txt"));			
				p.preProcess(new File(prefix+ outputLangDir + ".txt"));
				p.updateCrossLinkInfo(new File(srcCLFileName));
				p.updateCrossLinkInfo(new File(tgtCLFileName));
			}finally
			{
				File srcCLFile = new File(srcCLFileName);
				if(srcCLFile.exists())
					srcCLFile.delete();
				File tgtCLFile = new File(tgtCLFileName);
				if(tgtCLFile.exists())
					tgtCLFile.delete();
			}
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
		
		System.out.println("\nINFO: \t Updated files at target location.");
	}

	private static void printUsage() {
		System.out.println("Usage:\n java in.ac.iitb.cfilt.lte.common.dict.DictionaryWriter " +
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
