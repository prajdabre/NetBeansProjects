/**
 * Project : English-Hindi Wordnet Linking
 *
 * Team : CFILT, IIT Bombay.
 *
 * File Name : LexicalTransferEngine.java
 *
 * Created On: Sep 23, 2007
 *
 * Revision History: Modification Date Modified By	Comments
 *
 */
package in.ac.iitb.cfilt.lte;

import in.ac.iitb.cfilt.common.config.GlobalConstants;
import in.ac.iitb.cfilt.common.data.DSFRecord;
import in.ac.iitb.cfilt.common.data.Language;
import in.ac.iitb.cfilt.common.data.POS1;
import in.ac.iitb.cfilt.common.exception.MultilingualDictException;
import in.ac.iitb.cfilt.getopt.Getopt;
import in.ac.iitb.cfilt.getopt.LongOpt;
import in.ac.iitb.cfilt.multidict.dictionary.Dictionary;
import in.ac.iitb.cfilt.multidict.dictionary.FileBackedDictionary;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import sanchay.corpus.ssf.SSFProperties;
import sanchay.corpus.ssf.SSFSentence;
import sanchay.corpus.ssf.SSFStory;
import sanchay.corpus.ssf.features.FeatureAttribute;
import sanchay.corpus.ssf.features.FeatureStructure;
import sanchay.corpus.ssf.features.FeatureValue;
import sanchay.corpus.ssf.features.impl.FSProperties;
import sanchay.corpus.ssf.features.impl.FeatureStructuresImpl;
import sanchay.corpus.ssf.features.impl.FeatureValueImpl;
import sanchay.corpus.ssf.impl.SSFStoryImpl;
import sanchay.corpus.ssf.tree.SSFNode;

/**
 * Class	: LexicalTransferEngine Purpose	: This class is used to find the
 * translation of a source language word to the target language.
 */
public class LexicalTransferEngine {

    /**
     * This field stores the path of the USER_HOME variable.
     */
    public static String USER_HOME = null;
    /**
     * This field stores the logger for this class.
     */
    private static Logger logger = Logger.getLogger(LexicalTransferEngine.class);
    /**
     * This field stores a handle to the user dictionary object.
     */
    private Dictionary userDict = null;
    /**
     * This field stores a handle to the system dictionary object.
     */
    private Dictionary systemDict = null;
    /**
     * This field stores a handle to the session dictionary object.
     */
    private Dictionary sessionDict = null;
    private Dictionary NNPDict = null;
    private Dictionary BilingualDict = null;
    private static String AT = "@";
    private static String PUNC = "punc";
    public static String userDictPath = null;
    public static String sessionDictPath = null;
    public static String systemDictPath = null;
    public static String NNPDictPath = null;
    public static String BilingualDictPath = null;
    public static Language sourceLanguage = null;
    public static Language targetLanguage = null;
    private static boolean usePOSTags = false;
    public static String unrecognized = "/unrecognized_words.txt";
    public static String conjunctions = "/data_bin/sl_tl/lexicaltransfer/system_dict/mar-hin/mar/conj_pairs";
    public static String pronouns = "/data_bin/sl_tl/lexicaltransfer/system_dict/mar-hin/mar/pron_pairs";
    public static String synsetrank = "/data_bin/sl_tl/lexicaltransfer/system_dict/mar-hin/mar/rankingtable";
    public static BufferedWriter bw = null;
    public static HashMap<String, Vector<String>> conjlist = new HashMap<String, Vector<String>>();
    public static HashMap<String, Vector<String>> pronlist = new HashMap<String, Vector<String>>();
    public static Vector<String> first_rank_synset_list = new Vector<String>();

