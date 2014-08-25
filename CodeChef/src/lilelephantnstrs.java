
import java.io.*;
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RAJ
 */
class lilelephantnstrs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        String line ="";
        int k = Integer.parseInt(st.nextToken());

        int n = Integer.parseInt(st.nextToken());

        String[] goodstrs = new String[k];

        for (int i = 0; i < k; i++) {
            line = br.readLine();
            goodstrs[i]=line;

        }
        for (int i = 0; i < n; i++) {
            int gorb = 0;
            line = br.readLine();

            if (line.length() >= 47) {
                gorb = 1;
                
            } else {
                for (String s : goodstrs) {
                    if (line.contains(s)) {
                        gorb = 1;
                        break;
                    }
                }

            }
            if (gorb == 1) {
                System.out.println("Good");
            } else {
                System.out.println("Bad");
            }
        }
        br.close();
        System.exit(0);
    }
}
