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

public class SellOption extends Activity {
	private RadioGroup radioCategoryGroup;
	private RadioButton radioCategoryButton;
	private RadioGroup radioQualityGroup;
	private RadioButton radioQualityButton;
	private EditText doublePrice;
	private EditText stringTitle;
	//private EditText stringSummary;
	private EditText stringDescription;
	
	/** Called when the user clicks the Submit button */
	public void sellOptionSubmit (View view){
		
		
		/**
		 * Get the title of the product
		 */
		stringTitle = (EditText) findViewById(R.id.itemTitle);
		String title = getStringValue(stringTitle);	
		
		
		/**
		 * Get the category of the product
		 */
		radioCategoryGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		int selectedId = radioCategoryGroup.getCheckedRadioButtonId();
		radioCategoryButton = (RadioButton) findViewById(selectedId);
		String category = (String) radioCategoryButton.getText();
		int categoryIdx = radioCategoryGroup.indexOfChild(radioCategoryButton);

		/**
		 * Get the price of the product
		 */
		doublePrice = (EditText) findViewById(R.id.itemPrice);
		double price = getDoubleValue(doublePrice);
		
		/**
		 * Get the quality of the product
		 */
		radioQualityGroup = (RadioGroup) findViewById(R.id.radioGroup2);
		selectedId = radioQualityGroup.getCheckedRadioButtonId();
		radioQualityButton = (RadioButton) findViewById(selectedId);
		String quality = (String) radioQualityButton.getText();
		/*
		/**
		 * Get the summary of the product
		 */ /*
		stringSummary = (EditText) afindViewById(R.id.itemSummary);
		String summary = getStringValue(stringSummary);	
		*/
		
		/**
		 * Get the description of the product
		 */
		stringDescription = (EditText) findViewById(R.id.itemDescription);
		String description = getStringValue(stringDescription);
		
		Product prod = new Product(quality, title, description, 0);
		Listing newList = new Listing(price,prod);
		
		addItemToListCollection(newList, categoryIdx);
		
		//TODO: create an exception if user's input is not valid
		
		Intent intent = new Intent(this, SellOptionSubmit.class);
		Bundle bundle = new Bundle();
		bundle.putString("TITLE", title);
		bundle.putString("CATEGORY", category);
		bundle.putString("QUALITY", quality);
		bundle.putString("DESCRIPTION", description);
		bundle.putDouble("PRICE", price);
		intent.putExtras(bundle);
		startActivity(intent);
		//Toast.makeText(this, "Sucessful",Toast.LENGTH_LONG).show();
	}

	/**
	 * add a Listing instance to one of the collections
	 * (book/bedding/sport/fashion/cooking/electronics)
	 * @param newList
	 * @param index
	 */
	private void addItemToListCollection(Listing newList, int index){
		switch(index){
			case 0:
				Listing.bookListing.add(newList);
				break;
			case 1:
				Listing.beddingListing.add(newList);
				break;
			case 2:
				Listing.sportListing.add(newList);
				break;
			case 3:
				Listing.fashionListing.add(newList);
				break;
			case 4:
				Listing.cookingListing.add(newList);
				break;
			default:
				Listing.electronicsListing.add(newList);
				break;
		}
	}
	    
	private double getDoubleValue(EditText text) {
		double value;
		value = Double.parseDouble(text.getText().toString());
		return value;
	}
	
	private String getStringValue(EditText text){
		return text.getText().toString();
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
