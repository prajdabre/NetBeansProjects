
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class coindeno {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int coll[] = {12, 4, 5};
        System.out.println(minCoins(coll, 11));
    }

    static int minCoins(int[] coinDenominations, int S) {
        Arrays.sort(coinDenominations);
        int len = coinDenominations.length;
        int[] test = new int[S + 1];
        test[0] = 0;
        for (int i = 0; i < len; i++) {
            for (int j = coinDenominations[i]; j <= S; j++) {
                if (j % coinDenominations[i] == 0) {
                    test[j] = (j / coinDenominations[i]);
                    //System.out.println(j+" "+test[j]);
                } else {
                    //System.out.println(j / coinDenominations[i]+ " " + test[j % coinDenominations[i]]+" "+j);
                    test[j] = ((j / coinDenominations[i]) + test[j % coinDenominations[i]] )<test[j]?((j / coinDenominations[i]) + (test[j % coinDenominations[i]] > 0 ? test[j % coinDenominations[i]] : (-j / coinDenominations[i]))):test[j];
                }
            }
        }
        //System.out.println(test[S]);
        return test[S] == 0 ? -1 : test[S];

    }
}
