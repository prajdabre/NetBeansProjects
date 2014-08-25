/**
 * 
 */
package sl.morph.mar.transducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import sandhisplittermainpack.io.resources;
/**
 * @author swapnil
 *
 */
public class Main {
    private static Vector<String> a;
    private static BufferedReader reader1;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Transducer transducer = new Transducer(resources.morphotactpath);
//		transducer.getParsedOutput("उत्तर", null);
		
			a= transducer.getParsedOutput("ऊर्जित");
                        System.out.println(a);
                        System.out.println(a.firstElement().contains("no analysis for "+"उत्तaरासाठी"));
		
                
	}

}
