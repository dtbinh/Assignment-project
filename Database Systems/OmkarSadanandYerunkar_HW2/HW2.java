//import hw2.Conn;

import java.io.File;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


public class HW2 {

	/**
	 * @param args
	 */
		
	
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
	
	

	public static void evaluateWindow(String args0, String args1, String args2, String args3, String args4, String args5){
	HW2 c = new HW2();
	c.connect();
	String object_type;
	String s_query;
	int x1,x2,y1,y2;
	if(args1.equals("firebuilding"))
	{
		object_type="Firebuilding";
		x1=Integer.parseInt(args2);
		y1=Integer.parseInt(args3);
		x2=Integer.parseInt(args4);
		y2=Integer.parseInt(args5);
		s_query="Select distinct fb.f_building_id from firebuilding fb where sdo_filter(fb.fb_co_ordinates,mdsys.sdo_geometry(2003,NULL,NULL,mdsys.sdo_elem_info_array(1,1003,3),mdsys.sdo_ordinate_array("+x1+","+y1+","+x2+","+y2+")),'querytype=window')='TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("f_building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	else if(args1.equals("building"))
	{
		object_type="Building";
		
		x1=Integer.parseInt(args2);
		y1=Integer.parseInt(args3);
		x2=Integer.parseInt(args4);
		y2=Integer.parseInt(args5);
		s_query="Select distinct b.building_id from building b where b.b_name not in(Select fb.fb_name from firebuilding fb) and sdo_filter(b.co_ordinates,mdsys.sdo_geometry(2003,NULL,NULL,mdsys.sdo_elem_info_array(1,1003,3),mdsys.sdo_ordinate_array("+x1+","+y1+","+x2+","+y2+")),'querytype=window')='TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	else if(args1.equals("firehydrant"))
	{
		object_type="Firehydrant";
		x1=Integer.parseInt(args2);
		y1=Integer.parseInt(args3);
		x2=Integer.parseInt(args4);
		y2=Integer.parseInt(args5);
		s_query="Select distinct h.hydrant_id from hydrant h where sdo_filter(h.h_co_ordinates,mdsys.sdo_geometry(2003,NULL,NULL,mdsys.sdo_elem_info_array(1,1003,3),mdsys.sdo_ordinate_array("+x1+","+y1+","+x2+","+y2+")),'querytype=window')='TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String h_id=rs.getString("hydrant_id");
				System.out.println(h_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

	public static void evaluateWithin(String args0,String args1,String args2,String args3){
	HW2 c = new HW2();
	c.connect();
	String s_query;
	String object_type;
	
	String bldg;
	int dist;
	if(args1.equals("firebuilding"))
	{
		object_type="Firebuilding";
		bldg=args2;
		dist=Integer.parseInt(args3);
		
		//System.out.println(bldg+"-"+dist);
		
		
		s_query="Select distinct fb.f_building_id from firebuilding fb,building b where b.b_name=' "+bldg+"' and SDO_WITHIN_DISTANCE(fb.fb_co_ordinates, b.co_ordinates, 'distance ="+dist+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("f_building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	else if(args1.equals("firehydrant"))
	{
		object_type="Firehydrant";
		bldg=args2;
		dist=Integer.parseInt(args3);
		
		//System.out.println(bldg+"-"+dist);
		s_query="Select distinct h.hydrant_id from hydrant h, building b where b.b_name=' "+bldg+"' and SDO_WITHIN_DISTANCE(h.h_co_ordinates, b.co_ordinates, 'distance ="+dist+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("hydrant_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	else if(args1.equals("building"))
	{
		object_type="Building";
		bldg=args2;
		dist=Integer.parseInt(args3);
		//System.out.println(bldg+"-"+dist);
		
		
		s_query="Select distinct b.building_id from building b,building b1,firebuilding fb where b.building_id !=b1.building_id and b.b_name not in(Select fb.fb_name from firebuilding fb) and b1.b_name=' "+bldg+"' and SDO_WITHIN_DISTANCE(b.co_ordinates, b1.co_ordinates, 'distance ="+dist+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

	public static void evaluateNN(String args0,String args1,String args2,String args3){
	
	HW2 c = new HW2();
	c.connect();
	String s_query;
	String object_type;
	String bldg_id;
	int n;
	if(args1.equals("firebuilding"))
	{
		object_type="Firebuilding";
		bldg_id=args2;
		n=Integer.parseInt(args3);
		
		//System.out.println(bldg_id+"-"+n);
		
		
		s_query="Select distinct fb.f_building_id from firebuilding fb,building b1 where b1.building_id='"+bldg_id+"' and  fb.f_building_id!=b1.building_id and SDO_NN(fb.fb_co_ordinates, b1.co_ordinates, 'SDO_NUM_RES="+n+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("f_building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	else if(args1.equals("building"))
	{
		object_type="Building";
		
		bldg_id=args2;
		//bldg=" "+bldg;
		n=Integer.parseInt(args3);
		
		//System.out.println(bldg_id+"-"+n);
		
		
		s_query="Select distinct b.building_id from building b,building b1 where b1.building_id='"+bldg_id+"' and b.b_name not in(Select fb.fb_name from firebuilding fb) and b.building_id!=b1.building_id and SDO_NN(b.co_ordinates, b1.co_ordinates, 'SDO_NUM_RES="+(n+1)+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("building_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if(args1.equals("firehydrant"))
	{
		object_type="Firehydrant";
		System.out.println(object_type);
		
		bldg_id=args2;
		//bldg=" "+bldg;
		n=Integer.parseInt(args3);
		
		//System.out.println(bldg_id+"-"+n);
		
		
		s_query="Select distinct h.hydrant_id from hydrant h,building b1 where b1.building_id='"+bldg_id+"' and SDO_NN(h.h_co_ordinates, b1.co_ordinates, 'SDO_NUM_RES="+n+"') = 'TRUE'";
		System.out.println(object_type);
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_id=rs.getString("hydrant_id");
				System.out.println(b_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

	public static void evaluateDemo(String args0,String args1){
	HW2 c = new HW2();
	c.connect();
	String s_query;
	String object_type;
	int n;
	n=Integer.parseInt(args1);
	
	if(n==1)
	{
		s_query="Select distinct b.b_name from building b where b.b_name like ' S%' and b.b_name not in (Select fb.fb_name from firebuilding fb)";
		
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				String b_name=rs.getString("b_name");
				System.out.println(b_name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	else if(n==2)
	{
		String loop;
		String fb_name[]= new String[3];
		String fb_id[]= new String[3];
		loop="Select fb_name,f_building_id from firebuilding";
		ResultSet looprs=c.getResultSet(loop);
		int i=0;
		try {
			while(looprs.next())
			{
				fb_name[i]=looprs.getString("fb_name");
				fb_id[i]=looprs.getString("f_building_id");
			
				//System.out.println(fb_name[i]+"----"+fb_id[i]);
				i++;
			}
			
			for(i=0;i<3;i++)
			{
				s_query="Select distinct h.hydrant_id from hydrant h,firebuilding fb where fb.f_building_id='"+fb_id[i]+"' and SDO_NN(h.h_co_ordinates, fb.fb_co_ordinates, 'SDO_NUM_RES=5') = 'TRUE'";
			
				ResultSet rs= c.getResultSet(s_query);
				try {
					while(rs.next()){
						
						String h_id=rs.getString("hydrant_id");
						System.out.println(fb_name[i]+"  "+h_id);
						
					}
				} catch (SQLException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	else if(n==3)
	{
		
		s_query="Select a.hydrant_id,a.No_of_Buildings from (Select distinct h.hydrant_id,Count(b.building_id) as No_of_Buildings from hydrant h, building b where SDO_WITHIN_DISTANCE(b.co_ordinates,h.h_co_ordinates, 'distance =120') = 'TRUE' Group by h.hydrant_id ORDER By Count(b.building_id) DESC ) a  where rownum<=1";
		
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next()){
				//String h_id="A";
				String h_id=rs.getString("hydrant_id");
				String count=rs.getString("No_of_Buildings");
				System.out.println("Hydrant_id  No_of_Buildings ");
				System.out.println(h_id+"         "+count);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	else if(n==4)
	{
		
		//s_query="Select distinct h.hydrant_id,b.building_id from building b,hydrant h where SDO_NN(h.h_co_ordinates,b.co_ordinates, 'SDO_NUM_RES=1') = 'TRUE'";
		s_query="Select a.hydrant_id,a.Count from (Select distinct h.hydrant_id,Count(b.building_id) as Count from building b,hydrant h where  SDO_NN(h.h_co_ordinates,b.co_ordinates, 'SDO_NUM_RES=1') = 'TRUE' group by h.hydrant_id ORDER BY Count(b.building_id) DESC) a where rownum<=5";
		ResultSet rs= c.getResultSet(s_query);
		System.out.println("Hydrant_id  Count ");
		try {
			while(rs.next()){
				
				String h_id=rs.getString("hydrant_id");
				String count=rs.getString("Count");
				
				System.out.println(h_id+"         "+count);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	else if(n==5)
	{
		
		s_query="Select SDO_AGGR_MBR(b.co_ordinates) from building b where b.b_name like '%HE'";
		ResultSet rs= c.getResultSet(s_query);
		try {
			while(rs.next())
			try {
				STRUCT st = (STRUCT) rs.getObject(1);
				JGeometry j_geom = JGeometry.load(st);
				double mbr[]=j_geom.getMBR();
				System.out.println("x1="+mbr[0]+" y1="+mbr[1]+" x2="+mbr[2]+" y2="+mbr[3]);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	}

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub

		
		//System.out.println("Code is working in main...args0"+arg[0]+"args1"+arg[1]+"args2"+arg[2]+"args3"+arg[3]+"args4"+arg[4]+"args5"+arg[5]);
		/*
		String args[]= new String[6];
		args[0]=arg[0];
		System.out.println("Code: args[0]"+args[0]);
		args[1]=arg[1];
		args[2]=arg[2];
		args[3]=arg[3];
		args[4]=arg[4];
		args[5]=arg[5];
		*/
		if(args[0].equals("window"))
		{
			System.out.println("Code is working in window");
			evaluateWindow(args[0],args[1],args[2],args[3],args[4],args[5]);
		}
				
		else if(args[0].equals("within"))
		{
			evaluateWithin(args[0],args[1],args[2],args[3]);
		}
					
		else if(args[0].equals("nn"))
		{
			evaluateNN(args[0],args[1],args[2],args[3]);
		}
		
		else if(args[0].equals("demo"))
		{
			evaluateDemo(args[0],args[1]);
		}
		
		
	}

}
