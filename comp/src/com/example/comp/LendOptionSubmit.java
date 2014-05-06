package com.example.comp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LendOptionSubmit extends Activity {

	String title, category, quality, description, price, duedate, thisUser;
	//Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lend_option_submit);

		thisUser = getIntent().getExtras().getString("user");
		//Toast.makeText(this, thisUser, Toast.LENGTH_LONG).show();
		
		Bundle bundle = getIntent().getExtras();
		//Extract each value from the bundle for usage
		title = bundle.getString("TITLE");
		category = bundle.getString("CATEGORY");
		quality = bundle.getString("QUALITY");
		description = bundle.getString("DESCRIPTION");
		price = Double.toString(bundle.getDouble("PRICE"));
		duedate = bundle.getString("DUEDATE");
		
		//Toast.makeText(this, thisUser + " " + title + " " + category + " " + quality + " " + description + " " + price + " " +duedate, Toast.LENGTH_LONG).show();
		/*cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
	    try {
			cal.setTime(sdf.parse(duedate));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		TextView textTitle = (TextView) findViewById(R.id.confirmTitle);
		textTitle.append(title);

		TextView textCategory = (TextView) findViewById(R.id.confirmCategory);
		textCategory.append(category);

		TextView textQuality = (TextView) findViewById(R.id.confirmQuality);
		textQuality.append(quality);

		TextView textDescription = (TextView) findViewById(R.id.confirmDescription);
		textDescription.append(description);

		TextView textPrice = (TextView) findViewById(R.id.confirmPrice);
		textPrice.append(price);
		
		TextView textDueDate = (TextView) findViewById(R.id.confirmDueDate);
		textDueDate.append(duedate);
	}

	/**
	 * Called when the user clicks the confirm button
	 * Go back to MainActivity (Menu) page
	 * @param view
	 */
	public void backToHome(View view){
		Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra("user", thisUser);
		//Toast.makeText(this, thisUser, Toast.LENGTH_LONG).show();
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/add-item-exchange-to-database.php", 
				title, category,price, quality, description, duedate, thisUser);
		startActivity(intent);
	}
	
	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2], urls[3], urls[4], urls[5], urls[6], urls[7]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Data Sent!" + " " +  result, Toast.LENGTH_LONG).show();
		}
	}


	public static String POST(String url, String title, String category, String price, String quality, String desc, String duedate, String user){
		
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			String json = "";

			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("category", category));
			params.add(new BasicNameValuePair("price", price.toString()));
			params.add(new BasicNameValuePair("quality", quality.toString()));
			params.add(new BasicNameValuePair("description", desc.toString()));
			params.add(new BasicNameValuePair("duedate",duedate));
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
	    inflater.inflate(R.menu.lend_option_submit, menu);
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
