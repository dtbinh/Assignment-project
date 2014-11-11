import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;





public class Connection {
	
	
	
	static java.sql.Connection conn=null;
	static Statement stmt=null;
	ResultSet mainResultSet=null;
	
		public static final int DB_CONNECT_ERROR = -100;
	    public static final int DB_INSERT_ERROR = -101;
	    public static final int INVALID_TYPE_ERROR = -102;
	    public static final int DB_DELETE_ERROR = -103;
	    public static final int DB_CLOSE_ERROR = -104;
	    public static final int DB_QUERY_EXECUTE_ERROR = -105;
	    public static final int RESULT_SET_ERROR = -106;
	    
	public void connect() {
	
		
		
		try {
			//Open a connection
			System.out.println("Connecting to database...");
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:omkar","SYSTEM","Suryavanshi_91");
           
            
            //Execute a query
			System.out.println("Creating statement...");
			 stmt=conn.createStatement();
            
		}
		catch(Exception e) {
			System.out.println("ERROR: "+DB_CONNECT_ERROR+". Exception: "+e.toString());
			System.exit(DB_CONNECT_ERROR);
		}
	}
	
	public void close() {
	
		try {
			conn.close();
		}
		catch (Exception e){
			System.out.println("ERROR: "+DB_CLOSE_ERROR+". Exception: "+e.toString());
			System.exit(DB_CLOSE_ERROR);
		}
		System.out.println("Connection closed.");
	}
	
