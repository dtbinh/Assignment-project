

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.*;

/**
 * Servlet implementation class stock_servlet
 */
public class stock_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public stock_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("sdss");
		String symbol= request.getParameter("symbol");
		System.out.println(symbol);
		response.setContentType("application/json");
		//changed
		String encodesymb = URLEncoder.encode(symbol,"UTF-8");//symbol; 
		//encodesymb = encodesymb.replaceAll("'","/'");
		String url_php = "http://default-environment-qzy28j5khm.elasticbeanstalk.com/?symbol="+encodesymb;
		URL url= new URL(url_php);
		URLConnection connection= url.openConnection();
		connection.setAllowUserInteraction(false);
		
	 
	    BufferedReader stdin = new BufferedReader(new InputStreamReader(url.openStream())); 
		//InputStreamReader isr= new InputStreamReader(url.openStream());
		//BufferedReader stdin= new BufferedReader(isr);
		String input= new String();
		String xml_content="";
		while((input=stdin.readLine()) !=null)
		{
			xml_content += input+"\n";
		}
	
		System.out.println(xml_content);
		// Converting XML data to JSON
		 
		
			JSONObject xmlJSONObj;
			try {
				xmlJSONObj = XML.toJSONObject(xml_content);
				PrintWriter writer = response.getWriter(); 
			    writer.println(xmlJSONObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
					
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
