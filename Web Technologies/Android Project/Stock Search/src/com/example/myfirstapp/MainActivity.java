package com.example.myfirstapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.myfirstapp.MainActivity.PlaceholderFragment;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;


public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TableLayout tabledisplay = (TableLayout) findViewById(R.id.stockTable);
		tabledisplay.setVisibility(View.INVISIBLE);
		RelativeLayout reldisplay=(RelativeLayout) findViewById(R.id.relativeLayout1);
		reldisplay.setVisibility(View.INVISIBLE);
		ImageView imgdisplay=(ImageView) findViewById(R.id.stockimage);
		imgdisplay.setVisibility(View.INVISIBLE);
		
		ImageView arrowdisplay=(ImageView) findViewById(R.id.arrowimg);
		arrowdisplay.setVisibility(View.INVISIBLE);
		Button fb=(Button) findViewById(R.id.button2);
		fb.setVisibility(Button.INVISIBLE);
		Button news=(Button) findViewById(R.id.button3);
		news.setVisibility(Button.INVISIBLE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	
	public void displayQuote(View view){
		
		EditText symbol=(EditText) findViewById(R.id.edit_message);
		String symbstr = symbol.getText().toString().replaceAll("\\s+", " ");
		
		if(symbstr.equals(""))
		{
			AlertDialog.Builder wronginput = new AlertDialog.Builder(this);
			wronginput.setTitle("Error");
			wronginput.setMessage("Enter company symbol");
			wronginput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
			wronginput.show();
		} else {
			String url="http://cs-server.usc.edu:25882/examples/servlet/stock_servlet1?symbol="+symbstr;
			new ServletAsync().execute(this,url);
		}
	}
	
	/** Called when the user clicks the Send button 
	*/
	public void newsdisplay(View view) {
		EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    System.out.println("In newsdisplay"+message);
		startActivity(new Intent("com.example.myfirstapp.NewsDisplayActivity"));
		com.example.myfirstapp.NewsDisplayActivity.symbol=message;
		    
	}
	
	private class ServletAsync extends AsyncTask<Object,Integer, String> {
		private MainActivity value;
		@Override
		protected String doInBackground(Object... param) {
			System.out.println("In doinback");
			String finalvalue = null;
			value = (MainActivity)param[0];
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
				String name =  result.getString("Name");
				String symb= result.getString("Symbol");
				System.out.println(name);
			
				JSONObject quote= result.getJSONObject("Quote");
				String changetype=quote.getString("ChangeType");
				String change=quote.getString("Change");
				String changeinpercent=quote.getString("ChangeInPercent");
				String ltpo=quote.getString("LastTradePriceOnly");
				String prevclose=quote.getString("PreviousClose");
				String open=quote.getString("Open");
				String bid=quote.getString("Bid");
				String ask=quote.getString("Ask");
				String oytp=quote.getString("OneYearTargetPrice");
				String dayslow=quote.getString("DaysLow");
				String dayshigh=quote.getString("DaysHigh");
				String yrlow=quote.getString("YearLow");
				String yrhigh=quote.getString("YearHigh");
				String volume=quote.getString("Volume");
				String avgvol=quote.getString("AverageDailyVolume");
				String marketcap=quote.getString("MarketCapitalization");
				String stockimgurl=result.getString("StockChartImageURL");
				String up="http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif";
				String down="http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif";
				
				if(!change.equals("0.00"))
				{
					//downloading image
					new DwImageThread().execute(stockimgurl);
				
					//display the views
					TableLayout tabledisplay = (TableLayout) findViewById(R.id.stockTable);
					tabledisplay.setVisibility(View.VISIBLE);
					RelativeLayout reldisplay=(RelativeLayout) findViewById(R.id.relativeLayout1);
					reldisplay.setVisibility(View.VISIBLE);
					Button fb=(Button) findViewById(R.id.button2);
					fb.setVisibility(Button.VISIBLE);
					Button news=(Button) findViewById(R.id.button3);
					news.setVisibility(Button.VISIBLE);
				
				
					//display value in UI tablelayout
					TextView nameText = (TextView) findViewById(R.id.textView1);
					nameText.setText(name+" ("+symb+")");
					TextView symbText = (TextView) findViewById(R.id.textView2);
					symbText.setText(ltpo);
					TextView changeval = (TextView) findViewById(R.id.textView3);
					changeval.setText(change+" ("+changeinpercent+"%)");
					if(changetype.equals("-"))
					{
						//downloading down arrow image
						new DwArrowThread().execute(down);
						changeval.setTextColor(Color.RED);
				} else if(changetype.equals("+"))
				{
					//downloading up arrow image
					new DwArrowThread().execute(up);
					changeval.setTextColor(Color.GREEN);
				}
				TextView prevclose1=(TextView) findViewById(R.id.textView5);
				prevclose1.setText(prevclose);
				TextView open1=(TextView) findViewById(R.id.textView7);
				open1.setText(open);
				TextView bid1=(TextView) findViewById(R.id.textView9);
				bid1.setText(bid);
				TextView ask1=(TextView) findViewById(R.id.textView11);
				ask1.setText(ask);
				TextView oytp1=(TextView) findViewById(R.id.textView13);
				oytp1.setText(oytp);
				TextView dayrange1=(TextView) findViewById(R.id.textView15);
				dayrange1.setText(dayslow+"-"+dayshigh);
				TextView yrrange=(TextView) findViewById(R.id.textView17);
				yrrange.setText(yrlow+"-"+yrhigh);
				TextView volume1=(TextView) findViewById(R.id.textView19);
				volume1.setText(volume);
				TextView avgvol1=(TextView) findViewById(R.id.textView21);
				avgvol1.setText(avgvol);
				TextView marketcap1=(TextView) findViewById(R.id.textView23);
				marketcap1.setText(marketcap);
			} else if(change.equals("0.00")){
				//Hiding all the views
				TableLayout tabledisplay1 = (TableLayout) findViewById(R.id.stockTable);
				tabledisplay1.setVisibility(View.INVISIBLE);
				RelativeLayout reldisplay1=(RelativeLayout) findViewById(R.id.relativeLayout1);
				reldisplay1.setVisibility(View.INVISIBLE);
				ImageView imgdisplay1=(ImageView) findViewById(R.id.stockimage);
				imgdisplay1.setVisibility(View.INVISIBLE);
					
				ImageView arrowdisplay1=(ImageView) findViewById(R.id.arrowimg);
				arrowdisplay1.setVisibility(View.INVISIBLE);
				Button fb1=(Button) findViewById(R.id.button2);
				fb1.setVisibility(Button.INVISIBLE);
				Button news1=(Button) findViewById(R.id.button3);
				news1.setVisibility(Button.INVISIBLE);
					
				TextView changeval1 = (TextView) findViewById(R.id.textView3);
				changeval1.setText("Stock information not found!");
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			System.out.println("On post exe");
		}
	}
	
	private class DwArrowThread extends AsyncTask<String,Integer,Bitmap>{
		protected Bitmap doInBackground(String...param){
			Bitmap arrowimg;
			try {
				URL arrowurl=new URL(param[0]);	
				System.out.println(arrowurl);
				try {
					arrowimg = BitmapFactory.decodeStream(arrowurl.openConnection().getInputStream());
					return arrowimg;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Bitmap arrowimg){
			//display ImageView
			ImageView arrowdisplay=(ImageView) findViewById(R.id.arrowimg);
			arrowdisplay.setVisibility(View.VISIBLE);
			
			ImageView imgarrow1=(ImageView) findViewById(R.id.arrowimg);
			imgarrow1.setImageBitmap(arrowimg);
		}
	}
	
	private class DwImageThread extends AsyncTask<String,Integer,Bitmap>{
	
		protected Bitmap doInBackground(String...param){
			Bitmap imgdata;
			try {
				URL imgurl=new URL(param[0]);	
				try {
					imgdata = BitmapFactory.decodeStream(imgurl.openConnection().getInputStream());
			
					return imgdata;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Bitmap imgdata){
			//display ImageView
			ImageView imgdisplay=(ImageView) findViewById(R.id.stockimage);
			imgdisplay.setVisibility(View.VISIBLE);
			
			ImageView stockimg=(ImageView) findViewById(R.id.stockimage);
			stockimg.setImageBitmap(imgdata);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
