/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdfextractor;

import com.asprise.util.pdf.PDFReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author RAJ
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        File dir = new File("C:/Users/RAJ/Downloads/CREOLE/");
        for (File f : dir.listFiles()) {
            PDFReader reader = new PDFReader(new File("PDF5novela.pdf"));
            reader.open(); // open the file.
            int pages = reader.getNumberOfPages();

            for (int i = 0; i < pages; i++) {
                String text = reader.extractTextFromPage(i);
                System.out.println("Page " + i + ": " + text);
            }



            reader.close(); // finally, close the file.
        }

    }
}
