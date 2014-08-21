/**
 * 
 */
package com.pos.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pos.common.Constants;

/**
 * @author swapnil
 * 
 */
public class ResourceReader {
	public static Pattern TAG_PATTERN = Pattern
			.compile(Constants.TAG_PATTERN_STRING);
	public static Pattern TAG_PATTERN_END = Pattern
			.compile(Constants.END_TAG_PATTERN_STRING);
	public static Pattern MULTI_TAG_PATTERN = Pattern
			.compile(Constants.MULTI_TAG_PATTERN_STRING);

	/**
	 * @param fileName - File to be read
	 * @throws IOException
	 * 
	 * Given a fileName, this function prints all words and their corresponding tags on std output stream 
	 */
	public static void readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));

		String line = reader.readLine();

		while (line != null) {
			line = line.trim();
			// line= line.replaceAll(" +", " ");
			String[] words = line.split(" ");

			for (String string : words) {
				if (string.matches(Constants.MULTI_TAG_PATTERN_STRING)) {
					Matcher mm = MULTI_TAG_PATTERN.matcher(string);
					while (mm.find()) {
						String word = string.substring(mm.start(), mm.end());
						String[] arr = word.split("_");
						mm.replaceFirst("");
						System.out.println(arr[0] + " : " + arr[1]);
						// System.out.println(string.substring(m.start(),m.end())+":"+m.groupCount());
					}
				}

				else if (string.matches(Constants.TAG_PATTERN_STRING_END)) {
					String temp = string;

					int ind = string.substring(0, string.lastIndexOf("_"))
							.lastIndexOf("_");
					if (ind == temp.length()||ind==-1) {
						ind = 0;
					}
					temp = string.substring(ind);
					// System.out.println("==============="+string+"--"+temp);
					Matcher mm = MULTI_TAG_PATTERN.matcher(temp);
					int start = temp.length();
					while (mm.find()) {
						String word = temp.substring(mm.start(), mm.end());
						String[] arr = word.split("_");
						mm.replaceFirst("");
						start = mm.start();
						System.out.println(arr[0] + " : " + arr[1]);
						//TODO Add code to fill the data in appropriate data structures
						//arg[0] is the tag; arg[1] is word
						// System.out.println(string.substring(m.start(),m.end())+":"+m.groupCount());
					}
					temp = temp.substring(0, start);
					start = temp.length();
					Matcher m = TAG_PATTERN.matcher(temp);
					while (m.find()) {
						// System.out.println(temp.substring(m.start(),m.end()));
						String word = temp.substring(m.start(), m.end());
						String[] arr = word.split("_");
						m.replaceFirst("");
						start = m.start();
						System.out.println(arr[0] + "  " + arr[1]);
						//TODO Add code to fill the data in appropriate data structures
						// System.out.println(temp.substring(m.start(),m.end())+":"+m.groupCount());
					}

				} else if (string.matches(Constants.TAG_PATTERN_STRING)) {
					Matcher m = TAG_PATTERN.matcher(string);
					while (m.find()) {
						// System.out.println(string.substring(m.start(),m.end()));
						String word = string.substring(m.start(), m.end());
						String[] arr = word.split("_");
						m.replaceFirst("");
						System.out.println(arr[0] + "  " + arr[1]);
						// System.out.println(string.substring(m.start(),m.end())+":"+m.groupCount());
					}
				} else {
					//Junk/untagged words are skipped
//					System.out.println("No pattern matched for :" + string);
				}
			}
			line = reader.readLine();
		}

	}

	/**
	 * @param directoryName
	 * @throws IOException
	 * Given a directory name this funtion reads all the files within the directory
	 */
	public static void readDirectory(String directoryName) throws IOException {
		File dir = new File(directoryName);
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				readFile(file);
			}
		}
	}
	
	public static void readFile(String fileName) throws IOException {
		readFile(new File(fileName));
		
	}

	public static void main(String[] args) throws IOException {
//		readFile(Constants.SRC_DIR + "/set0/test.txt");
		readDirectory(Constants.SRC_DIR + "/set0");
	}
}
