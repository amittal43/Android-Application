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
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class ShowProduct extends Activity {


    int category;
    int index;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_product);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		Bundle bundle = getIntent().getExtras();
	    //Extract each value from the bundle for usage
	    category = bundle.getInt("CATEGORY");
	    index = bundle.getInt("INDEX");
		
	    TextView textTitle = (TextView) findViewById(R.id.showproductTitle);
	    textTitle.append(Listing.bookListing.get(index).getProduct().getTitle());
	    
	    TextView textDescription = (TextView) findViewById(R.id.showproductDescription);
	    textDescription.append(Listing.bookListing.get(index).getProduct().getDescription());
	    
	    TextView textQuality = (TextView) findViewById(R.id.showproductQuality);
	    textQuality.append(Listing.bookListing.get(index).getProduct().getQuality());
	    
	    TextView textPrice = (TextView) findViewById(R.id.showproductPrice);
	    String price = Double.toString(Listing.bookListing.get(index).getPrice());
	    textPrice.append(price);
		
	}
	
	public void buy (View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		Toast.makeText(this, "The item is bought successfully",Toast.LENGTH_LONG).show();
		Listing.bookListing.remove(index);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_product, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_show_product,
					container, false);
			return rootView;
		}
	}

}
