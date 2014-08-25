
import java.io.*;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author RAJ
 */
public class maxsumtriangle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        String line = "";
        int cases = Integer.parseInt(st.nextToken());
        for (int i = 0; i < cases; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int size = Integer.parseInt(st.nextToken());
            int triangle[][] = new int[size][size];
            for (int j = 0; j < size; j++) {
                int pos = 0;
                st = new StringTokenizer(br.readLine().trim());
                while (st.hasMoreTokens()) {
                    triangle[j][pos] = Integer.parseInt(st.nextToken());
                    pos++;
                }

            }
            //System.out.println(maxsum(triangle, 0, 0, size));
            System.out.println(maxsum_dynamic(triangle, size));
        }
    }

    static int maxsum(int[][] triangle, int x, int y, int size) {
        if (x == size || y == size) {
            return 0;
        }
        return (triangle[x][y] + Math.max(maxsum(triangle, x + 1, y, size), maxsum(triangle, x + 1, y + 1, size)));
    }

    static int maxsum_dynamic(int[][] triangle, int size) {
        int max_till_now = triangle[0][0];
        int max_dist[][] = new int[size][size];
        max_dist[0][0] = triangle[0][0];
        for (int i = 1; i < size; i++) {
            max_dist[i][0] = max_dist[i - 1][0] + triangle[i][0];
            if (max_dist[i][0] > max_till_now) {
                max_till_now = max_dist[i][0];
            }
            for (int j = 1; j <= i; j++) {
                max_dist[i][j] = triangle[i][j] + Math.max(max_dist[i - 1][j - 1], max_dist[i - 1][j]);
                if (max_dist[i][j] > max_till_now) {
                    max_till_now = max_dist[i][j];
                }
            }

        }
        return max_till_now;
    }
}
