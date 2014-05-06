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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchItem extends Activity {

	static String thisUser;
	String menu;
	boolean onCreate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		thisUser = getIntent().getExtras().getString("user");
		menu = getIntent().getExtras().getString("menu");

		ArrayList<String> fields = new ArrayList<String>();
		fields.add("Title");
		fields.add("Description");
		fields.add("Quality");
		fields.add("Price");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_item, fields);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spin = (Spinner) findViewById(R.id.sortByField);
		spin.setAdapter(dataAdapter);
		if(!getIntent().hasExtra("sort-field"))
		{
			spin.setSelection(0);
		}
		else
		{
			ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter(); 
			int spinnerPosition = myAdap.getPosition(getIntent().getExtras().getString("sort-field"));
			spin.setSelection(spinnerPosition);
		}
		ArrayList<String> options = new ArrayList<String>();
		options.add("Increasing");
		options.add("Decreasing");
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_spinner_item, options);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spin2 = (Spinner) findViewById(R.id.incOrDec);
		spin2.setAdapter(dataAdapter2);
		if(!getIntent().hasExtra("sort-order"))
		{
			spin2.setSelection(0);
		}
		else
		{
			ArrayAdapter myAdap = (ArrayAdapter) spin2.getAdapter(); 
			int spinnerPosition = myAdap.getPosition(getIntent().getExtras().getString("sort-order"));
			spin2.setSelection(spinnerPosition);
		
		}
		String category = getIntent().getExtras().getString("CATEGORY");
	
		if(getIntent().hasExtra("sort-field") && getIntent().hasExtra("sort-order"))
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-item.php", category, getIntent().getExtras().getString("sort-field"), getIntent().getExtras().getString("sort-order") );
		else
		{
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-item.php", category, "title", "Increasing");
		getIntent().putExtra("sort-field", "title");	
		getIntent().putExtra("sort-order", "Increasing");	
		}
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}
	
	public void sort(View view)
	{
		Intent i = getIntent();
		Spinner spin = (Spinner) findViewById(R.id.sortByField);
		String field = spin.getSelectedItem().toString();
		if(field.equals("Description"))
			field = "descr";
		i.putExtra("sort-field", field);
		spin = (Spinner) findViewById(R.id.incOrDec);
		String order = spin.getSelectedItem().toString();
		i.putExtra("sort-order", order);
		finish();
		startActivity(i);
	}

	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2], urls[3]);
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
						final String isAuction = obj.optString("isAuction");
						final String seller = obj.optString("seller");
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
								bundle.putString("SELLER", seller);
								if(isAuction.equals("true"))
								bundle.putBoolean("ISAUCTION", true);
								else
								bundle.putBoolean("ISAUCTION", false);
								Intent intent = new Intent(SearchItem.this, ShowProduct.class);
								intent.putExtras(bundle);
								intent.putExtra("user", thisUser);
								intent.putExtra("menu", menu);
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

	public static String POST(String url, String category, String field, String order){
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
			params.add(new BasicNameValuePair("sortField", field));
			params.add(new BasicNameValuePair("order", order));
			params.add(new BasicNameValuePair("user", thisUser));
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
}
