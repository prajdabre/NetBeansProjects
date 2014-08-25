/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HackerCup2013;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RAJ
 */
public class Tennison {

    /**
     * @param args the command line arguments
     */
    static Double wincurr(Double Ps, Double Pr, Double Pi) {
        return (Pi * Ps + (1 - Pi) * Pr);

    }

    static Double winprob(Integer K, Integer OutOf, Double Ps, Double Pr, Double Pi, Double Pu, Double Pw, Double Pd, Double Pl) {



        Double pWincurr = wincurr(Ps, Pr, Pi);
        Double pLosecurr = 1 - pWincurr;
        Double PiOnWin = Pi + Pu * Pw;
        if (PiOnWin >= 1.0) {
            PiOnWin = 1.0;
        }
        Double PiOnLose = Pi - Pd * Pl;
        if (PiOnLose <= 0.0) {
            PiOnLose = 0.0;
        }
        if (K == 0) {
            return 1.0;
        }

        if (K == OutOf) {
            return pWincurr * winprob(K - 1, K - 1, Ps, Pr, PiOnWin, Pu, Pw, Pd, Pl);
        }
        return pWincurr * winprob(K - 1, OutOf - 1, Ps, Pr, PiOnWin, Pu, Pw, Pd, Pl) + pLosecurr * winprob(K, OutOf - 1, Ps, Pr, PiOnLose, Pu, Pw, Pd, Pl);

    }

    static Double winprobdp(Integer K, Integer OutOf, Double Ps, Double Pr, Double Pi, Double Pu, Double Pw, Double Pd, Double Pl) {

        List<Double> sunprobs = new ArrayList<Double>();
        sunprobs.add(Pi);
        List<Double> temp = new ArrayList<Double>();

        while (true) {


            for (Double d : sunprobs) {
                if (!sunprobs.contains(d + Pu * Pw) && !temp.contains(d + Pu * Pw)) {
                    if (d + Pu * Pw < 1.0) {
                        temp.add(d + Pu * Pw);
                    }
                }
                if (!sunprobs.contains(d - Pd * Pl) && !temp.contains(d - Pd * Pl)) {
                    if (d - Pd * Pl > 0) {
                        temp.add(d - Pd * Pl);
                    }
                }
                if (!sunprobs.contains(d + Pu * Pw - Pd * Pl) && !temp.contains(d + Pu * Pw - Pd * Pl)) {
                    if (d + Pu * Pw - Pd * Pl > 0 && d + Pu * Pw - Pd * Pl < 1.0) {
                        temp.add(d + Pu * Pw - Pd * Pl);
                    } 
                }



            }
            //System.out.println(temp);
            if (temp.size() == 0) {
                break;
            }
            sunprobs.addAll(temp);
            temp = new ArrayList<Double>();
        }

        System.out.println(sunprobs.size());
        Double solnmatrix[][] = new Double[K + 1][OutOf + 1];
        solnmatrix[0][0] = 1.0;
        for (int i = 1; i <= OutOf; i++) {
            solnmatrix[0][i] = 1.0;
        }
        for (int i = 1; i <= K; i++) {
            solnmatrix[i][0] = 0.0;
        }

        for (int i = 1; i <= K; i++) {
            for (int j = 1; j <= OutOf; j++) {
                if (i == j) {
                    //solnmatrix[i][j] = Pwincurr * solnmatrix[i-1][j-1];
                } else {
                }
            }
        }
        Double pWincurr = wincurr(Ps, Pr, Pi);
        Double pLosecurr = 1 - pWincurr;
        Double PiOnWin = Pi + Pu * Pw;
        if (PiOnWin >= 1.0) {
            PiOnWin = 1.0;
        }
        Double PiOnLose = Pi - Pd * Pl;
        if (PiOnLose <= 0.0) {
            PiOnLose = 0.0;
        }
        if (K == 0) {
            return 1.0;
        }

        if (K == OutOf) {
            return pWincurr * winprob(K - 1, K - 1, Ps, Pr, PiOnWin, Pu, Pw, Pd, Pl);
        }
        return pWincurr * winprob(K - 1, OutOf - 1, Ps, Pr, PiOnWin, Pu, Pw, Pd, Pl) + pLosecurr * winprob(K, OutOf - 1, Ps, Pr, PiOnLose, Pu, Pw, Pd, Pl);

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/HackerCup2013/infile.txt")));
        String line = "";
        int cases = Integer.parseInt(br.readLine().trim());

        for (int case_counter = 0; case_counter < cases; case_counter++) {
            line = br.readLine().trim();
            String components[] = line.split(" ");
            Integer K;
            double Ps, Pr, Pi, Pu, Pw, Pd, Pl;
            K = Integer.parseInt(components[0]);
            Ps = Double.parseDouble(components[1]);
            Pr = Double.parseDouble(components[2]);
            Pi = Double.parseDouble(components[3]);
            Pu = Double.parseDouble(components[4]);
            Pw = Double.parseDouble(components[5]);
            Pd = Double.parseDouble(components[6]);
            Pl = Double.parseDouble(components[7]);

            System.out.println("Case #" + (case_counter + 1) + ": " + winprobdp(K, 2 * K - 1, Ps, Pr, Pi, Pu, Pw, Pd, Pl));
        }

    }
}
