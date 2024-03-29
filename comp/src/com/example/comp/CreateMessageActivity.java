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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateMessageActivity extends Activity {

	ArrayList<String> conversation;
	List<Map<String, String>> messageThread = new ArrayList<Map<String,String>>();
	ArrayList<String> allUsers;
	String thisUser;
	
	/* test Variables */
	int testNumUsers, testNumMsgs;
	String testRecepient;
	boolean testMessageSuccess;
	
	private void initList()
	{
		for(int i=0; conversation!=null && i < conversation.size(); i++)
		{
			System.out.println(conversation.get(i));
			String message = null;
			String fromUser = null, toUser = null, time = null;
			String allInfo = conversation.get(i);
			int i1 = allInfo.indexOf("!!!@@@###");
			fromUser = allInfo.substring(0, i1);
			int i2 = allInfo.indexOf("$$$%%%^^^");
			toUser = allInfo.substring(i1+9, i2);
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

	public void testConversationLoading()
	{
		ArrayList<String> aList = new ArrayList<String>();
		String str1 = "kath" + "!!!@@@###" + "user" + "$$$%%%^^^" + "hello" + "&&&***(((" + "2014-05-08 03:13:11";
		String str2 = "kath" + "!!!@@@###" + "user" + "$$$%%%^^^" + "how r u?" + "&&&***(((" + "2014-05-08 03:13:14";
		String str3 = "user" + "!!!@@@###" + "kath" + "$$$%%%^^^" + "i am fine thank you" + "&&&***(((" + "2014-05-08 03:13:17";
		aList.add(str1);
		aList.add(str2);
		aList.add(str3);
		getIntent().putStringArrayListExtra("conversation", aList);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_message);
		thisUser = getIntent().getExtras().getString("user");
		if(getIntent().hasExtra("create-message-test") && !getIntent().hasExtra("conversation"))
		{
			testConversationLoading();
		}
		if(getIntent().hasExtra("conversation") )
			conversation = new ArrayList<String>(getIntent().getStringArrayListExtra("conversation"));
		else
			conversation = new ArrayList<String>();
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-users.php", "", "", "");


		if(getIntent().hasExtra("conversation"))
		{
			int index1 = conversation.get(0).indexOf("!!!@@@###");
			int index2 = conversation.get(0).indexOf("$$$%%%^^^");
			String recepient = conversation.get(0).substring(index1+9, index2);
			System.out.println("the recip is " + recepient);
			//ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter(); 
			//int spinnerPosition = myAdap.getPosition(recepient);
			//spin.setSelection(spinnerPosition);
			//spin.setEnabled(false);
		}
		else
		{
			//	new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-users.php", "", "", "");
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

	public boolean returnMessageSuccess()
	{
		return testMessageSuccess;
	}
	
	
	public void sendMessage(View view)
	{
		if(validateFormInput())
		{
			EditText em = (EditText) findViewById (R.id.newMessage);
			Spinner spin = (Spinner) findViewById (R.id.recepList);
			String toUser = String.valueOf(spin.getSelectedItem());
			new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/send-message.php", thisUser, toUser, em.getText().toString());
			Toast.makeText(getApplicationContext(), "Sending " + em.getText().toString() + " to " + toUser , Toast.LENGTH_SHORT).show();
		}
	}

	private boolean validateFormInput()
	{
		EditText em = (EditText) findViewById (R.id.newMessage);
		Spinner spin = (Spinner) findViewById (R.id.recepList);
		String toUser = String.valueOf(spin.getSelectedItem());
		if(em.getText().toString().length()>0 && toUser.length()>0)
			return true;
		else
			return false;
	}

	
	public void processServerResponse(String result)
	{
		boolean useToMessage = false;
		boolean success = false;
		try
		{
			int index = result.indexOf("bin/php");	
			if(index >= 0)
				result = result.substring(index+7).trim();
			else
				result = result.trim();
			JSONObject jObj = new JSONObject(result);
			if(jObj.has("result") && jObj.optString("result").equals("1"))
			{
				Toast.makeText(getApplicationContext(), "Sent message", Toast.LENGTH_SHORT);
				success = true;
				useToMessage = true;
				testMessageSuccess = true;
				return;
			}
			else if(jObj.has("list"))
			{
				JSONArray jArray = jObj.getJSONArray("list");
				allUsers = new ArrayList<String>();
				for(int i=0; i < jArray.length(); i++)
				{
					String obj = jArray.get(i).toString();
					allUsers.add(obj);
				}
				allUsers.remove(getIntent().getExtras().getString("user"));
				//Toast.makeText(getApplicationContext(), "Downloaded all users info", Toast.LENGTH_SHORT).show();;
				//success = true;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Message not sent!" , Toast.LENGTH_LONG).show();
				useToMessage = true;
				testMessageSuccess = false;
				return;
			}
		}
		catch(JSONException e)
		{
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		if(useToMessage)
		{
			finish();
			getIntent().putExtra("calling-activity", "CreateMessageActivity");
			if(success)
			{	
				EditText et1 = (EditText) findViewById (R.id.newMessage);
				Spinner spin = (Spinner) findViewById (R.id.recepList);
				String toUser = String.valueOf(spin.getSelectedItem());
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

				String newMessage = getIntent().getExtras().getString("user") + "!!!@@@###" +
						toUser + "$$$%%%^^^" +
						et1.getText().toString() + "&&&***(((" +
						sdf.format(d);
				conversation.add(newMessage);
				getIntent().removeExtra("conversation");
				getIntent().putStringArrayListExtra("conversation", conversation);
				
			}
			startActivity(getIntent());
		}
		else
		{
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_spinner_item, allUsers);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner spin = (Spinner) findViewById(R.id.recepList);
			spin.setAdapter(dataAdapter);
			if(getIntent().hasExtra("conversation"))
			{
				int index1 = conversation.get(0).indexOf("!!!@@@###");
				int index2 = conversation.get(0).indexOf("$$$%%%^^^");
				String sender = conversation.get(0).substring(0, index1);
				String recepient = conversation.get(0).substring(index1+9, index2);
				String reqName = null;
				if(getIntent().getExtras().getString("user").equals(sender))
					reqName = recepient;
				else
					reqName = sender;
				testRecepient = reqName;
				System.out.println("the recippp is " + reqName);
				ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter(); 
				int spinnerPosition = myAdap.getPosition(reqName);
				spin.setSelection(spinnerPosition);
				spin.setEnabled(false);
			}
			if(getIntent().hasExtra("toUser"))
			{
				ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter(); 
				System.out.println(getIntent().getExtras().getString("toUser"));
				int spinnerPosition = myAdap.getPosition(getIntent().getExtras().getString("toUser"));
				spin.setSelection(spinnerPosition);
				spin.setEnabled(false); 
			}

		}

	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) 
		{
			return POST(urls[0], urls[1], urls[2], urls[3]);
		}
		// onPostExecute displays the results of the AsyncTask.

		@Override
		protected void onPostExecute(String result) 
		{
			//Toast.makeText(getBaseContext(), "Data Sent!" + " " + result , Toast.LENGTH_LONG).show();
			processServerResponse(result);


		}
	}

	@Override
	public void onBackPressed() 
	{
		if(getIntent().hasExtra("toUser"))
		{
			Intent intent = new Intent(getBaseContext(), MenuActivity.class);
			intent.putExtra("user", getIntent().getExtras().getString("user"));
			startActivity(intent);

		}
		else
		{
			Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
			intent.putExtra("user", getIntent().getExtras().getString("user"));
			startActivity(intent);
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_message, menu);
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
