package com.example.comp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SellOption extends Activity {
	private RadioGroup radioCategoryGroup;
	private RadioButton radioCategoryButton;
	private RadioGroup radioQualityGroup;
	private RadioButton radioQualityButton;
	private EditText doublePrice;
	private EditText stringTitle;
	private EditText stringDescription;

	String thisUser;

	private String imagePath = "noImage";

	private static final int SELECT_PICTURE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell_option);
		thisUser = getIntent().getExtras().getString("user");

		((Button) findViewById(R.id.buttonLoadPicture)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Intent intent = new Intent();
				//intent.setType("image/*");
				//intent.setAction(Intent.ACTION_GET_CONTENT);
				//startActivityForResult(Intent.createChooser(intent, "Select picture"), SELECT_PICTURE);;
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, SELECT_PICTURE);
			}
		});

	}

	public void setAuctionDate(View view)
	{
		CheckBox cb = (CheckBox) view;
		if(cb.isChecked())
		{

			EditText hh = (EditText) findViewById(R.id.aucHour);
			EditText mm = (EditText) findViewById(R.id.aucMin);
			EditText DD = (EditText) findViewById(R.id.aucDay);
			EditText MM = (EditText) findViewById(R.id.aucMonth);
			EditText YY = (EditText) findViewById(R.id.aucYear);
			hh.setEnabled(true);
			mm.setEnabled(true);
			DD.setEnabled(true);
			MM.setEnabled(true);
			YY.setEnabled(true);
		}
		else
		{
			EditText hh = (EditText) findViewById(R.id.aucHour);
			EditText mm = (EditText) findViewById(R.id.aucMin);
			EditText DD = (EditText) findViewById(R.id.aucDay);
			EditText MM = (EditText) findViewById(R.id.aucMonth);
			EditText YY = (EditText) findViewById(R.id.aucYear);
			hh.setEnabled(false);
			mm.setEnabled(false);
			DD.setEnabled(false);
			MM.setEnabled(false);
			YY.setEnabled(false);

		}
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

		/**
		 * Get the price of the product
		 */
		doublePrice = (EditText) findViewById(R.id.itemPrice);
		String price = doublePrice.getText().toString();

		/**
		 * Get the quality of the product
		 */
		radioQualityGroup = (RadioGroup) findViewById(R.id.radioGroup2);
		selectedId = radioQualityGroup.getCheckedRadioButtonId();
		radioQualityButton = (RadioButton) findViewById(selectedId);
		String quality = (String) radioQualityButton.getText();

		/**
		 * Get the description of the product
		 */
		stringDescription = (EditText) findViewById(R.id.itemDescription);
		String description = getStringValue(stringDescription);

		//TODO: create an exception if user's input is not valid

		if (title.matches("") || description.matches("") || price.matches(""))
		{
			Toast.makeText(this, "Input data is not complete!", Toast.LENGTH_LONG).show();
		}
		else {
			double priceDouble = Double.parseDouble(price);

			EditText hh = (EditText) findViewById(R.id.aucHour);
			EditText mm = (EditText) findViewById(R.id.aucMin);
			EditText DD = (EditText) findViewById(R.id.aucDay);
			EditText MM = (EditText) findViewById(R.id.aucMonth);
			EditText YY = (EditText) findViewById(R.id.aucYear);

			try
			{
				int h = Integer.valueOf(hh.getText().toString());
				int m = Integer.valueOf(mm.getText().toString());
				int D = Integer.valueOf(DD.getText().toString());
				int M = Integer.valueOf(MM.getText().toString());
				int Y = Integer.valueOf(YY.getText().toString());
				Date d = new Date();
				d.setHours(h);
				d.setMinutes(m);
				d.setDate(D);
				d.setMonth(M);
				d.setYear(Y);
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
				String end = sdf.format(d);
				System.out.println("Auction end date is " + end);
				Intent intent = new Intent(this, SellOptionSubmit.class);
				Bundle bundle = new Bundle();
				bundle.putString("DATE", end);
				bundle.putString("TITLE", title);
				bundle.putString("CATEGORY", category);
				bundle.putString("QUALITY", quality);
				bundle.putString("DESCRIPTION", description);
				bundle.putDouble("PRICE", priceDouble);
				bundle.putString("IMAGE", imagePath);
				intent.putExtras(bundle);
				if(thisUser == null)
					System.out.println("this user is null");
				intent.putExtra("user", thisUser);
				System.out.println("Passing info to SellOptionSubmit");
				startActivity(intent);

			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Illegal Format", Toast.LENGTH_LONG).show();
			}

		}
		//Toast.makeText(this, "Successful",Toast.LENGTH_LONG).show();
	}



	private double getDoubleValue(EditText text) {
		double value;
		value = Double.parseDouble(text.getText().toString());
		return value;
	}

	private String getStringValue(EditText text){
		return text.getText().toString();
	}


}
