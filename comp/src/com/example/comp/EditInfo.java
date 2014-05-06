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

import com.example.comp.MyProfile.HttpAsyncTask;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class EditInfo extends Activity {

	private String thisUser, editInfo;
	private TextView editTitle, confirmInput;
	private EditText inputInfo, inputConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_info);
		thisUser = getIntent().getExtras().getString("user");
		editInfo = getIntent().getExtras().getString("edit");
		
		editTitle = (TextView) findViewById(R.id.editTitle);
		editTitle.append(editInfo);
		
		confirmInput = (TextView) findViewById(R.id.confirmInput);
		confirmInput.append(editInfo);
		
		inputInfo = (EditText) findViewById(R.id.inputInfo);
		inputConfirm = (EditText) findViewById(R.id.inputConfirm);
		if (editInfo.equals("password")){
			inputInfo.setTransformationMethod(PasswordTransformationMethod.getInstance());
			inputConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
	}

	public void onClick(View view){
		String change = inputInfo.getText().toString();
		String confirm = inputConfirm.getText().toString();
		if (!change.matches(confirm)){
			Toast.makeText(this, "Inputs are not the same!", Toast.LENGTH_SHORT).show();
		} else {
			String edit;
			if (editInfo.equals("password")) edit = "password"; else
				if (editInfo.equals("email address")) edit = "email"; else
					edit = "phone";
			new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/edit-user-info.php", thisUser, edit, change);
		}
		
		
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
					Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getBaseContext(), MyProfile.class);
					intent.putExtra("user", thisUser);
					startActivity(intent);
					
				}
			}
			catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), result + "Error" + e.toString(),
							Toast.LENGTH_SHORT).show();
			}

		}
	}

	public static String POST(String url, String user, String edit, String change){
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
			params.add(new BasicNameValuePair("edit", edit));
			params.add(new BasicNameValuePair("change", change));
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
	
	/** Handle Action Bar Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
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
