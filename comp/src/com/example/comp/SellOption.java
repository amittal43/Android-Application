package com.example.comp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
	private String imagePath = "noImage";
	
	private static final int SELECT_PICTURE = 1;

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
		
		// TODO: Add the picture to the product
		Product prod = new Product(quality, title, description);
		Listing newList = new Listing(price,prod);
		
		
		//TODO: create an exception if user's input is not valid
		
		Intent intent = new Intent(this, SellOptionSubmit.class);
		Bundle bundle = new Bundle();
		bundle.putString("TITLE", title);
		bundle.putString("CATEGORY", category);
		bundle.putString("QUALITY", quality);
		bundle.putString("DESCRIPTION", description);
		bundle.putDouble("PRICE", price);
		bundle.putInt("CATEGORYINDEX", categoryIdx);
		bundle.putString("IMAGE", imagePath);
		intent.putExtras(bundle);
		startActivity(intent);
		//Toast.makeText(this, "Sucessful",Toast.LENGTH_LONG).show();
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
		
		OnTouchListener l = new OnTouchListener() {


			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_PICTURE);;
				return true;
			}
		};


		((Button) findViewById(R.id.buttonLoadPicture)).setOnTouchListener(l);

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				imagePath = getPath(selectedImageUri);            
				Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 200, 200);
				//Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_SHORT).show();
				ImageView image = (ImageView) findViewById(R.id.imageTumbnail);
				image.setImageBitmap(ThumbImage);
			}
		}
	}

	/**
	 * helper to retrieve the path of an image URI
	 */	
	public String getPath(Uri uri) {
		// just some safety built in 
		if( uri == null ) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}
/*
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
	*/
	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*
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
	*/
}
