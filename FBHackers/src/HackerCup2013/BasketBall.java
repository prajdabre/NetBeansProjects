/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HackerCup2013;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author RAJ
 */
public class BasketBall {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/RAJ/Documents/NetBeansProjects/FBHackers/src/HackerCup2013/infile.txt")));
        String line = "";
        int cases = Integer.parseInt(br.readLine().trim());

        for (int case_counter = 0; case_counter < cases; case_counter++) {
            line = br.readLine().trim();
            int N, M, P;
            N = Integer.parseInt(line.split(" ")[0]);
            M = Integer.parseInt(line.split(" ")[1]);
            P = Integer.parseInt(line.split(" ")[2]);
            List<players> player_list = new ArrayList<players>();
            for (int player_counter = 0; player_counter < N; player_counter++) {
                line = br.readLine().trim();
                player_list.add(new players(line.split(" ")[0], Integer.parseInt(line.split(" ")[1]), Integer.parseInt(line.split(" ")[2])));
            }
            Collections.sort(player_list, new PlayerComparator());
            List<players> teamA = new ArrayList<players>();
            List<players> teamB = new ArrayList<players>();
            int i = 0;
            for (players p : player_list) {
                i++;
                p.draft_number = i;
                //System.out.println(p.name + " " + p.rating + " " + p.height + " " + p.minutes_played + " " + i);
                if (i % 2 == 0) {
                    teamB.add(p);
                } else {
                    teamA.add(p);
                }
            }

            List<players> teamAplay = new ArrayList<players>();
            List<players> teamAreserve = new ArrayList<players>();
            List<players> teamBplay = new ArrayList<players>();
            List<players> teamBreserve = new ArrayList<players>();
            if (teamA.size() < P || teamB.size() < P) {
                System.out.println("Error");
                System.exit(1);
            }

            teamAreserve = new ArrayList<players>(teamA.subList(teamA.size() - P, teamA.size()));
            teamBreserve = new ArrayList<players>(teamB.subList(teamB.size() - P, teamB.size()));
            teamAplay = new ArrayList<players>(teamA.subList(0, P));
            teamBplay = new ArrayList<players>(teamB.subList(0, P));

            if (teamAplay.size() != teamA.size()) {

                for (int timer = 1; timer <= M; timer++) {

                    players toremove = teamAplay.get(teamAplay.size() - 1);
                    players toadd = teamAreserve.get(0);
                    toremove.minutes_played++;
                    teamAplay.set(teamAplay.size() - 1, toremove);
                    for (int player_counter = teamAplay.size() - 2; player_counter >= 0; player_counter--) {
                        players current = teamAplay.get(player_counter);
                        current.minutes_played++;
                        teamAplay.set(player_counter, current);
                        if (current.minutes_played > toremove.minutes_played || (current.minutes_played == toremove.minutes_played && current.draft_number > toremove.draft_number)) {
                            toremove = current;
                        }
                    }

                    for (int player_counter = 1; player_counter < teamAreserve.size(); player_counter++) {
                        players current = teamAreserve.get(player_counter);

                        if (current.minutes_played < toadd.minutes_played || (current.minutes_played == toadd.minutes_played && current.draft_number < toadd.draft_number)) {
                            toadd = current;

                        }
                    }

                    teamAplay.remove(toremove);
                    teamAreserve.remove(toadd);
                    teamAplay.add(toadd);
                    teamAreserve.add(0, toremove);

//                    System.out.println("Team A players currently:");
//                    for (players p : teamAplay) {
//                        System.out.println(p.name + " " + p.rating + " " + p.height + " " + p.minutes_played + " " + p.draft_number);
//                    }
                }



            }
//            System.out.println("Team B players currently");
//            for (players p : teamBplay) {
//                System.out.println(p.name + " " + p.rating + " " + p.height + " " + p.minutes_played + " " + p.draft_number);
//            }
            if (teamBplay.size() != teamB.size()) {

                for (int timer = 1; timer <= M; timer++) {
                    players toremove = teamBplay.get(teamBplay.size() - 1);
                    players toadd = teamBreserve.get(0);
                    toremove.minutes_played++;
                    teamBplay.set(teamBplay.size() - 1, toremove);
                    for (int player_counter = teamBplay.size() - 2; player_counter >= 0; player_counter--) {
                        players current = teamBplay.get(player_counter);
                        current.minutes_played++;
                        teamBplay.set(player_counter, current);
                        if (current.minutes_played > toremove.minutes_played || (current.minutes_played == toremove.minutes_played && current.draft_number > toremove.draft_number)) {
                            toremove = current;
                        }
                    }

                    for (int player_counter = 1; player_counter < teamBreserve.size(); player_counter++) {
                        players current = teamBreserve.get(player_counter);
                        if (current.minutes_played < toadd.minutes_played || (current.minutes_played == toadd.minutes_played && current.draft_number < toadd.draft_number)) {
                            toadd = current;
                        }
                    }

                    teamBplay.remove(toremove);
                    teamBreserve.remove(toadd);
                    teamBplay.add(toadd);
                    teamBreserve.add(0, toremove);


                }



            }
            List<String> names = new ArrayList<String>();

            for (players p : teamAplay) {
                names.add(p.name);
            }
            for (players p : teamBplay) {
                names.add(p.name);
            }

            Collections.sort(names);

            System.out.println("Case #"+(case_counter+1)+": "+names.toString().substring(1, names.toString().length()-1).replaceAll(",", ""));
//            for (String s : names) {
//                System.out.print(s + " ");
//            }
            //System.out.println("");



        }
    }
}

class players {

    String name;
    Integer height;
    Integer rating;
    Integer minutes_played = 0;
    Integer draft_number;

    public players() {
    }

    public players(String name, Integer rating, Integer height) {
        this.name = name;
        this.height = height;
        this.rating = rating;
    }
}

class PlayerComparator implements Comparator<players> {

    public int compare(players p1, players p2) {
        if (p1.rating > p2.rating) {
            return 0;
        } else if (p1.rating == p2.rating && p1.height > p2.height) {

            return 0;
        } else {
            return 1;
        }
    }
}
