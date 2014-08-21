
import java.io.*;
import javax.servlet.*;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


//@WebServlet("/SearchResults")
public class SearchResults extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException {



		//Access the data from the http request
		String type = request.getParameter("type");
		String query = request.getParameter("query");

                System.out.println("fsefssg");
		// Use the JDBC code given as sample to obtain data from database 
		// Alternatively to keep the example simple the search results are embedded in below code

		ComPrice cp = new ComPrice(query, type);
		cp.fetchResult();
		
/*
		for(SearchObject eb : cp.ebay.EbayObjects) {
			System.out.println(eb.getName());
			System.out.println(eb.getPrice());
			System.out.println(eb.getImgurl());
		}

		for(SearchObject jb : cp.jabong.JabongObjects) {
			System.out.println(jb.getName());
			System.out.println(jb.getPrice());
			System.out.println(jb.getImgurl());
		}


*/		

		//Display the results by writing to the http response output stream.

		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.print(type+" "+query);
		out.println("<head><title> Search Result </title> </head>");
		out.println("<body>");
		out.println("<p>");
		out.println("Search Results:");
		out.println("<table border=\"1\">");
/*		out.println("<tr align=\"center\"> <td colspan=\"3\">Flipkart</td>" +
				"<td colspan=\"3\">Ebay</td><td colspan=\"3\">Jabong</td> </tr>");
*/		out.println("<td colspan=\"2\">Name</td> <td>Price </td>");


		for(SearchObject fp : cp.flipkart.FlipkartObjects) {
			out.println("<tr align=\"center\"> <td> <img src=\"" + fp.getImgurl() + "\" /> </td> <td>" +
					fp.getName() + "</td> <td>" + fp.getPrice() + "</td>");
		}
		out.println("</tr> <tr><td colspan=\"2\">Name</td> <td>Price </td>");

		for(SearchObject eb : cp.ebay.EbayObjects) {
			out.println("<tr align=\"center\"> <td> <img src=\"" + eb.getImgurl() + "\" /> </td> <td>" +
					eb.getName() + "</td> <td>" + eb.getPrice() + "</td>");
		}

		out.println("</tr> <tr><td colspan=\"2\">Name</td> <td>Price </td> </tr>");
		for(SearchObject jb : cp.jabong.JabongObjects) {
			out.println("<tr align=\"center\"> <td> <img src=\"" + jb.getImgurl() + "\" /> </td> <td>" +
					jb.getName() + "</td> <td>" + jb.getPrice() + "</td>");
		}
		out.println("</tr>");


		out.println("</table>");
		out.println("</p>");
		out.println("</body>");
		out.close();	
	}
}