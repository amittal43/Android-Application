package com.example.comp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ShowProductExchange extends Activity {
	String title, price, quality, descr, duedate;
	int id;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_product_exchange);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		Bundle bundle = getIntent().getExtras();
	    //Extract each value from the bundle for usage
		id = bundle.getInt("ID");
		title = bundle.getString("TITLE");
		price = bundle.getString("PRICE");
		quality = bundle.getString("QUALITY");
		descr = bundle.getString("DESCR");
		duedate = bundle.getString("DUEDATE");
		
	    TextView textTitle = (TextView) findViewById(R.id.showproductTitle);
	    textTitle.append(title);
	    
	    TextView textDescription = (TextView) findViewById(R.id.showproductDescription);
	    textDescription.append(descr);
	    
	    TextView textQuality = (TextView) findViewById(R.id.showproductQuality);
	    textQuality.append(quality);
	    
	    TextView textPrice = (TextView) findViewById(R.id.showproductPrice);
	    textPrice.append(price);
	    
	    TextView textDueDate = (TextView) findViewById(R.id.showproductDueDate);
	    textDueDate.append(duedate);
		
	}
	
	public void borrow (View view){
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/delete-item-id.php", Integer.toString(id), "borrow");
		/*Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Toast toast = Toast.makeText(this, "You have successfully bought the item!",Toast.LENGTH_LONG);
		toast.show();
		Listing.bookListing.remove(index);*/
		
	}
	
	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Intent intent = new Intent(getBaseContext(), MenuActivity.class);
			startActivity(intent);
			Toast.makeText(getBaseContext(), "Data Sent!" + " " +  result, Toast.LENGTH_LONG).show();
		}
			
	}

	public static String POST(String url, String id, String menu){
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("id", id));
			params.add(new BasicNameValuePair("menu", menu));
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
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_product, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_show_product,
					container, false);
			return rootView;
		}
	}*/

}
