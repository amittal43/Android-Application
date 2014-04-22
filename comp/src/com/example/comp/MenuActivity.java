package com.example.comp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
	}
	
	//TODO: Create the method where the borrow button is clicked
	
		/** Called when the user clicks the Buy <and Borrow> button */
		public void openSearchResult (View view){
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
		}

		/** Called when the user clicks the Sell button */
		public void sellOption (View view){
			Intent intent = new Intent(this, SellOption.class);
			startActivity(intent);
		}
		
		/** Called when the user clicks the Sell button */
		public void showMessages (View view){
			Intent intent = new Intent(this, MessageActivity.class);
			intent.putExtra("user", getIntent().getExtras().getString("user"));
			startActivity(intent);
		}
		
		@Override
		public void onBackPressed() 
		{
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
		
		
		/** Called when the user clicks the Sell button */
		/*public void lendOption (View view){
			Intent intent = new Intent(this, LendOption.class);
			startActivity(intent);
		}*/
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
	
/*	public void searchItem(View view) {
		Intent intent = new Intent(this, SearchResultsActivity.class);
		Button searchButton = (Button) findViewById(R.id.sellClick);
		startActivity(intent);
	}*/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu, container,
					false);
			return rootView;
		}
	}*/

}
