
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
class holes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line ="";
        int k = Integer.parseInt(br.readLine().trim());
        for(int i=0;i<k;i++){
            line=br.readLine().trim();
            int count=0;
            char[] chars=line.toCharArray();
            for(char c:chars){
                if(c=='A'||c=='D'||c=='O'||c=='P'||c=='R'||c=='Q'){
                    count++;
                } else if(c=='B'){
                    count++;
                    count++;
                }
            }
            System.out.println(count);
        }
    }

}
