
import java.util.*;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

class SearchFlipkart extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>FlipkartObjects = new ArrayList<SearchObject>();
	
	SearchFlipkart(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}
	
	public void run() {
		try {
			Document docFlip = Jsoup.connect(finalQuery).get();

			String docTitle = docFlip.title();
			System.out.print("Searching... ");
			System.out.println(docTitle);
						
			String Name, Price, Imgurl;
			
			switch(type) {
			case "Watch": 
				Elements flipWatches = docFlip.select("div[data-pid]");
				for (Element link : flipWatches) {
	
					Name = link.select("div[class=pu-title fk-font-13]").text();
					Price = link.select("div[class=pu-final]").text();
					Imgurl = link.select("img[height]").attr("data-src");
					
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					FlipkartObjects.add(obj);
/*					System.out.println(obj.getName() + Name);
					System.out.println(obj.getPrice());
					System.out.println(obj.getImgurl());
*/				}

			case "Mobile": 
				Elements flipMobiles = docFlip.select("div[data-pid]");
				for (Element link : flipMobiles) {
					
					Price=link.select("div[class=pu-final]").text();
					Name=link.select("div[class=pu-title fk-font-13]").text();
					Imgurl=link.select("img[height]").attr("data-src");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Mobile");
					FlipkartObjects.add(obj);
				}

			case "Shoe": 
				Elements flipShoes = docFlip.select("div[class^=product-unit]");
				for (Element link : flipShoes) {

					Imgurl=link.select("img[height]").attr("data-src");
					Name=link.select("div[class=pu-title fk-font-13]").text();
					Price=link.select("div[class=pu-final]").text();
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Shoe");
					FlipkartObjects.add(obj);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class SearchEbay extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>EbayObjects = new ArrayList<SearchObject>();
	
	SearchEbay(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}
	
	public void run() {
		try {
			Document docFlip = Jsoup.connect(finalQuery).get();

			String docTitle = docFlip.title();
			System.out.print("Searching... ");
			System.out.println(docTitle);
						
			String Name, Price, Imgurl;
			
			switch(type) {
			case "Watch": 
				Elements ebayWatches = docFlip.select("table[itemtype]");
				for (Element link : ebayWatches) {
					Name = link.select("img[itemprop]").attr("alt");
					Imgurl = link.select("img[itemprop]").attr("src");
					Price = link.select("div[itemprop=price]").text();
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					EbayObjects.add(obj);
				}

			case "Mobile": 
				Elements ebayMobiles = docFlip.select("table[itemtype]");
				for (Element link : ebayMobiles) {
					Name = link.select("img[itemprop]").attr("alt");
					Imgurl = link.select("img[itemprop]").attr("src");
					Price = link.select("div[itemprop=price]").text();
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Mobile");
					EbayObjects.add(obj);
				}

			case "Shoe": 
				Elements ebayShoes = docFlip.select("div[class=lyr]");
				for (Element link : ebayShoes) {
					Name = link.select("img[itemprop]").attr("alt");
					Price = link.select("span[itemprop=price]").text();
					Imgurl = link.select("img[itemprop]").attr("src");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Shoe");
					EbayObjects.add(obj);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


class SearchJabong extends Thread {
	private String finalQuery;
	private String type;
	ArrayList<SearchObject>JabongObjects = new ArrayList<SearchObject>();
	
	SearchJabong(String finalQuery, String objType) {
		this.finalQuery = finalQuery;
		this.type = objType;
	}
	
	public void run() {
		try {
			Document docFlip = Jsoup.connect(finalQuery).get();

			String docTitle = docFlip.title();
			System.out.print("Searching... ");
			System.out.println(docTitle);
						
			String Name, Price, Imgurl;
			
			switch(type) {
			case "Watch": 
				Elements jabongWatches = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongWatches) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;


			case "Mobile": 
				Elements jabongMobiles = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongMobiles) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;

			case "Shoe": 
				Elements jabongShoes = docFlip.getElementsByAttributeValue("class","fleft pro-content");
				for (Element link : jabongShoes) {
					String temp = link.select("a[href]").attr("href").replace('-', ' ');
					Name = temp.substring(1, temp.lastIndexOf("."));
					Imgurl = link.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
					Price = link.select("a[href]").select("strong").text().replaceAll("&nbsp;", " ");
					SearchObject obj = new SearchObject(Name, Price, Imgurl, "Watch");
					JabongObjects.add(obj);
				}
				break;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class ComPrice {

	SearchFlipkart flipkart;
	SearchEbay ebay;
	SearchJabong jabong;
	
	ComPrice(String queryString, String type) {
		final String authUser = "11305R012";
		final String authPassword = "8PHKWfly@";
		Authenticator.setDefault(
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(authUser, authPassword.toCharArray());
					}
				}
				);
		System.setProperty("http.proxyHost","netmon.iitb.ac.in" );
		System.setProperty("http.proxyPort", "80");
		System.setProperty("http.proxyUser", authUser);
		System.setProperty("http.proxyPassword", authPassword);

		String wellFormedQuery = queryString.replaceAll(" ", "+");
		
		String flipQuery = "http://www.flipkart.com/search?q=" + wellFormedQuery + "&as=off&as-show=off&otracker=start";
		flipkart = new SearchFlipkart(flipQuery, type);
		
		String ebayQuery = "http://www.ebay.in/sch/i.html?_trksid=p3902.m570.l1313.TR8.TRC0.A0.X" + wellFormedQuery + "&_nkw=" + wellFormedQuery+"&_sacat=0&_from=R40";
		ebay = new SearchEbay(ebayQuery, type);
		
		String jabongQuery = "http://www.jabong.com/find/" + queryString.replaceAll(" ","-");
		jabong = new SearchJabong(jabongQuery, type);
	}
	
	void fetchResult() {
		flipkart.start();
		ebay.start();
		jabong.start();
		
		try {
			flipkart.join();
			ebay.join();
			jabong.join();
/*			
			for(SearchObject fp : flipkart.FlipkartObjects) {
				System.out.println(fp.getName());
				System.out.println(fp.getPrice());
				System.out.println(fp.getImgurl());
			}

			for(SearchObject eb : ebay.EbayObjects) {
				System.out.println(eb.getName());
				System.out.println(eb.getPrice());
				System.out.println(eb.getImgurl());
			}
			
			for(SearchObject jb : jabong.JabongObjects) {
				System.out.println(jb.getName());
				System.out.println(jb.getPrice());
				System.out.println(jb.getImgurl());
			}
			
*/		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}