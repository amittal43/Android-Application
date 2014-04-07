package com.example.comp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
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
		
		final Bundle bundle = new Bundle();
		
		for (int i=0; i < Listing.bookListing.size(); ++i){
			
		    LinearLayout container = (LinearLayout)findViewById(R.id.container);
		    Button rowButton = new Button(this);
		    //LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)container.getLayoutParams();
		    //rowButton.setLayoutParams(param);
		    

			String content = Listing.bookListing.get(i).getProduct().getTitle();
			content += "\n" + Listing.bookListing.get(i).getPrice();
		    // set some properties of rowTextView or something
		    rowButton.setText(content);
		    
		    final int index = i;
		    
		    rowButton.setOnClickListener(new OnClickListener() {
		        public void onClick(View v) {
		            bundle.putInt("CATEGORY", 0);
		            bundle.putInt("INDEX", index);
		            Intent intent = new Intent(SearchBooks.this, ShowProduct.class);
					intent.putExtras(bundle);
					startActivity(intent);
		        }
		    });
		    
		    container.addView(rowButton);

		    
		   /* RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();
		    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    params.addRule(RelativeLayout.LEFT_OF, R.id.id_to_be_left_of);
		    button.setLayoutParams(params); //causes layout update
			*/
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