	public ResultSet getResultSet(String query) {
        try {
            mainResultSet = stmt.executeQuery(query);
        }
        catch (Exception e) {
            System.out.println("ERROR: "+DB_QUERY_EXECUTE_ERROR+". Exception: "+e.toString());
            System.exit(DB_QUERY_EXECUTE_ERROR);
        }
        return mainResultSet;
    }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		 try {

			 File bookfile = new File("F:/Ebooks/Spring 2014/585/HW3/book.xml");
			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			 Document document = dBuilder.parse(bookfile);
			 
			 document.getDocumentElement().normalize();

			 System.out.println("Root element :" + document.getDocumentElement().getNodeName());

			 NodeList nodelist = document.getElementsByTagName("book");
			 	
			 int n=nodelist.getLength();
			 String id[]= new String[n];
			 String title[]= new String[n];
			 String author[][]= new String[n][2];
			 String price[]= new String[n];
			 String isbn[]= new String[n];
			 String pdate[]= new String[n];
			 System.out.println("----------------------------");
			
			 for (int i = 0; i < nodelist.getLength(); i++) {

			 Node node = nodelist.item(i);
			 
			 
			 System.out.println("\nCurrent Element :" + node.getNodeName());

			 if (node.getNodeType() == Node.ELEMENT_NODE) {

			 Element e = (Element) node;

			 
			 id[i]=e.getElementsByTagName("ID").item(0).getTextContent();
			 System.out.println("Id : " +id[i]);
			 
			 
			 title[i]=e.getElementsByTagName("TITLE").item(0).getTextContent();
			 System.out.println("Title : " +title[i]);
			 
			 
			 author[i][0]=e.getElementsByTagName("AUTHOR").item(0).getTextContent();
			 System.out.println("Author : " +author[i][0]);
			 
			 if(e.getElementsByTagName("AUTHOR").item(1)!=null)
			 {	
				 //if 2 authors
				 author[i][1]=e.getElementsByTagName("AUTHOR").item(1).getTextContent();
				 System.out.println("Author : " +author[i][1]);
			 }
			 else author[i][1]="";
			 
			 
			 price[i]=e.getElementsByTagName("PRICE").item(0).getTextContent();
			 System.out.println("Price : " +price[i]);
			 
			 
			 isbn[i]=e.getElementsByTagName("ISBN").item(0).getTextContent();
			 System.out.println("ISBN : " +isbn[i]);
			 
			 
			 pdate[i]=e.getElementsByTagName("PUBLISH_DATE").item(0).getTextContent();
			 System.out.println("Publish_Date : " +pdate[i]);
			 
			
			 if(e.getElementsByTagName("AUTHOR").item(1)==null)
			 {
				 Connection c = new Connection();
					c.connect();
					String s_query;
					
					s_query="Insert into Books values (XMLTYPE('<book><ID>"+id[i]+"</ID><TITLE>"+title[i]+"</TITLE><AUTHOR>"+author[i][0]+"</AUTHOR><PRICE>"+price[i]+"</PRICE><ISBN>"+isbn[i]+"</ISBN><PUBLISH_DATE>"+pdate[i]+"</PUBLISH_DATE></book>'))";
					ResultSet rs= c.getResultSet(s_query);
				
			 }
			 else if(e.getElementsByTagName("AUTHOR").item(1)!=null)
			 {
				 Connection c = new Connection();
					c.connect();
					String s_query;
					
					s_query="Insert into Books values (XMLTYPE('<book><ID>"+id[i]+"</ID><TITLE>"+title[i]+"</TITLE><AUTHOR>"+author[i][0]+"</AUTHOR><AUTHOR>"+author[i][1]+"</AUTHOR><PRICE>"+price[i]+"</PRICE><ISBN>"+isbn[i]+"</ISBN><PUBLISH_DATE>"+pdate[i]+"</PUBLISH_DATE></book>'))";
					ResultSet rs= c.getResultSet(s_query);
				 
			 }
			
			 }
			 }
			     } catch (Exception e) {
			 e.printStackTrace();
	}
	
		 
		 //Reviews
		 try {

			 File bookfile = new File("F:/Ebooks/Spring 2014/585/HW3/review.xml");
			 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			 Document document = dBuilder.parse(bookfile);
			 
			 document.getDocumentElement().normalize();

			 System.out.println("Root element :" + document.getDocumentElement().getNodeName());

			 NodeList nodelist = document.getElementsByTagName("review");
			 	
			 int n=nodelist.getLength();
			 String r_id[]= new String[n];
			 String b_title[]= new String[n];
			 String rating[]= new String[n];
			 String reviewer[]= new String[n];
			 String desc[]= new String[n];
			 String rdate[]= new String[n];
			 System.out.println("----------------------------");
			
			 for (int i = 0; i < nodelist.getLength(); i++) {

			 Node node = nodelist.item(i);
			 
			 
			 System.out.println("\nCurrent Element :" + node.getNodeName());

			 if (node.getNodeType() == Node.ELEMENT_NODE) {

			 Element e = (Element) node;

			 
			 r_id[i]=e.getElementsByTagName("REVIEW_ID").item(0).getTextContent();
			 System.out.println("Id : " +r_id[i]);
			 
			 
			 b_title[i]=e.getElementsByTagName("BOOK_TITLE").item(0).getTextContent();
			 System.out.println("Book Title : " +b_title[i]);
			 
			 
			 rating[i]=e.getElementsByTagName("RATING").item(0).getTextContent();
			 System.out.println("Rating : " +rating[i]);
			 
			 
			 
			 reviewer[i]=e.getElementsByTagName("REVIEWER").item(0).getTextContent();
			 System.out.println("Reviewer : " +reviewer[i]);
			 
			 
			 desc[i]=e.getElementsByTagName("REVIEW_DESC").item(0).getTextContent();
			 System.out.println("Desc : " +desc[i]);
			 
			 
			 rdate[i]=e.getElementsByTagName("REVIEW_DATE").item(0).getTextContent();
			 System.out.println("Review_Date : " +rdate[i]);
			 
			
			 
				 Connection c = new Connection();
					c.connect();
					String s_query;
					
					s_query="Insert into Reviews values (XMLTYPE('<review><REVIEW_ID>"+r_id[i]+"</REVIEW_ID><BOOK_TITLE>"+b_title[i]+"</BOOK_TITLE><RATING>"+rating[i]+"</RATING><REVIEWER>"+reviewer[i]+"</REVIEWER><REVIEW_DESC>"+desc[i]+"</REVIEW_DESC><REVIEW_DATE>"+rdate[i]+"</REVIEW_DATE></review>'))";
					ResultSet rs= c.getResultSet(s_query);
				
			 
			 
			
			 }
			 }
			     } catch (Exception e) {
			 e.printStackTrace();
	}
		 
		 
		 
		
			

}
}
