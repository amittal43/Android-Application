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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SellOption extends Activity {
	private RadioGroup radioCategoryGroup;
	private RadioButton radioCategoryButton;
	private EditText doublePrice;
	
	/** Called when the user clicks the Submit button */
	public void sellOptionSubmit (View view){
		//Intent intent = new Intent(this, SellOptionSubmit.class);
		
		radioCategoryGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		int selectedId = radioCategoryGroup.getCheckedRadioButtonId();
		radioCategoryButton = (RadioButton) findViewById(selectedId);
		int idx = radioCategoryGroup.indexOfChild(radioCategoryButton);
		//Toast.makeText(this, "Stres ne" + idx,Toast.LENGTH_LONG).show();
		/*
		Product product;
		doublePrice = (EditText) findViewById(R.id.doublePrice);
		double price = getDoubleValue(doublePrice);
		*/
		
		
		//startActivity(intent);
	}

	    
	private double getDoubleValue(EditText text) {
		double value;
		value = Double.parseDouble(text.getText().toString());
		return value;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_option);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_page, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_search_page,
					container, false);
			return rootView;
		}
	}

}
