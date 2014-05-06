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

import com.example.comp.SearchItem.HttpAsyncTask;

import android.app.Activity;
import android.app.ActionBar;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MyProfile extends Activity {

	String thisUser;
	TextView username, password, emailAddress, phoneNumber, name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		
		username = (TextView)findViewById(R.id.username);
		password = (TextView)findViewById(R.id.password);
		emailAddress = (TextView)findViewById(R.id.emailAddress);
		phoneNumber = (TextView)findViewById(R.id.phoneNumber);
		name = (TextView)findViewById(R.id.name);
		
		thisUser = getIntent().getExtras().getString("user");
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-user-info.php", thisUser);
	}
	
	public void onClick(View view) {
		Intent intent = new Intent(this, EditInfo.class);
		intent.putExtra("user", thisUser);
		switch(view.getId()){
			case R.id.editPassword:
				intent.putExtra("edit", "password");
				startActivity(intent);
				break;
				
			case R.id.editEmail: 
				intent.putExtra("edit", "email address");
				startActivity(intent);
				break;
			

			case R.id.editPhone:
				intent.putExtra("edit", "phone number");
				startActivity(intent);
				break;
		}

	}

	class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1]);
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
					Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
				} else {
					
					JSONArray jArray = jObj.getJSONArray("list");
					for(int i=0; i < jArray.length(); i++){
						JSONObject obj = jArray.getJSONObject(i);
						
						String stringPassword = obj.optString("password");
						String stringEmail = obj.optString("email");
						String stringPhone = obj.optString("phone");
						String stringName = obj.optString("name");
						
						//Toast.makeText(getBaseContext(), stringName, Toast.LENGTH_LONG).show();
						
						String invPassword = "";
						for (int j=0; j<stringPassword.length(); ++j) invPassword+='*';
						
						username.setText(thisUser);
						password.setText(invPassword);
						emailAddress.setText(stringEmail);
						phoneNumber.setText(stringPhone);
						name.setText(stringName);
						
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

	public static String POST(String url, String user){
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);

			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
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
	    inflater.inflate(R.menu.my_profile, menu);
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
