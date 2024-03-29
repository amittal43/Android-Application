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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	}
	

	public void onClick(View view) 
	{
		switch(view.getId()){
		case R.id.login:
			// call AsynTask to perform network operation on separate thread
			if(validateFormInput())
			{
				EditText username = (EditText) findViewById(R.id.username2);
				EditText password = (EditText) findViewById(R.id.password2);
				new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/login.php", username.getText().toString(), password.getText().toString());
			}
			else
				Toast.makeText(this, "Incomplete Data", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public boolean validateFormInput()
	{
		EditText username = (EditText) findViewById(R.id.username2);
		EditText password = (EditText) findViewById(R.id.password2);
		if(username.getText().toString().length() <= 0 || 
				password.getText().toString().length() <= 0)
		{
			return false;
		}

		return true;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2]);
		}
		// onPostExecute displays the results of the AsyncTask.

		@Override
		protected void onPostExecute(String result) 
		{
			//Toast.makeText(getBaseContext(), "Data Sent!" + " " + result , Toast.LENGTH_LONG).show();
			try
			{
				int index = result.indexOf("bin/php");	
				result = result.substring(index+7);
				JSONObject jObj = new JSONObject(result);
				if(jObj.optString("result").equals("1"))
				{
					EditText et = (EditText) findViewById(R.id.status2);
					et.setText("User Authenticated");
					Intent intent = new Intent(getBaseContext(), MenuActivity.class);
					EditText user = (EditText) findViewById(R.id.username2);
					intent.putExtra("user", user.getText().toString());
					startActivity(intent);
					finish();
				}
				else
				{
					EditText et = (EditText) findViewById(R.id.status2);
					et.setText("User Authentication Failed");
					//Toast.makeText(getBaseContext(), "User Authentication Failure" , Toast.LENGTH_LONG).show();
				}
			}
			catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), "Error" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}

			
		}
	}


	public static String POST(String url, String user, String password){
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);


			// ** Alternative way to convert Person object to JSON string usin Jackson Lib 
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person); 

			// 5. set json to StringEntity

			//StringEntity se = new StringEntity(json);

			// Request parameters and other properties.
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("username", user));
			params.add(new BasicNameValuePair("password", password));
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
