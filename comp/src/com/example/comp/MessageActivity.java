package com.example.comp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MessageActivity extends Activity {

	ArrayList<ArrayList<JSONObject>> messages;
	List<Map<String, String>> usersList = new ArrayList<Map<String,String>>();
	String thisUser = null;

	private void initList()
	{
		for(int i=0; messages!=null && i < messages.size(); i++)
		{
			String otherUser = null;
			if(messages.get(i).get(0).optString("from").equals(thisUser))
			{
				otherUser = messages.get(i).get(0).optString("to")+"("+ String.valueOf(messages.get(i).size()) + ")" + "\n" +
						messages.get(i).get(messages.get(i).size()-1).optString("message");
			}
			else if(messages.get(i).get(0).optString("to").equals(thisUser))
			{
				otherUser = messages.get(i).get(0).optString("from")+"("+ String.valueOf(messages.get(i).size()) + ")" + "\n" +
						messages.get(i).get(messages.get(i).size()-1).optString("message");
			}
			usersList.add(createUser("user", otherUser));
		}
	}

	private HashMap<String, String> createUser(String key, String name) 
	{
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put(key, name);
		return hMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		messages = new ArrayList<ArrayList<JSONObject>>();
		thisUser = getIntent().getExtras().getString("user");
		new HttpAsyncTask().execute("http://ihome.ust.hk/~sraghuraman/cgi-bin/fetch-messages.php", getIntent().getExtras().getString("user"));
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}

	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}

	public void search(View view)
	{
		EditText et1 = (EditText) findViewById (R.id.searchKey);
		if(getIntent().hasExtra("search-keyword"))
			getIntent().removeExtra("search-keyword");
		if(et1.getText().toString().length() > 0)
		{
			getIntent().putExtra("search-keyword", et1.getText().toString());
		}
		finish();
		startActivity(getIntent());
	}

	public void createNewMessage(View view)
	{
		Intent intent = new Intent(this, CreateMessageActivity.class);
		intent.putExtra("user", thisUser);
		intent.putExtra("calling-activity", "MessageActivity");
		startActivity(intent);
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String...urls) {
			return POST(urls[0], urls[1]);
		}
		// onPostExecute displays the results of the AsyncTask.

		@Override
		protected void onPostExecute(String result) 
		{
			//Toast.makeText(getBaseContext(), "Data Sent!" + " " + result , Toast.LENGTH_LONG).show();
			System.out.println(result);
			try
			{
				int index = result.indexOf("bin/php");	
				if(index >= 0)
					result = result.substring(index+7).trim();
				else
					result = result.trim();
				JSONObject jObj = new JSONObject(result);
				JSONArray jArray = jObj.getJSONArray("list");
				ArrayList<String> users = new ArrayList<String>();

				for(int i=0; i < jArray.length(); i++)
				{
					JSONObject obj = jArray.getJSONObject(i);
					if(!users.contains(obj.optString("from")))
					{
						users.add(obj.optString("from"));
					}
					if(!users.contains(obj.optString("to")))
					{
						users.add(obj.optString("to"));
					}
				}
				// the above code would get you all the users who have communicated with this user
			
				users.remove(getIntent().getExtras().getString("user"));

				System.out.println("Printing users" + users);
				
				String thisUser = getIntent().getExtras().getString("user");
				
				for(int i=0; i < users.size(); i++)
				{
					messages.add(new ArrayList<JSONObject>());
				}

				for(int i=0; i < jArray.length(); i++)
				{
					JSONObject obj = jArray.getJSONObject(i);
					String otherUser = null;
					if(obj.optString("from").equals(thisUser))
					{
						otherUser = obj.optString("to");
					}
					else if(obj.optString("to").equals(thisUser))
					{
						otherUser = obj.optString("from");
					}
					System.out.println("for " + thisUser + " this is the user we are searching for " + otherUser);
					int userIndex = users.indexOf(otherUser);
					messages.get(userIndex).add(obj);
				}

				for(int i=0; i < messages.size(); i++)
				{
					messageComparator m = new messageComparator();
					Collections.sort(messages.get(i), m);

					for(int j=0; j < messages.get(i).size(); j++)
					{
						System.out.println(messages.get(i).get(j));
					}
				}


				// filtering by search-keyword
				if(getIntent().hasExtra("search-keyword"))
				{
					String term = getIntent().getExtras().getString("search-keyword");
					for(int i=0; i < messages.size(); i++)
					{
						boolean show = false; 

						for(int j=0; j < messages.get(i).size(); j++)
						{
							String s1 = messages.get(i).get(0).optString("from");
							String s2 = messages.get(i).get(0).optString("to");
							if(!term.equals(thisUser))
							{
								if(s1.equals(term) || s2.equals(term))
								{
									show = true;
									break;
								}
							}
							String msgContent = messages.get(i).get(j).optString("message");
							if(msgContent.contains(term))
							{
								show = true;
								break;
							}
						}
						if(show == false)
						{
							messages.remove(i);
							i--;
						}
					}
				}
				ListView lv = (ListView) findViewById(R.id.listView);
				initList();
				SimpleAdapter simpleAdpt = new SimpleAdapter(getApplicationContext(), usersList, android.R.layout.simple_list_item_1, new String[] {"user"}, new int[] {android.R.id.text1});
				/*LinearLayout ll = (LinearLayout) findViewById(R.id.msgList); 
				for(int i=0; i < messages.size(); i++)
				{
					Button t1 = new Button(getApplicationContext());
					t1.setHeight(50);
					t1.setGravity(10);
					t1.setText(messages.get(i).get(0).optString("to")+"\nFirst Message");
					ll.addView(t1);
				}*/
				lv.setAdapter(simpleAdpt);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
							long id) {

						// We know the View is a TextView so we can cast it
						Intent intent = new Intent(getApplicationContext(), CreateMessageActivity.class);
						ArrayList<String> conversation = new ArrayList<String>();
						for(int i=0; messages!=null && messages.get(position)!=null 
								&& i < messages.get(position).size(); i++)
						{
							String temp = messages.get(position).get(i).optString("from") + "!!!@@@###" + messages.get(position).get(i).optString("to") + "$$$%%%^^^" + 
									messages.get(position).get(i).optString("message") + "&&&***(((" + messages.get(position).get(i).optString("time");
							conversation.add(temp);
						}
						intent.putStringArrayListExtra("conversation", conversation);
						intent.putExtra("calling-activity", "MessageActivity");

						intent.putExtra("user", getIntent().getExtras().getString("user"));
						startActivity(intent);
						/*TextView clickedView = (TextView) view;
				         Toast.makeText(getApplicationContext(), "Item with id ["+id+"] - Position ["+position+"] - Planet ["+clickedView.getText()+"]", Toast.LENGTH_LONG).show();
						 */
					}
				});

			}
			catch(JSONException e)
			{
				Toast.makeText(getApplicationContext(), result + "Error" + e.toString(),
						Toast.LENGTH_LONG).show();
			} 
		}
	}


	private class messageComparator implements Comparator<JSONObject>
	{
		@Override
		public int compare(JSONObject j1, JSONObject j2)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
			try
			{
				Date d1 = sdf.parse((String)j1.opt("time"));
				Date d2 = sdf.parse((String)j2.opt("time"));
				if(d1.compareTo(d2) < 0)
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
			catch(ParseException pe)
			{
				Toast.makeText(getApplicationContext(), "Cannot reorder jsons", Toast.LENGTH_LONG);
			}
			return 0;
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


			// ** Alternative way to convert Person object to JSON string usin Jackson Lib 
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person); 

			// 5. set json to StringEntity

			//StringEntity se = new StringEntity(json);

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
	    inflater.inflate(R.menu.message, menu);
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
