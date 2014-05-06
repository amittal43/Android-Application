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
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ShowProduct extends Activity {
	String title, price, quality, descr, thisUser;
	String menu;
	String id;
	String seller;
	boolean isAuction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_product);
		thisUser = getIntent().getExtras().getString("user");
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		Bundle bundle = getIntent().getExtras();
		//Extract each value from the bundle for usage
		id = bundle.getString("ID");
		title = bundle.getString("TITLE");
		price = bundle.getString("PRICE");
		quality = bundle.getString("QUALITY");
		descr = bundle.getString("DESCR");
		seller = bundle.getString("SELLER");
		isAuction = bundle.getBoolean("ISAUCTION");
		System.out.println("Seller is: " + seller + isAuction);
		menu = getIntent().getExtras().getString("menu");
		
		TextView textTitle = (TextView) findViewById(R.id.showproductTitle);
		textTitle.append(title);

		TextView textDescription = (TextView) findViewById(R.id.showproductDescription);
		textDescription.append(descr);

		TextView textQuality = (TextView) findViewById(R.id.showproductQuality);
		textQuality.append(quality);

		TextView textPrice = (TextView) findViewById(R.id.showproductPrice);
		textPrice.append(price);

		TextView textSeller = (TextView) findViewById(R.id.showSeller);
		textSeller.append(seller);

		if(isAuction)
		{
			Button btn = (Button) findViewById(R.id.buyButton);
			btn.setText("Bid");
			TextView yourBid = (TextView) findViewById(R.id.yourBid);
			yourBid.setVisibility(View.VISIBLE);
			EditText newBid = (EditText) findViewById(R.id.newBid);
			newBid.setVisibility(View.VISIBLE);
		}
		else
		{
		
		}
	}

	public void buy (View view){
		System.out.println("id = " + id);
		if(isAuction)
		{
			try
			{
			Double oldPrice = Double.valueOf(price);
			EditText newBid = (EditText) findViewById(R.id.newBid);
			Double newPrice = Double.valueOf(newBid.getText().toString());
				if(newPrice <= oldPrice)
				{
					Toast.makeText(getApplicationContext(), "New bid must be higher than current bid", Toast.LENGTH_SHORT).show();
				}
				else
				{
					new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/delete-item-id.php", id, thisUser, menu, "true", String.valueOf(newPrice));
				}
			}
			catch(Exception e)
			{
				
			}
		}
		else
		{
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/delete-item-id.php", id, thisUser, menu, "false", "random");
		}
		/*Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Toast toast = Toast.makeText(this, "You have successfully bought the item!",Toast.LENGTH_LONG);
		toast.show();
		Listing.bookListing.remove(index);*/
	}

	public void postBuyOptions(String str)
	{
		if(str.equals("Item bought successfully"))
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// set title
			alertDialogBuilder.setTitle("Seller Information");

			// set dialog message
			alertDialogBuilder.setMessage("Your seller is " + seller).setCancelable(false)
			.setPositiveButton("Message Seller",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					Intent intent = new Intent(getBaseContext(), CreateMessageActivity.class);
					intent.putExtra("user", thisUser);
					intent.putExtra("toUser", seller);
					startActivity(intent);
				}
			})
			.setNegativeButton("Go Back",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					Intent intent = new Intent(getBaseContext(), MenuActivity.class);
					intent.putExtra("user", thisUser);
					startActivity(intent);
				}
			});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}
	}

	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2], urls[3], urls[4], urls[5]);
		}
		// onPostExecute displays the results of the AsyncTask.

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Data Sent!" + " " +  result, Toast.LENGTH_LONG).show();
			postBuyOptions(result);
		}

	}

	public static String POST(String url, String id, String user, String menu, String isAuction, String price){
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
			params.add(new BasicNameValuePair("user", user));
			params.add(new BasicNameValuePair("isAuction", isAuction));
			params.add(new BasicNameValuePair("price", price));
			
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

	private static String convertInputStreamToString(InputStream inputStream) throws IOException
	{
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
