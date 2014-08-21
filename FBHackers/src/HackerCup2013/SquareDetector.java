/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HackerCup2013;

import java.io.*;
import java.util.Collections;

/**
 *
 * @author RAJ
 */
public class SquareDetector {

    /**
     * @param args the command line arguments
     */
    public static int min3(int a, int b, int c) {
        return ((a < b ? a : b) < c) ? (a < b ? a : b) : c;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/HackerCup2013/infile.txt")));
        String line = "";
        int cases = Integer.parseInt(br.readLine().trim());
        for (int case_counter = 0; case_counter < cases; case_counter++) {
            int square_size = Integer.parseInt(br.readLine().trim());
            int black_count = 0;
            int max_square = 0;
            int square_data[][] = new int[square_size][square_size];
            for (int data_line_counter = 0; data_line_counter < square_size; data_line_counter++) {
                line = br.readLine().trim();
                int col = 0;

                for (char b_o_w : line.toCharArray()) {
                    if (b_o_w == '.') {
                        square_data[data_line_counter][col] = 0;
                        if (data_line_counter != 0 && col != 0) {
                            int i_1j_1 = square_data[data_line_counter - 1][col - 1];
                            int i_1j = square_data[data_line_counter - 1][col];
                            int ij_1 = square_data[data_line_counter][col - 1];
                            if(i_1j>0 && i_1j_1>0 && ij_1>0){
                                break;
                            }
                        }

                    } else if (b_o_w == '#') {
                        square_data[data_line_counter][col]=1;
                        black_count++;
                        if (data_line_counter != 0 && col != 0) {
                            int i_1j_1 = square_data[data_line_counter - 1][col - 1];
                            int i_1j = square_data[data_line_counter - 1][col];
                            int ij_1 = square_data[data_line_counter][col - 1];
                            square_data[data_line_counter][col] = Math.min(ij_1, Math.min(i_1j, i_1j_1)) + 1;
                            max_square = max_square > square_data[data_line_counter][col] ? max_square : square_data[data_line_counter][col];
                            
                        }

                    }
                    col++;

                }

            }
//            for(int i=0;i<square_size;i++){
//                for(int j=0;j<square_size;j++){
//                    System.out.print(square_data[i][j]+" ");
//                }
//                System.out.println("");
//            }
//            System.out.println(max_square);
            if (black_count == Math.pow(max_square, 2)) {
                System.out.println("Case #" + (case_counter+1) + ": YES");
            } else {
                System.out.println("Case #" + (case_counter+1) + ": NO");
            }
            

        }
    }
}
