package com.example.comp;

import java.io.BufferedReader;
import java.io.File;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SellOptionSubmit extends Activity {

	static String thisUser;

	boolean auction;
	String title, category, quality, description, price, date, imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_option_submit);
		auction = false;
		//Bundle bundle = getIntent().getExtras();
		//Extract each value from the bundle for usage
		title = getIntent().getExtras().getString("TITLE");
		if(getIntent().hasExtra("auction"))
			auction = getIntent().getExtras().getBoolean("auction");
		category = getIntent().getExtras().getString("CATEGORY");
		date = getIntent().getExtras().getString("DATE");
		quality = getIntent().getExtras().getString("QUALITY");
		description = getIntent().getExtras().getString("DESCRIPTION");
		price = Double.toString(getIntent().getExtras().getDouble("PRICE"));
		imagePath = getIntent().getExtras().getString("IMAGE");
		thisUser = getIntent().getExtras().getString("user");

		System.out.println("this user value is " + thisUser);

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

		ImageView image = (ImageView) findViewById(R.id.confirmImage);
		image.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 300, 300));
	}

	/**
	 * Called when the user clicks the confirm button
	 * Go back to MainActivity (Menu) page
	 * @param view
	 */
	public void backToHome(View view){
		Intent intent = new Intent(this, MenuActivity.class);

		intent.putExtra("user", thisUser);
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/add-item-to-database-image.php", 
				title, category,price, quality, description, thisUser, String.valueOf(auction), date, imagePath);
		startActivity(intent);
	}

	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2], urls[3], urls[4], urls[5], urls[6], urls[7], urls[8], urls[9]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Data Sent!" + " " +  result, Toast.LENGTH_LONG).show();
			System.out.print(result);
		}
	}


	public static String POST(String url, String title, String category, String price, String quality, String desc, String user, String isAuction, String eTime, String imagePath)
	{
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			
			String json = "";

			// Request parameters and other properties. using a MultipartEntity
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = new FileBody(new File(imagePath));
			builder.addPart("image", fileBody);
			builder.addTextBody("title", title);
			builder.addTextBody("category", category);
			builder.addTextBody("price", price.toString());
			builder.addTextBody("quality", quality.toString());
			builder.addTextBody("description", desc.toString());
			builder.addTextBody("user", thisUser);
			builder.addTextBody("isAuction", isAuction);
			builder.addTextBody("edate", eTime);

			httpPost.setEntity(builder.build());

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
	    inflater.inflate(R.menu.sell_option_submit, menu);
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
