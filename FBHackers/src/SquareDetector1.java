import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */

/**
 * @author swapnil
 *
 */
public class SquareDetector1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String line = null;
		int noOfTestCases = Integer.parseInt(reader.readLine().trim());

		for (int i = 0; i < noOfTestCases; i++) {
			line = reader.readLine();
			String arr[]  = line.split(" ");
			int n = Integer.parseInt(arr[0]);
			int m = Integer.parseInt(arr[1]);
			int p = Integer.parseInt(arr[2]);

			List<Player> list = new ArrayList<SquareDetector1.Player>();
			for (int j = 0; j < n; j++) {
				line = reader.readLine();
				arr = line.split(" ");
				Player player = new Player(arr[0],Integer.parseInt(arr[1]),Integer.parseInt(arr[2]));
				list.add(player);
			}
			if(2*p ==n) {
				printInfo(list,i);
				continue;
			}else {
				Collections.sort(list);
			}
			Player[] team1 = new Player[(n+1)/2];
			Player[] team2 = new Player[(n)/2];
			int ind1 = 0;
			int ind2=0;
//			System.out.println(list);
			for (int j = 0; j < n; j++) {
				if(j%2==0) {
					//add to team1
					if(ind1<p) {
						team1[p-ind1-1] = list.get(j);
						ind1++;
					}else {
						team1[ind1] = list.get(j);
						ind1++;
					}
				}else {
					if(ind2<p) {
						team2[p-ind2-1] = list.get(j);
						ind2++;
					}else {
						team2[ind2] = list.get(j);
						ind2++;
					}
				}


			}
			int a = 0;
			int b = p-1;

			int size = n/2;
			if(n%2!=0) {
				size = (n+1)/2;
			}
			int m1 = m%(size);
			a = (a+m1)%size;
			b = (b+m1)%size;
			ind1=a;
			list.clear();
			while(ind1!=b) {
//				System.out.println(ind1+" "+team1[ind1]+" "+team2[ind1]);
				list.add(team1[ind1]);
//				list.add(team2[ind1]);
				ind1 = (ind1+1)%size;
			}
			list.add(team1[ind1]);
			size = n/2;
			a=0;
			b=p-1;
			m1 = m%(size);
			a = (a+m1)%size;
			b = (b+m1)%size;
			ind1=a;
			while(ind1!=b) {
//				System.out.println(ind1+" "+team1[ind1]+" "+team2[ind1]);
//				list.add(team1[ind1]);
				list.add(team2[ind1]);
				ind1 = (ind1+1)%size;
			}
			list.add(team2[ind1]);
			printInfo(list,i);

		}

		reader.close();

	}
	private static void printInfo(List<Player> list,int i) {
		Collections.sort(list,new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
//				System.out.println(o1+" "+o2);
				return o1.name.compareTo(o2.name);
			}
		});
		String name = "";
		for (Player player : list) {
			name+=player.name+" ";
		}
		name = name.trim();
		System.out.println("Case #"+(i+1)+": "+name);
	}
	static class Player implements Comparable<Player>{
		String name;
		Integer shotPercent;
		Integer height;
		public Player() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Player(String name, Integer shotPercent, Integer height) {
			super();
			this.name = name;
			this.shotPercent = shotPercent;
			this.height = height;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getShotPercent() {
			return shotPercent;
		}
		public void setShotPercent(Integer shotPercent) {
			this.shotPercent = shotPercent;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		@Override
		public boolean equals(Object arg0) {
			Player p = (Player) arg0;
			return name.equals(p.name);
		}
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		@Override
		public int compareTo(Player p) {
			if(shotPercent>p.shotPercent) {
				return -1;
			}
			if(shotPercent==p.shotPercent) {
				return -1*height.compareTo(p.height);
			}else {
				return 1;
			}
		}
		@Override
		public String toString() {
			return name;
		}


	}

}

/*
 Case #1: Anil Doan Duc Fabien Manohar Paul Rajat Yintao
Case #2: Erling Zejia
Case #3: Bhuwan Duc Fabien Meihong Vlad Wai
Case #4: Aravind Ekansh
Case #5: Lingjuan Zejia
Case #6: Purav Slawek Wai Weiyan
Case #7: Anil Aravind Jan Jan Victor Viswanath
Case #8: Anil Igor Purav Sanjeet
Case #9: Atol Clifton Clifton Doan John John Purav Song
Case #10: Aleksandar Aravind Bhuwan Igor
Case #11: Ahmed Anshuman Jan Ranjeeth
Case #12: Fabien Weiyan
Case #13: Doan Duc Gaurav John John Jordan Liang Rudradev Thomas Viswanath
Case #14: Anshuman Atol Paul Wai
Case #15: Anh Luiz Ranjeeth Roman Steaphan Weitao Weitao Zihing
Case #16: Andrei Doan Ekansh Erling Fabien Zef
Case #17: Fabien Kittipat Liang Paul
Case #18: Atol Bhuwan Bhuwan Lin Nima Vladislav Voja Voja Yingsheng Zainab
Case #19: Andriy Jan Lingjuan Wenjie Xiao Yingsheng
Case #20: Doan John
 *
Case #1: Anil Doan Fabien Manohar Meihong Paul Tom Yintao
Case #2: Erling Zejia
Case #3: Bhuwan Duc Fabien Meihong Vlad Wai
Case #4: Aravind Erling
Case #5: Lingjuan Zejia
Case #6: Purav Slawek Wai Weiyan
Case #7: Aravind Kittipat Victor Viswanath Wenjie Yintao
Case #8: Nima Oleksandr Sanjeet Saransh
Case #9: Chad Clifton Doan John John Mehdi Paul Saransh
Case #10: Aleksandar Aravind Bhuwan Igor
Case #11: Ahmed Liang Lingjuan Sanjeet
Case #12: Atol Ekansh
Case #13: Doan Duc Gaurav John John Jordan Liang Rudradev Thomas Viswanath
Case #14: Manohar Paul Wei Wenjie
Case #15: Andras Anh Anil Ranjeeth Steaphan Weitao Weitao Zihing
Case #16: Andrei Doan Ekansh Erling Fabien Zef
Case #17: Fabien Kittipat Liang Paul
Case #18: Fabien Lin Purav Ranjeeth Rudradev Vladislav Voja Voja Yingsheng Zainab
Case #19: Ajay Andriy Ekansh Torbjorn Wenjie Yingsheng
Case #20: Doan Nathan
 */