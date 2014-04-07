package com.example.comp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchBooks extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_books);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		for (int i=0; i< Listing.bookListing.size(); ++i){
			
		    LinearLayout container = (LinearLayout)findViewById(R.id.container);
			final TextView rowTextView = new TextView(this);
		    // set some properties of rowTextView or something
		    rowTextView.setText(Listing.bookListing.get(i).getProduct().getTitle());

		    container.addView(rowTextView);
		    // add the textview to the linearlayout
		    //myLinearLayout.addView(rowTextView);

		    // save a reference to the textview for later
		    //myTextViews[i] = rowTextView
		    

		    //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		    //TextView tv = new TextView(viewrecords.this);
		    /*tv.setId(1);
		    tv.setTextSize(15);
		    tv.setText("test adding");
		    tv.setLayoutParams(lp);*/
		    //ll.addView(tv);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_books, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_search_books,
					container, false);
			return rootView;
		}
	}

}
