/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author RAJ
 */
public class outline {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String s[] = {".##.", "..#.", ".###", "..#."};
        System.out.println(largestResult(2, 3));
    }

    static int outlineLengthEasy(int R, int C, String[] figure) {
        char content[][] = new char[R][C];
        for (int i = 0; i < R; i++) {
            char thisrow[] = figure[i].toCharArray();
            for (int j = 0; j < C; j++) {
                content[i][j] = thisrow[j];
            }

        }
        int border = 0;
        if (R == 1 && C == 1) {
            if (content[0][0] == '#') {
                return 4;
            }
        }
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (content[i][j] == '#') {
                    if (i == 0) {
                        if (j == 0) {
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            border++;
                            border++;
                        } else if (j == C - 1) {
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            border++;
                            border++;
                        } else {
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            border++;
                        }
                    } else if (i == R - 1) {
                        if (j == 0) {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            border++;
                            border++;
                        } else if (j == C - 1) {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            border++;
                            border++;
                        } else {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            border++;
                        }
                    } else {
                        if (j == 0) {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                            border++;

                        } else if (j == C - 1) {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                            border++;

                        } else {
                            if (content[i - 1][j] == '.') {
                                border++;
                            }
                            if (content[i][j + 1] == '.') {
                                border++;
                            }
                            if (content[i][j - 1] == '.') {
                                border++;
                            }
                            if (content[i + 1][j] == '.') {
                                border++;
                            }
                        }
                    }

                }

            }
        }

        return border;

    }

    static int largestResult(int D, int O) {
        return getlargest(0, O, 1, D,2);
}

    static int getlargest(int opsdone, int limit, int tillnow, int D, int startfrom){

        int maxtillnow = tillnow;

        
        for(int i=startfrom;i<=9;i++){
            
            }
        }
    }
