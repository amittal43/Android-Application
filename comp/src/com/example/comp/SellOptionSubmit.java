package com.example.comp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SellOptionSubmit extends Activity {

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_option_submit);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	    Bundle bundle = getIntent().getExtras();
	    //Extract each value from the bundle for usage
	    String title = bundle.getString("TITLE");
	    String category = bundle.getString("CATEGORY");
	    String quality = bundle.getString("QUALITY");
	    String description = bundle.getString("DESCRIPTION");
	    String price = Double.toString(bundle.getDouble("PRICE"));
	    
	    
	    TextView textTitle = (TextView) findViewById(R.id.confirmTitle);
	    textTitle.append(title);
	    
	    TextView textCategory = (TextView) findViewById(R.id.confirmCategory);
	    textCategory.append(category);
	    
	    TextView textQuality = (TextView) findViewById(R.id.confirmQuality);
	    textQuality.append(quality);
	    
	    TextView textDescription = (TextView) findViewById(R.id.confirmDescription);
	    textDescription.append(description);
	    
	    TextView textPrice = (TextView) findViewById(R.id.confirmPrice);
	    textPrice.append(price);
	}
	
	/**
	 * Called when the user clicks the confirm button
	 * Go back to MainActivity (Menu) page
	 * @param view
	 */
	public void backToHome(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Toast.makeText(this, "The Item is added successfully",Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sell_option_submit, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_sell_option_submit, container, false);
			return rootView;
		}
	}

}
