package com.example.myfirstapp;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class NewsDisplayActivity extends ActionBarActivity{

	public static String symbol;
	public String title[];
	public String link[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_display);
		System.out.println("In NewsDisplayActivity "+symbol);
		newsDisplay(symbol);
	
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void newsDisplay(String symbol){
		
		String url="http://cs-server.usc.edu:25882/examples/servlet/stock_servlet1?symbol="+symbol;
		new NewsDisplayAsync().execute(this,url);
	}
	
	private class NewsDisplayAsync extends AsyncTask<Object,Integer, String> {
		
		private NewsDisplayActivity value;
		@Override
		protected String doInBackground(Object... param) {
			
			System.out.println("In doinback");
			String finalvalue = null;
		
				value = (NewsDisplayActivity)param[0];

					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet((String)param[1]);
					HttpResponse responseGet = null;
					
						try {
							responseGet = client.execute(get);
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						HttpEntity entity = responseGet.getEntity();
						
						try {
							 finalvalue = EntityUtils.toString(entity);
						} catch (org.apache.http.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
			System.out.println("In doinback");
			System.out.println(finalvalue);
			return finalvalue;
	}
		
		@Override
		protected void onPostExecute(String finalvalue) {
			
			try {
				System.out.println("On post exe");
				
				//Get JSON data in string varables
				JSONObject results = new JSONObject(finalvalue);
				JSONObject result = results.getJSONObject("result");
				System.out.println(result);
				JSONObject news= result.getJSONObject("News");
				JSONArray item=news.getJSONArray("Item");
				int n=item.length();
				System.out.println(n);
				
				title= new String[n];
				link=new String[n];
				
				for(int i=0;i<n;i++)
				{
					JSONObject itemresults=item.getJSONObject(i);
					title[i]= itemresults.getString("Title");
					link[i]=itemresults.getString("Link");
					System.out.println(title[i]+" "+link[i]);
					
				}
			    if(n<20)
			    {
			    	for(int i=n;i<20;i++) 	
			    	{
			    		title[i]="";
			    		link[i]="";
			    	}
			    }
				
			    TextView t[]= new TextView[20];
			    //Diplay news
			    t[0]=(TextView) findViewById(R.id.textView1);
				t[0].setText(title[0]);
				t[1]=(TextView) findViewById(R.id.textView2);
				t[1].setText(title[1]);
				t[2]=(TextView) findViewById(R.id.textView3);
				t[2].setText(title[2]);
				t[3]=(TextView) findViewById(R.id.textView4);
				t[3].setText(title[3]);
				t[4]=(TextView) findViewById(R.id.textView5);
				t[4].setText(title[4]);
				t[5]=(TextView) findViewById(R.id.textView6);
				t[5].setText(title[5]);
				t[6]=(TextView) findViewById(R.id.textView7);
				t[6].setText(title[6]);
				t[7]=(TextView) findViewById(R.id.textView8);
				t[7].setText(title[7]);
				t[8]=(TextView) findViewById(R.id.textView9);
				t[8].setText(title[8]);
				t[9]=(TextView) findViewById(R.id.textView10);
				t[9].setText(title[9]);
				t[10]=(TextView) findViewById(R.id.textView11);
				t[10].setText(title[10]);
				t[11]=(TextView) findViewById(R.id.textView12);
				t[11].setText(title[11]);
				t[12]=(TextView) findViewById(R.id.textView13);
				t[12].setText(title[12]);
				t[13]=(TextView) findViewById(R.id.textView14);
				t[13].setText(title[13]);
				t[14]=(TextView) findViewById(R.id.textView15);
				t[14].setText(title[14]);
				t[15]=(TextView) findViewById(R.id.textView16);
				t[15].setText(title[15]);
				t[16]=(TextView) findViewById(R.id.textView17);
				t[16].setText(title[16]);
				t[17]=(TextView) findViewById(R.id.textView18);
				t[17].setText(title[17]);
				t[18]=(TextView) findViewById(R.id.textView19);
				t[18].setText(title[18]);
				t[19]=(TextView) findViewById(R.id.textView20);
				t[19].setText(title[19]);
				int[] id={R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4,R.id.textView5,R.id.textView6,R.id.textView7,R.id.textView8,R.id.textView9,R.id.textView10,R.id.textView11,R.id.textView12,R.id.textView13,R.id.textView14,R.id.textView15,R.id.textView16,R.id.textView17,R.id.textView18,R.id.textView19};
				
				for(int i=0;i<n;i++)
				{
					//t[i].setId(id[i]);
					//t[i].setOnClickListener((android.view.View.OnClickListener) onClick);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news_display,
					container, false);
			return rootView;
		}
	}

}
