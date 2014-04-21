package com.example.comp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Build;

public class CreateMessageActivity extends Activity {

	ArrayList<String> conversation;
	List<Map<String, String>> messageThread = new ArrayList<Map<String,String>>();

	private void initList()
	{
		boolean notset = true;
		for(int i=0; conversation!=null && i < conversation.size(); i++)
		{
			System.out.println(conversation.get(i));
			String message = null;
			String fromUser = null, toUser = null, time = null;
			EditText et = (EditText) findViewById(R.id.destUser);
			String allInfo = conversation.get(i);
			int i1 = allInfo.indexOf("!!!@@@###");

			fromUser = allInfo.substring(0, i1);
			int i2 = allInfo.indexOf("$$$%%%^^^");
			toUser = allInfo.substring(i1+9, i2);
			if(notset)
			{
				if(toUser.equals(getIntent().getExtras().getString("user")))
					et.setText(fromUser);
				else
					et.setText(toUser);
				notset = false;
			}
			i1 = allInfo.indexOf("&&&***(((");
			message = allInfo.substring(i2+9, i1);
			time = allInfo.substring(i1+9);

			String composedMessage = fromUser + " [" + time + "] " + "\n" + message;
			messageThread.add(createMessage("message", composedMessage));
		}
	}

	private HashMap<String, String> createMessage(String key, String name) 
	{
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put(key, name);
		return hMap;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_message);
		if(getIntent().hasExtra("conversation") )
			conversation = new ArrayList<String>(getIntent().getStringArrayListExtra("conversation"));
		if(getIntent().hasExtra("conversation"))
		{
			Button b1 = (Button) findViewById (R.id.findUser);
			b1.setVisibility(View.INVISIBLE);
		}
		else
		{
			Button b1 = (Button) findViewById (R.id.findUser);
			b1.setVisibility(View.VISIBLE);
		}
		if(getIntent().hasExtra("conversation"))
		{
			ListView lv = (ListView) findViewById(R.id.msgList);
			initList();
			SimpleAdapter simpleAdpt = new SimpleAdapter(getApplicationContext(), messageThread, android.R.layout.simple_list_item_1, new String[] {"message"}, new int[] {android.R.id.text1});
			lv.setAdapter(simpleAdpt);
		}
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_message, menu);
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
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_message,
					container, false);
			return rootView;
		}
	}

	public void sendMessage(View view)
	{
		if(validateFormInput())
		{
			EditText em = (EditText) findViewById (R.id.newMessage);
			EditText to = (EditText) findViewById (R.id.destUser);
			new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/send-message.php", getIntent().getExtras().getString("user"), to.getText().toString(), em.getText().toString());
			Toast.makeText(getApplicationContext(), "Sending " + em.getText().toString() + " to " + to.getText().toString() , Toast.LENGTH_SHORT).show();
		}
	}

	private boolean validateFormInput()
	{
		EditText em = (EditText) findViewById (R.id.newMessage);
		EditText to = (EditText) findViewById (R.id.destUser);
		if(em.getText().toString().length()>0 && to.getText().toString().length()>0)
			return true;
		else
			return false;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1], urls[2], urls[3]);
		}
		// onPostExecute displays the results of the AsyncTask.

		@Override
		protected void onPostExecute(String result) 
		{
			Toast.makeText(getBaseContext(), "Data Sent!" + " " + result , Toast.LENGTH_LONG).show();
			boolean success = false;
			try
			{
				int index = result.indexOf("bin/php");	
				if(index >= 0)
					result = result.substring(index+7).trim();
				else
					result = result.trim();
				JSONObject jObj = new JSONObject(result);
				if(jObj.optString("result").equals("1"))
				{
					Toast.makeText(getApplicationContext(), "Sent message", Toast.LENGTH_SHORT);
					success = true;
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Message not sent!" , Toast.LENGTH_LONG).show();
				}
			}
			catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), "Error" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
			finish();
			getIntent().putExtra("calling-activity", "CreateMessageActivity");
			if(success)
			{	
				EditText et1 = (EditText) findViewById (R.id.newMessage);
				EditText et2 = (EditText) findViewById (R.id.destUser);
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

				String newMessage = getIntent().getExtras().getString("user") + "!!!@@@###" +
						et2.getText().toString() + "$$$%%%^^^" +
						et1.getText().toString() + "&&&***(((" +
						sdf.format(d);
				conversation.add(newMessage);
				getIntent().removeExtra("conversation");
				getIntent().putStringArrayListExtra("conversation", conversation);
			}
			startActivity(getIntent());
		}
	}

	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
		intent.putExtra("user", getIntent().getExtras().getString("user"));
		startActivity(intent);
	}

	public static String POST(String url, String fromUser, String toUser, String message){
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
			params.add(new BasicNameValuePair("from", fromUser));
			params.add(new BasicNameValuePair("to", toUser));
			params.add(new BasicNameValuePair("message", message));

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