    /**
     * <p><b>Method</b> : initialize <p><b>Purpose</b>	: Initializes the Lexical
     * Transfer Engine with the correct dictionary path. <p><b>@param
     * dictionaryPath - The base directory for the location of the dictionary.
     * <p><b>@throws MultilingualDictException</b>
     */
    public void initialize(String USER_HOME, boolean openUserDict, boolean openSessionDict, boolean openSystemDict,
            boolean openNNPDict, boolean openBilingualDict) throws MultilingualDictException {
        logger.info("Initializing Lexical Transfer Engine");
        sourceLanguage = Language.MARATHI;
        targetLanguage = Language.HINDI;
        this.USER_HOME = USER_HOME;
        unrecognized = USER_HOME + unrecognized;
        conjunctions = USER_HOME + conjunctions;
        pronouns = USER_HOME + pronouns;
        synsetrank = USER_HOME + synsetrank;
        //String dictionaryPath = USER_HOME + File.separator + 
        //						"data_bin" + File.separator +
        //						"sl_tl" + File.separator + "lexicaltransfer";

        String default_system_dict_path = USER_HOME + File.separator + "data_bin" + File.separator
                + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "system_dict";
        // + File.separator + sourceLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase();
        String default_user_dict_path = USER_HOME + File.separator + "data_bin" + File.separator
                + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "user_dict";
        // + File.separator+ sourceLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase(); 
        String default_session_dict_path = USER_HOME + File.separator + "data_bin" + File.separator
                + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "session_dict";
        // + File.separator + sourceLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase();		
        String default_NNP_dict_path = USER_HOME + File.separator + "data_bin" + File.separator
                + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "NNP_dict";
        // + File.separator + sourceLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase();		
        String default_Bilingual_dict_path = USER_HOME + File.separator + "data_bin" + File.separator
                + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "Bilingual_dict";
        // 	+ File.separator + sourceLanguage.toString().toLowerCase() + "-" + targetLanguage.toString().toLowerCase();		

        if (openUserDict) {
            userDict = Dictionary.getInstance("userDict");
            if (userDictPath != null) {
                String userDictPathSt = getDefaultPath(userDictPath,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(userDictPathSt)) {
                    userDictPathSt = getDefaultPath(userDictPath,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(userDictPathSt)) {
                        logger.info("Dictionary path " + userDictPathSt + " does not exist. Please specify the corect dictionary path");
                    }
                }
                userDict.initialize(userDictPathSt, sourceLanguage, targetLanguage);	// init after getting path
            } else {
                String default_user_dict_path_st = getDefaultPath(default_user_dict_path,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(default_user_dict_path_st)) {
                    default_user_dict_path_st = getDefaultPath(default_user_dict_path,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(default_user_dict_path_st)) {
                        logger.info("Default dictionary path " + default_user_dict_path_st + " does not exist. Please specify the dictionary path");
                    }
                }
                userDict.initialize(default_user_dict_path_st, sourceLanguage, targetLanguage);	// init after getting path
            }
        }

        if (openSessionDict) {
            sessionDict = Dictionary.getInstance("sessionDict");
            if (sessionDictPath != null) {
                String sessionDictPathSt = getDefaultPath(sessionDictPath,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(sessionDictPathSt)) {
                    sessionDictPathSt = getDefaultPath(sessionDictPath,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(sessionDictPathSt)) {
                        logger.info("Dictionary path " + sessionDictPathSt + " does not exist. Please specify the corect dictionary path");
                    }
                }
                sessionDict.initialize(sessionDictPathSt, sourceLanguage, targetLanguage);	// init after getting path
            } else {
                String default_session_dict_path_st = getDefaultPath(default_session_dict_path,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(default_session_dict_path_st)) {
                    default_session_dict_path_st = getDefaultPath(default_session_dict_path,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(default_session_dict_path_st)) {
                        logger.info("Default dictionary path " + default_session_dict_path_st + " does not exist. Please specify the dictionary path");
                    }
                }
                sessionDict.initialize(default_session_dict_path_st, sourceLanguage, targetLanguage);	// init after getting path
            }
        }

        if (openSystemDict) {
            systemDict = Dictionary.getInstance("systemDict");
            if (systemDictPath != null) {
                String systemDictPathSt = getDefaultPath(systemDictPath,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(systemDictPathSt)) {
                    systemDictPathSt = getDefaultPath(systemDictPath,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(systemDictPathSt)) {
                        logger.info("Dictionary path " + systemDictPathSt + " does not exist. Please specify the corect dictionary path");
                    }
                }
                systemDict.initialize(systemDictPathSt, sourceLanguage, targetLanguage);	// init after getting path
            } else {
                String default_system_dict_path_st = getDefaultPath(default_system_dict_path,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(default_system_dict_path_st)) {
                    default_system_dict_path_st = getDefaultPath(default_system_dict_path,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(default_system_dict_path_st)) {
                        logger.info("Default dictionary path " + default_system_dict_path_st + " does not exist. Please specify the dictionary path");
                    }
                }
                systemDict.initialize(default_system_dict_path_st, sourceLanguage, targetLanguage);	// init after getting path
            }
        }

        if (openNNPDict) {
            NNPDict = Dictionary.getInstance("NNPDict");
            if (NNPDictPath != null) {
                String NNPDictPathSt = getDefaultPath(NNPDictPath,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(NNPDictPathSt)) {
                    NNPDictPathSt = getDefaultPath(NNPDictPath,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(NNPDictPathSt)) {
                        logger.info("Dictionary path " + NNPDictPathSt + " does not exist. Please specify the corect dictionary path");
                    }
                }
                NNPDict.initialize(NNPDictPathSt, sourceLanguage, targetLanguage);	// init after getting path
            } else {
                String default_NNP_dict_path_st = getDefaultPath(default_NNP_dict_path,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(default_NNP_dict_path_st)) {
                    default_NNP_dict_path_st = getDefaultPath(default_NNP_dict_path,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(default_NNP_dict_path_st)) {
                        logger.info("Default dictionary path " + default_NNP_dict_path_st + " does not exist. Please specify the dictionary path");
                    }
                }
                NNPDict.initialize(default_NNP_dict_path_st, sourceLanguage, targetLanguage);	// init after getting path
            }
        }
        if (openBilingualDict) {
            BilingualDict = Dictionary.getInstance("BilingualDict");
            if (BilingualDictPath != null) {
                String BilingualDictPathSt = getDefaultPath(BilingualDictPath,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(BilingualDictPathSt)) {
                    BilingualDictPathSt = getDefaultPath(BilingualDictPath,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(BilingualDictPathSt)) {
                        logger.info("Dictionary path " + BilingualDictPathSt + " does not exist. Please specify the corect dictionary path");
                    }
                }
                BilingualDict.initialize(BilingualDictPathSt, sourceLanguage, targetLanguage);	// init after getting path
            } else {
                String default_Bilingual_dict_path_st = getDefaultPath(default_Bilingual_dict_path,
                        sourceLanguage.toString().toLowerCase(), targetLanguage.toString().toLowerCase());
                if (!checkIfDirectoryExists(default_Bilingual_dict_path_st)) {
                    default_Bilingual_dict_path_st = getDefaultPath(default_Bilingual_dict_path,
                            targetLanguage.toString().toLowerCase(), sourceLanguage.toString().toLowerCase());
                    if (!checkIfDirectoryExists(default_Bilingual_dict_path_st)) {
                        logger.info("Default dictionary path " + default_Bilingual_dict_path_st + " does not exist. Please specify the dictionary path");
                    }
                }
                BilingualDict.initialize(default_Bilingual_dict_path_st, sourceLanguage, targetLanguage);	// init after getting path
            }
        }
        try {
            bw = new BufferedWriter(new FileWriter(unrecognized, true));
            BufferedReader reader = new BufferedReader(new FileReader(conjunctions));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String linecont[] = line.split("\t");
                if (conjlist.containsKey(linecont[1])) {
                    Vector<String> temp = conjlist.get(linecont[1]);
                    temp.add(linecont[2]);
                    conjlist.put(linecont[1], temp);
                } else {
                    Vector<String> temp = new Vector<String>();
                    temp.add(linecont[2]);
                    conjlist.put(linecont[1], temp);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	
	try {
            //bw = new BufferedWriter(new FileWriter(unrecognized, true));
            BufferedReader reader = new BufferedReader(new FileReader(pronouns));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String linecont[] = line.split("\t");
                if (pronlist.containsKey(linecont[1])) {
                    Vector<String> temp = pronlist.get(linecont[1]);
                    temp.add(linecont[2]);
                    pronlist.put(linecont[1], temp);
                } else {
                    Vector<String> temp = new Vector<String>();
                    temp.add(linecont[2]);
                    pronlist.put(linecont[1], temp);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

	try {
            //bw = new BufferedWriter(new FileWriter(unrecognized, true));
            BufferedReader reader = new BufferedReader(new FileReader(synsetrank));
            String line = "";
            while ((line = reader.readLine()) != null) {
				String linecont[] = line.trim().split("\t");
				if(linecont.length<=2){
					continue;
				}
                if(linecont[2].equals("1")){
					first_rank_synset_list.add(linecont[0]);
				}
                
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //TODO logger.info(dictionaryPath);
        //EnglishILMapper.initialize(dictionaryPath + File.separator + "EnglishILMap.txt");
    }

    /**
     * <p><b>Method</b> : translate <p><b>Purpose</b>	: Translates the
     * inputFileName from sourceLanguage to targetLanguage and stores the output
     * in outputFileName <p><b>@param inputFileName - The name of the input
     * file/directory. <p><b>@param outputFileName- The name of the output
     * file/directory. <p><b>@param sourceLanguage- source language.
     * <p><b>@param targetLanguage- target language. <p><b>@throws
     * MultilingualDictException</b>
     */
    public void translate(String inputFileName,
            String outputFileName, Language sourceLanguage, Language targetLanguage)
            throws MultilingualDictException {
        File input = new File(inputFileName);
        /*
         * if(input.isDirectory()) { File[] children = input.listFiles();
         * for(int i=0; i<children.length; i++) {
         * translate(children[i].getAbsolutePath(), outputFileName +
         * File.separator + children[i].getName(), sourceLanguage,
         * targetLanguage); } } else { if(!outputFileName.endsWith(".txt") &&
         * !outputFileName.endsWith(".out")) { outputFileName = outputFileName +
         * File.separator + input.getName(); }
         */ transfer(input.getAbsolutePath(),
                outputFileName,
                sourceLanguage,
                targetLanguage);
        //}
    }

    /**
     * <p><b>Method</b> : transfer <p><b>Purpose</b>	: translates the file to
     * target language. <p><b>@param inputFileName - Input file name.
     * <p><b>@param outputFileName- Output file name. <p><b>@param
     * sourceLanguage- Source Language <p><b>@param targetLanguage- Target
     * Language <p><b>@throws MultilingualDictException</b>
     */
    private void transfer(String inputFileName, String outputFileName, Language sourceLanguage, Language targetLanguage) throws MultilingualDictException {
        try {
            String chunk_name = "";
            String Head = "", Aspect = "", Tense = "", Mood = "", Poslcat = "";
            FeatureAttribute nodefaWord2 = null;
            FeatureAttribute attrib2 = null;
            String word = "", lcat = "", gen = "", num = "", per = "", cas = "", vib = "", tam = "";
            String result = "";
	    String pahije[] = {"चाहिए"};
            logger.info("Translating file " + inputFileName);
            File output = null;
            if (outputFileName != null) {
                output = new File(outputFileName).getParentFile();
                if (output != null && !output.exists()) {
                    output.mkdirs();
                }
            }

            FSProperties fsp = new FSProperties();
            SSFProperties ssfp = new SSFProperties();
            SSFProperties cmlp = new SSFProperties();
            SSFStory story = new SSFStoryImpl();
            String SANCHAY_HOME = USER_HOME + File.separator + "data_bin" + File.separator
                    + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "Sanchay";

            logger.debug("SANCHAY_HOME = " + SANCHAY_HOME);
            fsp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "fs-mandatory-attribs.txt",
                    SANCHAY_HOME + File.separator + "props" + File.separator + "fs-props.txt", "UTF-8");
            ssfp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "ssf-props.txt", "UTF-8");
            cmlp.read(SANCHAY_HOME + File.separator + "props" + File.separator + "cml-props.txt", "UTF-8");
            FeatureStructuresImpl.setFSProperties(fsp);
            SSFNode.setSSFProperties(ssfp);
            SSFNode.setCMLProperties(cmlp);

            story.readFile(inputFileName);

            int scount = story.countSentences();//scount store the number of sentences ( no of {<Sentence id="1">,</Sentence>}) in input file 
            for (int i = 0; i < scount; i++) {
				
                SSFSentence sen = story.getSentence(i);
                int ccount = sen.getRoot().countChildren();// It count the no of line between ( {<Sentence id="1">,</Sentence>})

                for (int j = 0; j < ccount; j++) {
                    SSFNode node = sen.getRoot().getChild(j);//It store the roots of the lines within the {<Sentence id="1">,</Sentence>} Ex- 1	((	VGF	<fs af='वाचणे,v,,sg,1,,@_रहा_है,@त' head='वाचत' aspect='h' tense='pas' mood='in'>
//1.1	@वाच	VM	<fs af='@वाच,v,,pl,3,,ते_हुए,त' aspect='h' tense='pas' mood='in' name='वाचत'>
//1.2	@.	SYM	<fs af='@.,pun,,,,,,' poslcat='NM'>
//	))
// node will store the VGF not VM or SYM.Means name of the chunck.

                    chunk_name = node.getName();
                    String sourceWord = "", sourceTam = "";
                    String targetWords[] = null, targetTams[] = null;
                    FeatureAttribute nodefaWord = null;
                    FeatureAttribute nodefatam = null;
                    FeatureAttribute nodefapos = null;
                    POS1 pos = null;

                    String tempName;
                    boolean isKridanta = false;
                    String chunkNameType = "";		// obtained from node.getName();
                    if ((tempName = node.getName()) != null) {
                        if (tempName.equals("VGF")) {
                            chunkNameType = "VGF";
                        } else if (tempName.equals("VGNF")) {
                            chunkNameType = "VGNF";
                        } else if (tempName.equals("VGINF")) {
                            chunkNameType = "VGINF";
                        } else if (tempName.equals("VGNN")) {
                            chunkNameType = "VGNN";
                        } else {
                            chunkNameType = "";
                        }
                    }

                    String synsetId = null;
                    String lexWord = node.getLexData();//It store null value.
                    sourceWord = lexWord.trim();
                    String[] rootWd = new String[1];
                    //System.out.println(lexWord);




                    if (node.getFeatureStructures() != null && node.getFeatureStructures().getChildCount() > 0) {
                        FeatureStructure nodefs = node.getFeatureStructures().getAltFSValue(0);
                        nodefaWord = nodefs.getAttribute(0);
                        if (nodefaWord != null) {
                            word = nodefaWord.getAltValue(0).toString();
                            //	System.out.println(word);
                        }
                        nodefaWord = nodefs.getAttribute(1);
                        if (nodefaWord != null) {
                            lcat = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(2);
                        if (nodefaWord != null) {
                            gen = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(3);
                        if (nodefaWord != null) {
                            num = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(4);
                        if (nodefaWord != null) {
                            per = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(5);
                        if (nodefaWord != null) {
                            cas = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(6);
                        if (nodefaWord != null) {
                            vib = nodefaWord.getAltValue(0).toString();
                        }

                        nodefaWord = nodefs.getAttribute(7);
                        if (nodefaWord != null) {
                            tam = nodefaWord.getAltValue(0).toString();
                        }
                        nodefaWord = nodefs.getAttribute(0); // The one who did not add this line is a "genius"
                        // Code for accessing other keys value such as head, aspect, tense, mood etc.

                        int countAttrib2 = nodefs.countAttributes();
                        FeatureAttribute attrib4 = null;
                        boolean isNamedEntity2 = false;
                        for (int m = 0; m < countAttrib2; m++) {
                            attrib4 = nodefs.getAttribute(m);
                            if (attrib4 != null) {
                                isNamedEntity2 = true;
                                if (attrib4.getName().equalsIgnoreCase("head")) {
                                    Head = (String) attrib4.getAltValue(0).getValue();
                                }
                                if (attrib4.getName().equalsIgnoreCase("aspect")) {
                                    Aspect = (String) attrib4.getAltValue(0).getValue();
                                }
                                if (attrib4.getName().equalsIgnoreCase("tense")) {
                                    Tense = (String) attrib4.getAltValue(0).getValue();
                                }
                                if (attrib4.getName().equalsIgnoreCase("mood")) {
                                    Mood = (String) attrib4.getAltValue(0).getValue();
                                }
                            }
                        }

                        // System.out.println("Head : "+Head);
                        //  System.out.println("Aspect : "+Aspect);
                        //  System.out.println("Tense : "+Tense);
                        //  System.out.println("Mood : "+Mood);
                        //result = num + " " + per + " " + vib; 
                        result = num + " " + per + " " + vib + " " + Tense;
                        // System.out.println("Result : "+result);
                        //    System.out.println("Result : "+tam);

                        /*
                         * if(vib.contains("अस")) {
                         *
                         * //System.out.println("sss"); //
                         * System.out.println(chunk_name); *
                         *
                         * if(chunk_name.equals("VGF")) {
                         * //System.out.println("ppp"); String
                         * t=compare2(result); //System.out.println(t);
                         *
                         * if(t != null) {
                         *
                         * FeatureValue temp2 = new FeatureValueImpl();
                         * temp2.setValue(t); nodefs.modifyAttributeValue(temp2
                         * ,6,0); // FeatureValue temp2 = new
                         * FeatureValueImpl(); // temp2.setValue(t); //
                         * nodefs.modifyAttributeValue(temp2 ,7,0);
                         *
                         *
                         * }
                         * }
                         * }
                         */


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        if (nodefaWord != null) {
                            sourceWord = nodefaWord.getAltValue(0).toString();
                        }
                        nodefatam = nodefs.getAttribute(6);
                        if (nodefatam != null) {
                            sourceTam = nodefatam.getAltValue(0).toString();
                        }
                        nodefapos = nodefs.getAttribute(1);
                        if (nodefapos != null) {
                            pos = getCategory(nodefapos.getAltValue(0).toString());
                        }
                        if (usePOSTags) {
                            pos = getCategory(node.getName());
                        }
                        int countAttrib = nodefs.countAttributes();//It store the no. of attribute of the first line of chunk.
                        FeatureAttribute attrib = null;
                        boolean isNamedEntity = false;
			boolean isDate = false;
                        String EnamexValue = "";
                        for (int m = 0; m < countAttrib; m++) {
                            attrib = nodefs.getAttribute(m);
                            if (attrib != null) {
                                if (attrib.getName().equalsIgnoreCase("ENAMEX_TYPE")) {
                                    EnamexValue = (String) attrib.getAltValue(0).getValue();
                                }
                                if (EnamexValue.equalsIgnoreCase("person") || EnamexValue.equalsIgnoreCase("location") || EnamexValue.equalsIgnoreCase("organization") || EnamexValue.equalsIgnoreCase("miscellaneous") || EnamexValue.equalsIgnoreCase("facility")) {
                                    isNamedEntity = true;
                                } else {
                                    isNamedEntity = false;
                                }
				if (EnamexValue.equalsIgnoreCase("date")){
						isDate=true;						
					}

                                if (attrib.getName().equalsIgnoreCase("SYNSETID")) {
                                    synsetId = (String) attrib.getAltValue(0).getValue();
                                }
                                if (attrib.getName().equalsIgnoreCase("kridanta_type")) {
                                    FeatureValue fval = attrib.getAltValue(0);
                                   /* if (fval.toString().equals("ne") || isKridanta) {
                                        isKridanta = true;
//System.out.println(sourceWord+" "+isKridanta+" INTERESTING POINT"); //This is DAYAMAX scene in the head whenever a vibhakticompute is done there are 2 fields if kridanta_type and thus the isKridanta becomes true first and then false
                                    } else {
                                        isKridanta = false;
                                    }*/
                                }
                            }
                        }
//System.out.println(sourceWord+" "+isKridanta);
                        sourceWord = sourceWord.trim();
                        sourceTam = sourceTam.trim();
//			System.out.println(sourceWord+" "+isKridanta); 		
                        // If the word is a Named Entity then ignore its translation. It would be transliterated
                        boolean caratPrefixWord = sourceWord.startsWith(GlobalConstants.CARAT);
                        boolean tildaPrefixWord = sourceWord.startsWith(GlobalConstants.TILDA);
                        
                        if (!isNamedEntity && !caratPrefixWord && !tildaPrefixWord) {

                            targetWords = getTranslation(sourceWord, pos, sourceLanguage, targetLanguage, synsetId);

                        }
                        
                         // Fallback to other dictionaries ---- (should put further checks but I am in a hurry for now) Raj Dabre 5/5/2014
                        
                             
                        if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.NOUN, sourceLanguage, targetLanguage, synsetId);

                        }
                        
                        if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.ADJECTIVE, sourceLanguage, targetLanguage, synsetId);

                        }
                        
                        if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.ADVERB, sourceLanguage, targetLanguage, synsetId);

                        }
			
			if(word.equals("पाहिजेणे") || word.equals("पाहिजे")){
				targetWords = pahije;		// Temp fix for pahije	
			}
                        boolean caratPrefixTAM = sourceTam.startsWith(GlobalConstants.CARAT);
                        boolean tildaPrefixTAM = sourceTam.startsWith(GlobalConstants.TILDA);
                        // As WSD is currently working only for content words, we pass false to the function

                        boolean isChangeInRoot = false;
                        rootWd[0] = sourceWord;
//			System.out.println("boom");
                        if (!caratPrefixTAM && !tildaPrefixTAM) {
			    if (isDate && sourceTam.equals("ई")){
				targetTams = new String[1];
				targetTams[0] = "0_को";
		            } else if(lcat.equals("pn") && (sourceTam.equals("झ्या#पुढे") || sourceTam.equals("चा#पुढे") ) ){
				targetTams = new String[1];
				targetTams[0] = "का_आगे";
			    } else{
                            targetTams = processTAM(rootWd, sourceTam, pos, sourceLanguage, targetLanguage, null, isKridanta, chunkNameType, nodefs);
			   }
                            if (!sourceWord.equals(rootWd[0])) {
                                isChangeInRoot = true;
                            }
                            if (isChangeInRoot) {
                                if (targetWords == null) {
                                    targetWords = new String[1];
                                }
                                targetWords[0] = rootWd[0];
                            }
                        }
                        if (targetWords == null || targetWords.length == 0) {
                            if (caratPrefixWord || tildaPrefixWord) {
                                logger.debug(sourceWord + " starts with ^, ^^ or ~");
                                sourceWord = sourceWord.replaceFirst("\\^\\^|\\^", "");
                                node.setLexData(sourceWord);
                                if (nodefaWord != null) {
                                    FeatureValue newaTargetWord = new FeatureValueImpl();
                                    newaTargetWord.setValue(sourceWord);
                                    nodefaWord.modifyAltValue(newaTargetWord, 0);
                                }
                            } else {
                                if (isNamedEntity) {
                                    logger.debug(sourceWord + " is a Named Entity and would be transliterated");
                                } else {
                                    logger.debug("No translation found for " + sourceWord);
                                }

                                if (!nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceWord.trim().equals("")) {
                                    node.setLexData(AT + sourceWord);
                                }

                                if (nodefaWord != null && !nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceWord.trim().equals("")) {
                                    FeatureValue newaTargetWord = new FeatureValueImpl();
                                    newaTargetWord.setValue(AT + sourceWord);
                                    nodefaWord.modifyAltValue(newaTargetWord, 0);
                                }
                            }
                        } else {

                            logger.debug("Translation for " + sourceWord + " is " + targetWords[0]);

                            node.setLexData(targetWords[0]);
                            if (nodefaWord != null) {
                                FeatureValue newaTagetWord = new FeatureValueImpl();
                                newaTagetWord.setValue(targetWords[0]);

                                nodefaWord.modifyAltValue(newaTagetWord, 0);
                            }
                        }

                        // Setting TAM value
                        if (targetTams == null || targetTams.length == 0) {
                            if (caratPrefixTAM || tildaPrefixTAM) {
                                logger.debug(sourceTam + " starts with ^, ^^ or ~");
                                sourceTam = sourceTam.replaceFirst("\\^\\^|\\^", "");
                                if (nodefatam != null) {
                                    FeatureValue newaTam = new FeatureValueImpl();
                                    newaTam.setValue(sourceTam);
                                    nodefatam.modifyAltValue(newaTam, 0);
                                }
                            } else {
                                if (nodefatam != null && !nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceTam.trim().equals("")) {
                                    FeatureValue newaTam = new FeatureValueImpl();
                                    newaTam.setValue(AT + sourceTam);
                                    nodefatam.modifyAltValue(newaTam, 0);
                                    logger.debug("No translation found for TAM " + sourceTam);
                                }
                            }
                        } else if (nodefatam != null) {
                            logger.debug("Translation for " + sourceTam + " is " + targetTams[0]);
                            FeatureValue newaTam = new FeatureValueImpl();
                            newaTam.setValue(targetTams[0]);

                            nodefatam.modifyAltValue(newaTam, 0);
                        }
                    }







                    for (int k = 0; k < node.countChildren(); k++) {
						
                        targetTams = null;
                        targetWords = null;
                        synsetId = null;
                        //System.out.println(1);
                        SSFNode nodeChild = (SSFNode) node.getChildAt(k);
                        sourceWord = nodeChild.getLexData();
                        lexWord = nodeChild.getLexData();
                        sourceWord = lexWord.trim();
                        //System.out.println(lexWord);
                        isKridanta = false;
                        /*
                         * chunkNameType = "";
                         * if((tempName=node.getName())!=null){
                         * if(tempName.equals("VGF")) chunkNameType="VGF"; else
                         * if(tempName.equals("VGNF")) chunkNameType="VGNF";
                         * else if(tempName.equals("VGINF"))
                         * chunkNameType="VGINF"; else chunkNameType = ""; }
                         */
                        if (nodeChild.getFeatureStructures() != null && nodeChild.getFeatureStructures().getChildCount() > 0) {
                            FeatureStructure nodefs = nodeChild.getFeatureStructures().getAltFSValue(0);
                            nodefaWord = nodefs.getAttribute(0);
                            if (nodefaWord != null) {
                                sourceWord = nodefaWord.getAltValue(0).toString();
                            }

                            nodefatam = nodefs.getAttribute(6);
                            if (nodefatam != null) {
                                sourceTam = nodefatam.getAltValue(0).toString();
                            }
                            nodefapos = nodefs.getAttribute(1);
                            if (nodefapos != null) {
                                pos = getCategory(nodefapos.getAltValue(0).toString());
                            }

                            if (usePOSTags) {
                                pos = getCategory(nodeChild.getName());
                            }

                            int countAttrib = nodefs.countAttributes();
                            FeatureAttribute attrib = null;
                            String enamexValue = "";
                            boolean isNamedEntity = false;
			    boolean isDate = false;
                            for (int m = 0; m < countAttrib; m++) {
                                attrib = nodefs.getAttribute(m);
                                if (attrib != null) {
                                    if (attrib.getName().equalsIgnoreCase("ENAMEX_TYPE")) {
                                        enamexValue = (String) attrib.getAltValue(0).getValue();
                                        if (enamexValue.equalsIgnoreCase("person") || enamexValue.equalsIgnoreCase("location") || enamexValue.equalsIgnoreCase("organization") || enamexValue.equalsIgnoreCase("miscellaneous") || enamexValue.equalsIgnoreCase("facility")) {

                                            isNamedEntity = true;
                                        } else {
                                            isNamedEntity = false;
                                        }
					
					if (enamexValue.equalsIgnoreCase("date")){
						isDate=true;						
					}
                                    }
                                    if (attrib.getName().equalsIgnoreCase("SYNSETID")) {
                                        synsetId = (String) attrib.getAltValue(0).getValue();
                                    }
                                    if (attrib.getName().equalsIgnoreCase("kridanta_type")) {
                                        FeatureValue fval = attrib.getAltValue(0);
                                       /* if (fval.toString().equals("ne")) {
                                            isKridanta = true;
						//System.out.println(sourceWord+" "+isKridanta+" INTERESTING POINT");
                                        } else {
                                            isKridanta = false;
                                        } */
                                    }
                                }
                            }
			    //System.out.println("Here:"+enamexValue);
                            sourceWord = sourceWord.trim();
                            sourceTam = sourceTam.trim();
			    //System.out.println("TAM: "+sourceTam);
                            // If the word is a Named Entity then ignore its translation. It would be transliterated
                            boolean caratPrefixWord = sourceWord.startsWith(GlobalConstants.CARAT);
                            boolean tildaPrefixWord = sourceWord.startsWith(GlobalConstants.TILDA);
                            if (!isNamedEntity && !caratPrefixWord && !tildaPrefixWord) {
                                targetWords = getTranslation(sourceWord, pos, sourceLanguage, targetLanguage, synsetId);
                            }
                            
                            // Fallback to other dictionaries ---- (should put further checks but I am in a hurry for now) Raj Dabre 5/5/2014
                             
                            if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.NOUN, sourceLanguage, targetLanguage, synsetId);

                        }
                        
                        if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.ADJECTIVE, sourceLanguage, targetLanguage, synsetId);

                        }
                        
                        if (targetWords==null || targetWords.length == 0) {
						
                            targetWords = getTranslation(sourceWord, POS1.ADVERB, sourceLanguage, targetLanguage, synsetId);

                        }
                            //   if(targetWords!=null)
                            //  for(String s:targetWords){
                            //	System.out.println(s+" "+synsetId);

                            // }
			if(sourceWord.equals("पाहिजेणे") || sourceWord.equals("पाहिजे")){
				targetWords = pahije;		// Temp fix for pahije	
			}
                            rootWd[0] = sourceWord;
                            boolean isChangeInRoot = false;
                            boolean caratPrefixTAM = sourceTam.startsWith(GlobalConstants.CARAT);
                            boolean tildaPrefixTAM = sourceTam.startsWith(GlobalConstants.TILDA);
                            // As WSD is currently working only for content words, we pass false to the function
                            if (!caratPrefixTAM && !tildaPrefixTAM) {
				//System.out.println(isDate+" " +(sourceTam=="ई"));
				if (isDate && sourceTam.equals("ई")){
				targetTams = new String[1];
				targetTams[0] = "0_को";
		            } else if(lcat.equals("pn") && (sourceTam.equals("झ्या#पुढे") || sourceTam.equals("चा#पुढे") ) ){
				targetTams = new String[1];
				targetTams[0] = "का_आगे";
			    } else{
				//System.out.println("Here"+sourceTam+" "+pos);
                                targetTams = processTAM(rootWd, sourceTam, pos, sourceLanguage, targetLanguage, null, isKridanta, chunkNameType, nodefs);
			    }
                                if (!sourceWord.equals(rootWd[0])) {
                                    isChangeInRoot = true;
                                }
                                if (isChangeInRoot) {
                                    if (targetWords == null) {
                                        targetWords = new String[1];
                                    }
                                    targetWords[0] = rootWd[0];
                                }
                            }
			    
                            if (targetWords == null || targetWords.length == 0) {
                                if (caratPrefixWord || tildaPrefixWord) {
                                    logger.debug(sourceWord + " starts with ^, ^^ or ~");
                                    sourceWord = sourceWord.replaceFirst("\\^\\^|\\^", "");
                                    nodeChild.setLexData(sourceWord);

                                    if (nodefaWord != null) {
                                        FeatureValue newalue = new FeatureValueImpl();
                                        newalue.setValue(sourceWord);
                                        nodefaWord.modifyAltValue(newalue, 0);
                                    }
                                } else {
                                    if (isNamedEntity) {
                                        logger.debug(sourceWord + " is a Named Entity and would be transliterated");
                                    } else {
                                        logger.debug("No translation found for " + sourceWord);
                                    }

                                    if (!nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceWord.trim().equals("")) {
                                        nodeChild.setLexData(AT + sourceWord);
                                        try {
                                            bw.write(sourceWord + " " + nodefapos.getAltValue(0).toString() + "\n"); // Add anomaly to the list
                                        } catch (IOException ioe) {
                                            ioe.printStackTrace();
                                        }
                                    }

                                    if (nodefaWord != null && !nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceWord.trim().equals("")) {
                                        FeatureValue newalue = new FeatureValueImpl();
                                        newalue.setValue(AT + sourceWord);
                                        //    try{
                                        //	bw.write(sourceWord+" "+nodefapos.getAltValue(0).toString()+"\n"); // Add anomaly to the list
                                        //     }catch (IOException ioe) {
                                        //           ioe.printStackTrace();
                                        //       }
                                        nodefaWord.modifyAltValue(newalue, 0);
                                    }
                                }
                            } else {
                                logger.debug("Translation for " + sourceWord + " is " + targetWords[0]);
                                nodeChild.setLexData(targetWords[0]);
                                if (nodefaWord != null) {
                                    FeatureValue newalue = new FeatureValueImpl();
                                    newalue.setValue(targetWords[0]);
                                    nodefaWord.modifyAltValue(newalue, 0);
                                }
                            }
                            // Setting TAM value
                            if (targetTams == null || targetTams.length == 0) {
                                if (caratPrefixTAM || tildaPrefixTAM) {
                                    logger.debug(sourceTam + " starts with ^, ^^ or ~");
                                    sourceTam = sourceTam.replaceFirst("\\^\\^|\\^", "");
                                    if (nodefatam != null) {
                                        FeatureValue newaTam = new FeatureValueImpl();
                                        newaTam.setValue(sourceTam);
                                        nodefatam.modifyAltValue(newaTam, 0);
                                    }
                                } else {
                                    if (nodefatam != null && !nodefapos.getAltValue(0).toString().equals(PUNC) && !sourceTam.trim().equals("")) {
                                        FeatureValue newaTam = new FeatureValueImpl();
                                        newaTam.setValue(AT + sourceTam);
                                        try {
                                            bw.write(sourceTam + " " + nodefapos.getAltValue(0).toString() + "\n");// Add anomaly to the list
                                        } catch (IOException ioe) {
                                            ioe.printStackTrace();
                                        }
                                        nodefatam.modifyAltValue(newaTam, 0);
                                        logger.debug("No translation found for TAM " + sourceTam);
                                    }
                                }
                            } else if (nodefatam != null) {
                                logger.debug("Translation for " + sourceTam + " is " + targetTams[0]);
				//System.out.println("Target Tam: "+targetTams[0]);
                                FeatureValue newaTam = new FeatureValueImpl();
                                newaTam.setValue(targetTams[0]);
                                nodefatam.modifyAltValue(newaTam, 0);
                            }
                        }
                    }
                }
            }

            if (outputFileName != null) {
                story.save(outputFileName, "UTF-8");
            } else {
                story.print(System.out);
            }

        } catch (Exception ex) {
            logger.fatal("Error while processing input SSF file.");
            throw new MultilingualDictException("Error while processing input SSF file.", ex);
        }
    }

    /**
     * <p><b>Method</b> : getTranslation <p><b>Purpose</b>	: Returns the
     * translation of the source word. <p><b>@param lemma - source word
     * <p><b>@param pos	- POS category <p><b>@param sourceLanguage - Source
     * Language <p><b>@param targetLanguage - Target Language <p><b>@return</b>
     * - Returns an array containing the translations of the source word in the
     * target language.
     * @param isDisambiguated
     */
    public String[] getTranslation(
            String lemma,
            POS1 pos,
            Language sourceLanguage,
            Language targetLanguage, String synsetId) {
        try {
            String[] output = null;
            if (sessionDict != null) {
                output = getCrossLinkedWords(sessionDict, lemma, sourceLanguage, targetLanguage, pos, synsetId);
            }
            if (output == null || output.length == 0) {
                if (userDict != null) {
                    output = getCrossLinkedWords(userDict, lemma, sourceLanguage, targetLanguage, pos, synsetId);
                }
                if (output == null || output.length == 0) {
                    if (NNPDict != null) {
                        output = getCrossLinkedWords(NNPDict, lemma, sourceLanguage, targetLanguage, pos, synsetId);
                    }
                    if (output == null || output.length == 0) {
                        if (systemDict != null) {
                            output = getCrossLinkedWords(systemDict, lemma, sourceLanguage, targetLanguage, pos, synsetId);
                        }
                        if (output == null || output.length == 0) {
                            if (BilingualDict != null) {
                                output = getCrossLinkedWords(BilingualDict, lemma, sourceLanguage, targetLanguage, pos, synsetId);
                            }
                            if (output == null || output.length == 0) {
                                if (!lemma.equals("")) {
                                    logger.debug("\"" + lemma + "\"" + " is not present in any dictionary");
                                }
                            } else {
                                logger.debug("\"" + lemma + "\"" + " is present in Bilingual dictionary.");
                            }
                        } else {
                            logger.debug("\"" + lemma + "\"" + " is present in system dictionary.");
                        }
                    } else {
                        logger.debug("\"" + lemma + "\"" + " is present in NNP dictionary.");
                    }
                } else {
                    logger.debug("\"" + lemma + "\"" + " is present in user dictionary.");
                }
            } else {
                logger.debug("\"" + lemma + "\"" + " is present in session dictionary.");
            }
            //System.out.println(output);
            return output;
        } catch (MultilingualDictException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Method : getCategory Purpose	:
     *
     * @param strCategory
     * @return
     */
    public POS1 getCategory_POS(String strCategory) {
        if (strCategory.startsWith("N")) {
            return POS1.NOUN;
        }
        if (strCategory.startsWith("JJ")) {
            return POS1.ADJECTIVE;
        }
        if (strCategory.startsWith("RB")) {
            return POS1.ADVERB;
        }
        if (strCategory.startsWith("V")) {
            return POS1.VERB;
        }
        if (strCategory.startsWith("CC")) {
            return POS1.CONJ;
        }
        return POS1.FW;
    }

    /**
     * Method : getCategory Purpose	:
     *
     * @param strCategory
     * @return
     */
    public POS1 getCategory_LCAT(String strCategory) {
        if (strCategory.equals("n")|| strCategory.equals("unk")) {
            return POS1.NOUN;
        }
        if (strCategory.equals("adj")) {
            return POS1.ADJECTIVE;
        }
        if (strCategory.equals("adv")) {
            return POS1.ADVERB;
        }
        if (strCategory.equals("v")) {
            return POS1.VERB;
        }
        if (strCategory.equals("cardinal")) {
            return POS1.ADJECTIVE;
        }
        if (strCategory.equals("nst")) {
            return POS1.ADVERB;
        }
        if (strCategory.equals("conj")) {
            return POS1.CONJ;
        }
	if (strCategory.equals("pn") || strCategory.equals("particle")) {
            return POS1.PRON;
        }
        return POS1.FW;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String compare2(String result) {

        String line = "";
        String npvt = "";
        String tempTam = "";

        try {
            FileInputStream fstream = new FileInputStream(USER_HOME + "/data_bin/sl_tl/lexicaltransfer/TamDict/asa_suffix.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            //It reads line by line from asa_suffix
            //and  split it according to ":" and store it in the array atr3
            while ((line = br.readLine()) != null) {
                String[] str3 = line.split(":");

                if (result.equalsIgnoreCase(str3[0])) {

                    tempTam = str3[1];
                }


            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found exception - " + e.toString());
        } catch (IOException e) {
            System.out.println("Input output  exception - " + e.toString());
        }
        return (tempTam);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public POS1 getCategory(String strCategory) {
        if (usePOSTags) {
            return getCategory_POS(strCategory);
        } else {
            return getCategory_LCAT(strCategory);
        }
    }

    /**
     * Method : main Purpose	: Dummy main for testing purpose.
     *
     * @param args
     * @throws MultilingualDictException
     * @throws IOException
     */
    public static void main(String args[]) throws MultilingualDictException, IOException {

        String inputFileName = null;
        int debugLevel = 1;
        String outputFileName = null;
        String logFile = null;
        int c;

        // 
        //ReadEnv.load();
        //USER_HOME = ReadEnv.getProperty("USER_HOME");
        //USER_HOME = "/media/D/NewCleanBuild";
        //StringBuffer sb = new StringBuffer();
        LongOpt[] longopts = new LongOpt[8];
        StringBuffer systemDict = new StringBuffer();
        StringBuffer userDict = new StringBuffer();
        StringBuffer sessionDict = new StringBuffer();
        StringBuffer NNPDict = new StringBuffer();
        StringBuffer BiDict = new StringBuffer();
        StringBuffer logFilePath = new StringBuffer();

        boolean openUserDict = false;
        boolean openSessionDict = false;
        boolean openSystemDict = false;
        boolean openNNPDict = false;
        boolean openBilingualDict = false;

        longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[1] = new LongOpt("system-dict", LongOpt.OPTIONAL_ARGUMENT, systemDict, 'y');
        longopts[2] = new LongOpt("session-dict", LongOpt.OPTIONAL_ARGUMENT, sessionDict, 'e');
        longopts[3] = new LongOpt("user-dict", LongOpt.OPTIONAL_ARGUMENT, userDict, 'r');
        longopts[4] = new LongOpt("NNP-dict", LongOpt.OPTIONAL_ARGUMENT, NNPDict, 'n');
        longopts[5] = new LongOpt("Bilingual-dict", LongOpt.OPTIONAL_ARGUMENT, BiDict, 'l');
        longopts[6] = new LongOpt("Use POSTags", LongOpt.NO_ARGUMENT, null, 'p');
        longopts[6] = new LongOpt("LogFile Path", LongOpt.REQUIRED_ARGUMENT, logFilePath, 'j');

        Getopt g = new Getopt("LexicalTransferEngine", args, "-:i:o:s:t:d:u:y::e::r::n::l::p::j:h", longopts);
        g.setOpterr(false);
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 0:
                case 'h':
                    System.out.println("Usage:\n java -classpath "
                            + "$setu/bin/sl_tl/lexicaltransfer/common:"
                            + "$setu/bin/sl_tl/lexicaltransfer/common/lib/MultiDictAPIs.jar:"
                            + "$setu/bin/sl_tl/lexicaltransfer/common/lib/SSFAPI.jar:"
                            + "$setu/bin/sl_tl/lexicaltransfer/common/lib/log4j-1.2.15.jar"
                            + " in.ac.iitb.cfilt.lte.engine.LexicalTransferEngine "
                            + " [-options]\n"
                            + " where options include\n"
                            + " -h --help\t\t for help on sample usage\n"
                            + " -u \t\t path of USER_HOME directory. In our case this will be the path of setu directory\n"
                            + " -i \t\t for specifying input file name (mandatory)  \n"
                            + " -o \t\t for specifying output file name (mandatory) \n"
                            + " -s \t\t for specifying source language (3-letter language code, mandatory)\n"
                            + " -t \t\t for specifying target language (3-letter language code, mandatory)\n"
                            + " -e [path] \t\t for enabling session specific dictionary to be used "
                            + "	and optionally specifying its location"
                            + " -r user-dict [path] \t\t for enabling user specific dictionary to be used "
                            + "	and optionally specifying its location"
                            + " -y [path] \t\t for enabling system specific dictionary to be used "
                            + "	and optionally specifying its location"
                            + " -n [path] \t\t for enabling Proper Noun(NNP) dictionary to be used "
                            + "	and optionally specifying its location"
                            + " -l [path] \t\t for enabling Bilingual dictionary to be used "
                            + "	and optionally specifying its location"
                            + " -j [path] \t\t for specifying path of the log file\n"
                            + " -d \t\t for specifying debug level (optional) Possible log levels are:\n"
                            + " -d \t\t "
                            + "\t\t\t\tLOG_LEVEL:\n\t\t\t\t\t1 : INFO(default)\n\t\t\t\t\t2 : DEBUG\n\t\t\t\t\t3 : ALL\n\t\t\t\t\t4 : OFF");
                    System.exit(0);

                //
                case 'o':
                    outputFileName = g.getOptarg();
                    break;
                //
                case 'u':
                    USER_HOME = g.getOptarg();
                    break;
                //
                case 'i':
                    inputFileName = g.getOptarg();
                    break;
                //
                case 's':
                    sourceLanguage = Language.getLanguage(g.getOptarg());
                    break;
                //
                case 't':
                    targetLanguage = Language.getLanguage(g.getOptarg());
                    break;
                //
                case 'y':
                    systemDictPath = g.getOptarg();
                    openSystemDict = true;
                    break;
                //
                case 'e':
                    sessionDictPath = g.getOptarg();
                    openSessionDict = true;
                    break;
                //
                case 'r':
                    userDictPath = g.getOptarg();
                    openUserDict = true;
                    break;
                //
                case 'n':
                    NNPDictPath = g.getOptarg();
                    openNNPDict = true;
                    break;
                //
                case 'l':
                    BilingualDictPath = g.getOptarg();
                    openBilingualDict = true;
                    break;
                case 'p':
                    usePOSTags = true;
                    break;
                //
                case 'j':
                    logFile = g.getOptarg();
                    break;
                case 'd':
                    try {
                        debugLevel = Integer.parseInt(g.getOptarg());
                    } catch (Exception ex) {
                        System.out.println("Debug level should be a valid integer");
                    }
                    break;
                //

                case ':':
                    System.out.println("You need an argument for option "
                            + (char) g.getOptopt());
                    System.exit(0);
                //
                case '?':
                    System.out.println("The option '" + (char) g.getOptopt()
                            + "' is not valid");
                    System.exit(0);
                //
                default:
                    System.out.println("getopt() returned " + c);
                    break;
            }
        }

        if (USER_HOME == null) {
            System.out.println("You must specify option 'u' for 'USER_HOME' directory i.e the path of setu directory");
            System.exit(0);
        }
        if (inputFileName == null) {
            System.out.println("You must specify option 'i' for 'input file' ");
            System.exit(0);
        }
        /*
         * if(outputFileName == null) { System.out.println("You must specify
         * option 'o' for 'output file' "); System.exit(0); }
         */ if (sourceLanguage == null) {
            System.out.println("You must specify option 's' for 'source language' ");
            System.exit(0);
        }
        if (targetLanguage == null) {
            System.out.println("You must specify option 't' for 'target language' ");
            System.exit(0);
        }
        BasicConfigurator.configure();
        if (logFile != null) {
            FileAppender fappender = new FileAppender(new PatternLayout("%-6p [%t] (%F:%L) %m%n"), logFile);
            logger.addAppender(fappender);
        }
        switch (debugLevel) {
            case 1:
                logger.setLevel(Level.INFO);
                break;
            case 2:
                logger.setLevel(Level.DEBUG);
                break;
            case 3:
                logger.setLevel(Level.ALL);
                break;
            case 4:
                logger.setLevel(Level.OFF);
                break;
        }

        logger.info("$setu=" + USER_HOME);
        if (systemDictPath != null && !checkIfDirectoryExists(systemDictPath)) {
            logger.info("Specified directory " + systemDictPath + " does not exist.");
            System.exit(0);
        }

        if (userDictPath != null && !checkIfDirectoryExists(userDictPath)) {
            logger.info("Specified directory " + userDictPath + " does not exist.");
            System.exit(0);
        }

        if (sessionDictPath != null && !checkIfDirectoryExists(sessionDictPath)) {
            logger.info("Specified directory " + sessionDictPath + " does not exist.");
            System.exit(0);
        }

        if (NNPDictPath != null && !checkIfDirectoryExists(NNPDictPath)) {
            logger.info("Specified directory " + NNPDictPath + " does not exist.");
            System.exit(0);
        }

        if (BilingualDictPath != null && !checkIfDirectoryExists(BilingualDictPath)) {
            logger.info("Specified directory " + BilingualDictPath + " does not exist.");
            System.exit(0);
        }

        LexicalTransferEngine lte = new LexicalTransferEngine();
        lte.initialize(USER_HOME, openUserDict, openSessionDict, openSystemDict, openNNPDict, openBilingualDict);
        lte.translate(inputFileName, outputFileName, sourceLanguage,
                targetLanguage);
        bw.close();
        lte.stripDuplicatesFromFile(unrecognized);
        if (outputFileName != null) {
            logger.info("Translation done. Please check the output file/dir "
                    + outputFileName);
        }
    }

    private static boolean checkIfDirectoryExists(String dirName) {
        if (dirName == null) {
            return false;
        }
        return new File(dirName).exists() && new File(dirName).isDirectory();
    }

    private String getDefaultPath(String path, String language1, String language2) {
        return path + File.separator + language1 + "-" + language2;
    }

    private String[] getCrossLinkedWords(Dictionary dict, String lemma, Language sourceLanguage,
            Language targetLanguage, POS1 pos, String synsetId) throws MultilingualDictException {

        if (pos.equals(POS1.CONJ)) {
	    String conjcands[];
            Vector<String> temp = conjlist.get(lemma);
	    if(temp!=null){
            	conjcands = new String[temp.size()];
	    }
	    else{
		return null;
		}
            for(int i=0;i<conjcands.length;i++){
                conjcands[i] = temp.get(i);
            }
	    if(conjcands.length==0 || conjcands==null){
		pos = POS1.FW;
	    } else		
	        return conjcands;
            
        }
	
	if (pos.equals(POS1.PRON)) {
	    if(lemma.endsWith("णे")){
	    	lemma = lemma.replaceAll("णे$" , "" ); //Handle mistakes in postagging and overcome null pointer error
//		System.out.println(lemma);
	    }
	    String proncands[] = null;
	    if(pronlist.containsKey(lemma)){
            Vector<String> temp = pronlist.get(lemma);
//	    System.out.println(lemma+" "+temp);
            proncands = new String[temp.size()];
	     
            for(int i=0;i<proncands.length;i++){
                proncands[i] = temp.get(i);
            }
	    }
	    if(proncands.length==0 || proncands==null){
		pos = POS1.FW;
	    } else		
		
	        return proncands;
            
        }

        if (dict instanceof FileBackedDictionary) {
            dict = (FileBackedDictionary) dict;
        }

        String[] crossLinkedWords = null;
        if (synsetId != null) {
            DSFRecord dsfr = dict.getDSFRecord(synsetId, pos, sourceLanguage);
            if (dsfr != null) {
                if (pos == null) {
                    crossLinkedWords = dict.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId);
                } else {
                    crossLinkedWords = dict.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId, pos);
                }

                if (crossLinkedWords != null) {
                    return crossLinkedWords;
                }
            }
        }


        DSFRecord[] dsfRecords;
        if (pos == null) {
            dsfRecords = dict.getDSFRecords(lemma, sourceLanguage);
        } else {
            dsfRecords = dict.getDSFRecords(lemma, pos, sourceLanguage);
            //System.out.println(lemma+pos);
        }
        //System.out.println(pos);
        Vector<String> synsetlist = new Vector<String>();
        if (dsfRecords != null) {
            int lIndex = Integer.MAX_VALUE;
            for (int count = 0; count < dsfRecords.length; count++) {
                if (dsfRecords[count] != null) {
                    Vector<String> vwords = dsfRecords[count].getWords();

                    if (vwords != null) {
                        int curIndex = vwords.indexOf(lemma);
                        lIndex = curIndex;
                        synsetId = dsfRecords[count].getID();
                        synsetlist.add(synsetId);

                    }
                }
            }
            
            if (synsetlist.size() > 0) {
				
                synsetId = synsetlist.firstElement();
                /*
                 * Modification by Raj Dabre
                 * I have received a list of synsets ranked by frequency. This was done by linguists
                 * The wordnet is not ordered in such a way
                 * By doing this I now perform actual WFS WSD. This should be better I think
                 * I populate a list of synsets which are the first sense
                 * For each of the retrieved synsets by the dict.getDSFRecords() I simply check its presence in the list 
                 * and thats the 1st sense
                 * The order is no longer arbit
                 */
                for(String synset_retrieved:synsetlist){ 
					if(first_rank_synset_list.contains(synset_retrieved)){
						synsetId = synset_retrieved;
						break;
					}
				}
				
				//End of modification
            }
            if (synsetId != null) {
                if (pos == null) {
                    crossLinkedWords = dict.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId);
                } else {
                    crossLinkedWords = dict.getCrossLinkedWords(lemma, sourceLanguage, targetLanguage, synsetId, pos);
                }
            }
        }
        return crossLinkedWords;
    }

    /*
     * flags is for <kridanta,VGF,VGNF/VGINF/VGNN>
     */
    public String[] processTAM(String[] sourceWord, String sourceTam, POS1 pos, Language sourceLanguage, Language targetLanguage,
            String synsetId, boolean isKridanta, String chunkNameType, FeatureStructure nodefs) {
        String temp = ""; // only for mar-hin
//System.out.println("Source Tam: "+sourceTam+" "+isKridanta);
        if (sourceTam.contains("#")) {
            String retVal = "";
	    if((sourceTam.contains("ण्या#स") && !sourceTam.contains("ण्या#साठी") ) ||(sourceTam.contains("ल्या#स") && !sourceTam.contains("ल्या#साठी") ) || sourceTam.contains("ण्या#ला")|| sourceTam.contains("ल्या#ला")){
	    	sourceTam = sourceTam.trim().replaceAll("#","_");
		//System.out.println("sfs");
	    }
            String sourceTams[] = sourceTam.trim().split("#");
            for (int i = 0; i < sourceTams.length; i++) {

                if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                    sourceTams[i] = removeEmptyMatra(sourceTams[i]);
                }
                //System.out.println(sourceTams[i]);
                String targetTams[];
                if (pos == POS1.VERB) {
                    if (isKridanta) {
//System.out.println("Kridanata type transfer");
                        targetTams = getTranslation(sourceTams[i], POS1.FW, sourceLanguage, targetLanguage, null);

                        if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                            if (targetTams == null) {

                                targetTams = processTamMarHin(sourceWord, null, chunkNameType, isKridanta, nodefs);
                            } else {
                                //		System.out.println(targetTams[0]);
                                targetTams = processTamMarHin(sourceWord, targetTams[0], chunkNameType, isKridanta, nodefs);
                                //System.out.println("boom");
                            }
                        }
                    } else {
                        if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                            targetTams = processTamMarHin(sourceWord, sourceTams[i], chunkNameType, false, null);
                        } else {
                            targetTams = getTranslation(sourceTams[i], POS1.TAM, sourceLanguage, targetLanguage, null);
			    
                        }
                    }
                } else {

				//System.out.println(POS.FW);
                    targetTams = getTranslation(sourceTams[i], POS1.FW, sourceLanguage, targetLanguage, null);
//		    for(String x: targetTams){
//				System.out.println(x);
//			    }
                }
                if (targetTams == null || targetTams.length == 0) {
                    retVal += sourceTams[i] + "#";
                } else {
                    retVal += targetTams[0] + "#";
                }
                //	System.out.println(sourceTams[i]);
            }

            retVal = retVal.substring(0, retVal.length() - 1);
            String ret[] = new String[1];
            ret[0] = replaceHashBy_(retVal);
            return ret;

        } else {
            if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                temp = sourceTam;
                sourceTam = removeEmptyMatra(sourceTam);
            }
            String targetTams[];
            if (pos == POS1.VERB) {
                if (isKridanta) {
                    targetTams = getTranslation(sourceTam, POS1.FW, sourceLanguage, targetLanguage, null);
                    if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                        if (targetTams == null) {
                            targetTams = processTamMarHin(sourceWord, null, chunkNameType, isKridanta, nodefs);

                        } else {
                            targetTams = processTamMarHin(sourceWord, targetTams[0], chunkNameType, isKridanta, nodefs);
                            //This was genius on the part of the developer originally it was targetTams[i] and it leads to arrayindexoutofbounds error.
                            //I Raj Dabre of IITB have solved this. Its like looking for a needle in a haystack.
                        }
                    }
                } else {
                    if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                        targetTams = processTamMarHin(sourceWord, sourceTam, chunkNameType, false, null);
                    } else {
                        targetTams = getTranslation(sourceTam, POS1.TAM, sourceLanguage, targetLanguage, null);
                    }
                }
            } else {
		
                targetTams = getTranslation(sourceTam, POS1.FW, sourceLanguage, targetLanguage, null);
            }
            if (sourceLanguage.toString().toLowerCase().equals("mar") && targetLanguage.toString().toLowerCase().equals("hin")) {
                if (targetTams == null) {
		    //System.out.println("Here again"+sourceTam+" "+POS.FW);
                    if (temp != sourceTam) {
                        targetTams = new String[1];
                        targetTams[0] = temp;
                    }

                } else {//else part is not in ilmt pipeline
                    for (int i = 0; i < targetTams.length; i++) {
                        if (targetTams[i] == "___") {
                            targetTams[i] = "";
                        }
                    }
                }
            }
            return targetTams;
        }
    }

    /*
     * A seperate TAM processing module when sl=mar, tl=hin author = Anup,
     * Saurabh
     */
    public String[] processTamMarHin(String[] sourceWord, String sourceTam, String chunkNameType, boolean isKridanta, FeatureStructure nodefs) {
        // if isKridanta is true then sourceTam already contains earlier found targetTam

        String tempTam = sourceTam;
        String[] finalTams;
        boolean[] localFlags = {false};		// only for passing by reference
//		flag[0] = true		if tam is negative
        String[] correctRootReplacement = new String[1]; // only for passing by reference
        correctRootReplacement[0] = "";	// used for the case where root="आहे", tam="त" -> "हो"

        if (!isKridanta) {
            tempTam = preprocessTamMarHin(sourceWord[0], tempTam, localFlags, correctRootReplacement); // neg flag is set here
            //
            tempTam = mainTamTransferTamMarHin(tempTam, chunkNameType, localFlags);	// chunkNameType is useful here
        }

        finalTams = postprocessTamMarHin(sourceWord, tempTam, localFlags, correctRootReplacement, isKridanta, nodefs); // neg flag is used here
        return finalTams;
    }

    /*
     * Pre-processing module for TAMs
     */
    public String preprocessTamMarHin(String sourceWord, String sourceTam, boolean[] localFlags, String[] correctRootReplacement) {
        String tempTam = "";
        tempTam = removeEmptyMatra(sourceTam);
        tempTam = negCheckModule(tempTam, localFlags);
        rootCheckModule(sourceWord, tempTam, correctRootReplacement);
        tempTam = componentCombo(tempTam);
        return tempTam;
    }

    /*
     * Module to remove negation from the souceTam, this will set a Neg_flag =
     * flag[0]
     */
    public String negCheckModule(String sourceTam, boolean[] localFlags) {
        try {
            HashMap<String, String[]> hmNegate = readFileIntoHashMap("negate.txt", 2, 0);
            HashMap<String, String[]> hmNegateCloseList = readFileIntoHashMap("negateCloseList.txt", 1, 0);
            ArrayList<String> negateList = keyToArrayList(hmNegateCloseList);

            for (int i = 0; i < negateList.size(); i++) {
                String tempNegate = negateList.get(i);
                int tempNegIndex = sourceTam.indexOf(tempNegate);
                if (tempNegIndex >= 0) {
                    String negSource = sourceTam.replaceAll("\\+", "_");
                    String[] negArray = negSource.split("_");
                    String negTarget = "";
                    int j;
                    for (j = 0; j < negArray.length; j++) {
                        if (negArray[j].equals(tempNegate)) {
                            break;
                        }
                    }
                    if (j > 0) {
                        if (!negArray[j - 1].equals("")) {
                            negTarget = negTarget + negArray[j - 1] + sourceTam.substring(tempNegIndex - 1, tempNegIndex) + tempNegate;
                        }
                    }
                    if (j < negArray.length - 1) {
                        if (!negArray[j + 1].equals("")) {
                            negTarget = negTarget + sourceTam.substring(tempNegIndex + tempNegate.length(), tempNegIndex + tempNegate.length() + 1) + negArray[j + 1];
                        }
                    }
                    StringBuffer nsbf = new StringBuffer(negTarget);
                    if (negTarget.startsWith("_") || negTarget.startsWith("+")) {
                        nsbf = nsbf.deleteCharAt(0);
                    }
                    if (negTarget.endsWith("+") || negTarget.endsWith("_")) {
                        nsbf = nsbf.deleteCharAt(negTarget.length() - 1);
                    }
                    negTarget = nsbf.toString();
                    StringBuffer sbf = new StringBuffer(sourceTam);
                    int replaceIndex = sourceTam.indexOf(negTarget);
                    if (hmNegate.containsKey(negTarget)) {
                        localFlags[0] = true;
                        sbf = sbf.replace(replaceIndex, replaceIndex + negTarget.length(), hmNegate.get(negTarget)[1]);
                        sourceTam = sbf.toString();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!localFlags[0]) {
            if (sourceTam.indexOf("_नाही+") >= 0) {
                String negS = "_नाही+";
                StringBuffer temp = new StringBuffer(sourceTam);
                sourceTam = temp.replace(sourceTam.indexOf("_नाही+"), sourceTam.indexOf("_नाही+") + negS.length(), "_आहे+").toString();
                localFlags[0] = true;
            } else if (sourceTam.indexOf("_नाही_") >= 0) {
                String negS = "_नाही";
                StringBuffer temp = new StringBuffer(sourceTam);
                sourceTam = temp.delete(sourceTam.indexOf("_नाही_"), sourceTam.indexOf("_नाही_") + negS.length()).toString();
                localFlags[0] = true;
            } else if (sourceTam.indexOf("_नाही") >= 0) {
                String negS = "_नाही+";
                StringBuffer temp = new StringBuffer(sourceTam);
                sourceTam = temp.replace(sourceTam.indexOf("_नाही"), sourceTam.indexOf("_नाही") + negS.length(), "_आहे+").toString();
                localFlags[0] = true;
            }
        }
        return sourceTam;
    }

    public String componentCombo(String sourceTam) {
        try {
            HashMap<String, String[]> combo = readFileIntoHashMap("combo.txt", 1, 0);
            ArrayList<String> combSource = keyToArrayList(combo);
            for (int i = 0; i < combSource.size(); i++) {
                String tempComb = combSource.get(i);
                StringBuffer tempSb = new StringBuffer(sourceTam);
                if (sourceTam.startsWith(tempComb + "+")) {
                    sourceTam = tempSb.deleteCharAt(tempComb.length() + 1).toString();
                    break;
                } else if (sourceTam.indexOf("_" + tempComb + "+") >= 0) {
                    int index = sourceTam.indexOf("_" + tempComb + "+");
                    sourceTam = tempSb.deleteCharAt(index + tempComb.length() + 1).toString();
                    break;
                } else if (sourceTam.indexOf("+" + tempComb + "+") >= 0) {
                    int index = sourceTam.indexOf("+" + tempComb + "+");
                    sourceTam = tempSb.deleteCharAt(index + tempComb.length() + 1).toString();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceTam;
    }

    public ArrayList<String> keyToArrayList(HashMap<String, String[]> hm) {
        ArrayList<String> temp = new ArrayList<String>();
        Set<String> s = hm.keySet();
        Iterator<String> itr = s.iterator();
        while (itr.hasNext()) {
            String tString = (String) itr.next();
            temp.add(tString);
        }
        return temp;
    }

    public String removeEmptyMatra(String sourceTam) {
        try {
            HashMap<String, String[]> replacement = readFileIntoHashMap("matra.txt", 2, 0);
            ArrayList<String> matraList = keyToArrayList(replacement);
            for (int i = 0; i < matraList.size(); i++) {
                StringBuffer sourceTamBuffer = new StringBuffer(sourceTam);
                String tempMatra = matraList.get(i);
                ArrayList<Integer> matraArray = new ArrayList<Integer>();
                for (int j = 0; j < sourceTam.length(); j++) {
                    if (sourceTam.substring(j, j + 1).equals(tempMatra)) {
                        matraArray.add(new Integer(j));
                    }
                }
                for (int j = 0; j < matraArray.size(); j++) {
                    int matraIndex = (Integer) matraArray.get(j);
                    if (matraIndex >= 0) {
                        if (matraIndex == 0) {  // || matraIndex == 1
                            //replace from hash map
                            sourceTam = sourceTamBuffer.replace(matraIndex, matraIndex + 1, replacement.get(tempMatra)[1]).toString();
                        } else if (sourceTam.charAt(matraIndex - 1) == '+' || sourceTam.charAt(matraIndex - 1) == '_') {
                            //replace from hash map
                            if (matraIndex == sourceTam.length() - 1) {
                                sourceTam = sourceTamBuffer.replace(matraIndex - 1, matraIndex + 1, "").toString();
                            } else {
                                sourceTam = sourceTamBuffer.replace(matraIndex, matraIndex + 1, replacement.get(tempMatra)[1]).toString();
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceTam;
    }

    void rootCheckModule(String sourceWord, String sourceTam, String[] correctRootReplacement) {
        try {
            HashMap<String, String[]> hm = readFileIntoHashMap("spl_aux_table.txt", 4, 2);
            String tempStr = sourceWord + "+" + sourceTam;
            if (sourceWord != null) {
                if (hm.containsKey(tempStr)) {
                    correctRootReplacement[0] = (hm.get(tempStr))[3];
                } else {
                    correctRootReplacement[0] = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String[]> getCorrectHashMap(String chunkNameType) {
        HashMap<String, String[]> hm;
        hm = null;
        try {
            if (chunkNameType.startsWith("VG")) {
                if (chunkNameType.startsWith("VGF")) {
                    hm = readFileIntoHashMap("akhyat_pairs.txt", 4, 2);
                } else if (chunkNameType.startsWith("VGNF")) {
                    hm = readFileIntoHashMap("kridanta_pairs.txt", 4, 2);
                } else if (chunkNameType.startsWith("VGINF")) {
                    hm = readFileIntoHashMap("kridanta_pairs.txt", 4, 2);
                } else if (chunkNameType.startsWith("VGNN")) {
                    hm = readFileIntoHashMap("kridanta_pairs.txt", 4, 2);
                } else {
                    hm = readFileIntoHashMap("akhyat_pairs.txt", 4, 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }

    public String compare(String result) {

        String line = "";
        String npvt = "";
        String tempTam = "";

        try {
            FileInputStream fstream = new FileInputStream(USER_HOME + "/data_bin/sl_tl/lexicaltransfer/TamDict/asa_suffix.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            //It reads line by line from asa_suffix
            //and  split it according to ":" and store it in the array atr3
            while ((line = br.readLine()) != null) {
                String[] str3 = line.split(":");

                if (result.equalsIgnoreCase(str3[0])) {

                    tempTam = str3[1];
                }


            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found exception - " + e.toString());
        } catch (IOException e) {
            System.out.println("Input output  exception - " + e.toString());
        }
        return (tempTam);
    }

    /*
     * main TAM transfer algorithm designed by Subodh, implemented by Anup
     */
    public String mainTamTransferTamMarHin(String sourceTam, String chunkNameType, boolean[] localFlags) {
        if (sourceTam.isEmpty()) {
            return "";
        }
        String targetTam = "";
        try {
            HashMap<String, String[]> pairs = getCorrectHashMap(chunkNameType);
            if (pairs != null) {
                if (sourceTam.indexOf("+") < 0) {
                    // no occurance of +
                    // only one pair
                    // direct transfer
                    if (pairs.containsKey(sourceTam)) {
                        targetTam = pairs.get(sourceTam)[3];
                    } else {
                        targetTam = "@" + sourceTam;
                    }
                } else {
                    String[] tamTokens = sourceTam.split("\\+");
                    for (int i = 0; i < tamTokens.length; i++) {
                        if (tamTokens[i].isEmpty()) {
                            continue;
                        }
                        if (pairs.containsKey(tamTokens[i])) {
                            targetTam = targetTam + pairs.get(tamTokens[i])[3] + "+";
                        } else {
                            targetTam = targetTam + "@" + tamTokens[i] + "+";
                        }
                    }
                    if (targetTam.charAt(targetTam.length() - 1) == '+') {
                        StringBuffer tempTargetTam = new StringBuffer(targetTam);
                        tempTargetTam = tempTargetTam.deleteCharAt(targetTam.length() - 1);
                        targetTam = tempTargetTam.toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetTam;
    }

    /*
     * post-processing module for TAM transfer
     */
    public String[] postprocessTamMarHin(String[] sourceWord, String sourceTam,
            boolean[] localFlags, String[] correctRootReplacement, boolean isKridanta, FeatureStructure nodefs) {

        String[] finalTams = new String[1];
        String tempTam = "", kr_type = "", tempTam1 = "";
//System.out.println("boom");
        if (!isKridanta) {
            //putTamValueToKridantaCMattrib(nodefs,sourceTam); 
            tempTam = postNegation(sourceTam, localFlags);
            tempTam = postRootCheck(sourceWord, tempTam, correctRootReplacement[0]);
            //  tempTam =  asaProcessing(sourceWord, sourceTam, localFlags, correctRootReplacement, isKridanta, nodefs);


        } else {
            tempTam = putTamValueToKridantaCMattrib(nodefs, sourceTam);
            tempTam1 = "ने"; //I have replaced here ना_  with  "ने"  on 25 october. 
            if (tempTam == null) {
                tempTam = tempTam1;       //Raj Dabre 24/5/2012
                //	tempTam = tempTam1.concat(tempTam);
            }
        }

        finalTams[0] = tempTam;

        return finalTams;
    }

    /*
     * public String asaProcessing(String[] sourceWord, String sourceTam,
     * boolean[] localFlags, String[] correctRootReplacement, boolean
     * isKridanta, FeatureStructure nodefs) { String[] finalTams = new
     * String[1]; String tempTam=""; int countAttrib = nodefs.countAttributes();
     * FeatureAttribute attrib; for(int m=0; m < countAttrib; m++) { attrib =
     * nodefs.getAttribute(m); if(attrib != null) {
     * if(attrib.getName().equalsIgnoreCase("kridanta_cm")){ //	Enumeration
     * children = attrib.children(); //	TreeNode s = children.nextElement();
     * FeatureValue fval = attrib.getAltValue(0); //fval.setValue(tamValue);
     * tempTam=fval.toString();
     *
     * }
     * }
     * }
     * finalTams[0]=tempTam; return tempTam;
     *
     * }
     */
    public String replaceHashBy_(String tam) {
        String resultTam = tam;
        if ((tam != null) || (tam.length() > 0)) {
            resultTam = tam.replace('#', '_');
        }
        return resultTam;
    }

    public String putTamValueToKridantaCMattrib(FeatureStructure nodefs, String tamValue) {
        int countAttrib = nodefs.countAttributes();
        FeatureAttribute attrib;
        for (int m = 0; m < countAttrib; m++) {
            attrib = nodefs.getAttribute(m);

            if (attrib != null) {
                if (attrib.getName().equalsIgnoreCase("kridanta_cm")) {
//					Enumeration children = attrib.children();
//					TreeNode s = children.nextElement();
                    FeatureValue fval = attrib.getAltValue(0);
                    fval.setValue(tamValue);
                }
            }
        }
        return tamValue;
    }

    public String postNegation(String sourceTam, boolean[] flags) {
        if (flags[0] == true) {
            if (sourceTam.indexOf("_") >= 0) {
                sourceTam = sourceTam.replaceFirst("_", "_नही_");
            } else {
                sourceTam = sourceTam + "_नही";
            }
        }
        return sourceTam;
    }

    public String postRootCheck(String[] sourceWord, String sourceTam, String correctRootReplacement) {
        // check for special cases like, root="आहे", tam="त" -> "हो"
        if ((!correctRootReplacement.equals("")) && (correctRootReplacement != null)) {
            sourceWord[0] = correctRootReplacement;
            sourceTam = "";
        }
        return sourceTam;
    }

    public HashMap<String, String[]> readFileIntoHashMap(String filename, int numColumns, int keyIndex) {
        try {
            HashMap<String, String[]> hm = new HashMap<String, String[]>();
            String tam_dict_mar_hin = USER_HOME + File.separator + "data_bin" + File.separator
                    + "sl_tl" + File.separator + "lexicaltransfer" + File.separator + "TamDict" + File.separator;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tam_dict_mar_hin + filename)));
            while (br.ready()) {
                String[] record = new String[numColumns];

                String line = br.readLine();
                if (line.length() < numColumns) {
                    continue;
                }
                if (line.startsWith("#")) {
                    continue;
                } else {
                    StringTokenizer stk = new StringTokenizer(line);
                    for (int c = 0; c < record.length; c++) {
                        record[c] = stk.nextToken();
                        if (record[c].equals("___")) // special entries for null string
                        {
                            record[c] = "";
                        }
                    }
                    if (!hm.containsKey(record[keyIndex])) {
                        hm.put(record[keyIndex], record);
                    }
                }
            }
            return hm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stripDuplicatesFromFile(String filename) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        Set<String> lines = new HashSet<String>(10000); // maybe should be bigger
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String unique : lines) {
            writer.write(unique);
            writer.newLine();
        }
        writer.close();
    }
}

