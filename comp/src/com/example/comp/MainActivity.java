package com.example.comp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*final Button buyButton = (Button) findViewById(R.id.buyClick);
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSearchResult(v);
            }
        });*/
		
	}
		public void onClick(View view) 
		{
			switch(view.getId()){
			case R.id.button1: 
				Intent intent1 = new Intent(getBaseContext(), LoginActivity.class);
				startActivity(intent1);
				break;
			

/*			case R.id.button4:
				Intent intent2 = new Intent(getBaseContext(), CreateUserActivity.class);
				startActivity(intent2);
				break;*/
		}

		}


	//TODO: Create the method where the borrow button is clicked
	
	/** Called when the user clicks the Buy <and Borrow> button */
	public void openSearchResult (View view){
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}

	/** Called when the user clicks the Sell button */
	public void lendOption (View view){
		Intent intent = new Intent(this, LendOption.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void searchItem(View view) {
		Intent intent = new Intent(this, SearchResultsActivity.class);
		Button searchButton = (Button) findViewById(R.id.sellClick);
		startActivity(intent);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
