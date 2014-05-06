package com.example.comp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchItemExchange extends Activity {

	String thisUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item_exchange);
		thisUser = getIntent().getExtras().getString("user");
		
		String category = getIntent().getExtras().getString("CATEGORY");
		
		String order = "Increasing";
		String field = "price";
		//Toast.makeText(this, category, Toast.LENGTH_LONG).show();
		
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-item-exchange.php", category, order, field, thisUser);
	}
	
	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1],urls[2],urls[3],urls[4]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			//Toast.makeText(getBaseContext(), "Data Sent!" + " " +  result, Toast.LENGTH_LONG).show();
			
			try {
				int index = result.indexOf("bin/php");	
				result = result.substring(index+7);
				JSONObject jObj = new JSONObject(result);
					
				if (jObj.optString("result").equals("0")){
					Toast.makeText(getBaseContext(), "No item available", Toast.LENGTH_SHORT).show();
				} else {
					
					JSONArray jArray = jObj.getJSONArray("list");
					for(int i=0; i < jArray.length(); i++){
						JSONObject obj = jArray.getJSONObject(i);
	
						LinearLayout container = (LinearLayout)findViewById(R.id.container);
					    Button rowButton = new Button(getBaseContext());
					    
					    final String title = obj.optString("title");
					    final String price = obj.optString("price");
					    final String quality = obj.optString("quality");
					    final String descr = obj.optString("descr");
					    final String id = obj.optString("id");
					    //final String id = obj.optString("id");
					    final String duedate = obj.optString("duedate");
						
					    //Toast.makeText(getBaseContext(), title + " " + price + " " + id + " " + duedate, Toast.LENGTH_LONG).show();
					    
					    //set the content of the button
					    String content =  title + "\n" + price;
					    rowButton.setText(content);
					    
					    //rowButton.setOnClickListener((OnClickListener) this);
					    
					    rowButton.setOnClickListener(new OnClickListener() {
					        @Override
					    	public void onClick(View v) {
					        	Bundle bundle = new Bundle();
					        	bundle.putString("ID", id);
					        	bundle.putString("TITLE", title);
					        	bundle.putString("PRICE", price);
					        	bundle.putString("QUALITY", quality);
					        	bundle.putString("DESCR", descr);
					        	bundle.putString("DUEDATE", duedate);
					            Intent intent = new Intent(SearchItemExchange.this, ShowProductExchange.class);
								intent.putExtras(bundle);
								startActivity(intent);
					        }
					    });
					    container.addView(rowButton);
					}
				}
			}
			catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), result + "Error" + e.toString(),
							Toast.LENGTH_SHORT).show();
			}

		}
	}

	public static String POST(String url, String category, String order, String field, String user){
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("category", category));
			params.add(new BasicNameValuePair("ordering", order));
			params.add(new BasicNameValuePair("sortField",field));
			params.add(new BasicNameValuePair("user", user));
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;

	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.search_item_exchange, menu);
	    return true;
	}
	
	/** Called when the user clicks the Action Bar - Menu button */
	public void goToMyProfile(MenuItem item){
		Intent intent = new Intent(this, MyProfile.class);
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Action Bar - Menu button */
	public void goToMenu(MenuItem item){
		Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}
	
}
