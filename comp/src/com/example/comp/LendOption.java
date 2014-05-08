package com.example.comp;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.comp.DatePickerFragment.OnDataPass;

import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class LendOption extends FragmentActivity implements OnDataPass {

	private RadioGroup radioCategoryGroup;
	private RadioButton radioCategoryButton;
	private RadioGroup radioQualityGroup;
	private RadioButton radioQualityButton;
	private EditText doublePrice, stringTitle, stringDescription, showDate;
	private Calendar calendar;
	private String duedate = "", title = "", category = "", quality = "", description = "", price = "", thisUser;
	private String imagePath = "noImage";
	private static final int SELECT_PICTURE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_option);
        thisUser = getIntent().getExtras().getString("user");
        
        ((Button) findViewById(R.id.buttonLoadPictureExchange)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, SELECT_PICTURE);				
			}
		});
        //Toast.makeText(this, thisUser, Toast.LENGTH_LONG).show();
    }
    
    //Call DatePickerFragment - to pick a date
    public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(this.getSupportFragmentManager(), "datePicker"); 
	}
    
    //Passing data (loan due date) from fragment to activity
  	@Override
  	public void onDataPass(Calendar calendar){
  		this.calendar = calendar;
  		showDate = (EditText) findViewById(R.id.showDate);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
	    //Toast.makeText(this, sdf.format(this.calendar.getTime()),Toast.LENGTH_LONG).show();
	    duedate = sdf.format(this.calendar.getTime());
	    //Toast.makeText(this, duedate,Toast.LENGTH_LONG).show();
	    showDate.setText(duedate); 
  	}
  	
  	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				imagePath = getPath(selectedImageUri);            
				Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 200, 200);
				//Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_SHORT).show();
				ImageView image = (ImageView) findViewById(R.id.imageThumbnailExchange);
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
	public void lendOptionSubmit (View view){
		
		stringTitle = (EditText) findViewById(R.id.itemTitle);
		
		radioCategoryGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		int selectedId = radioCategoryGroup.getCheckedRadioButtonId();
		radioCategoryButton = (RadioButton) findViewById(selectedId);
		
		doublePrice = (EditText) findViewById(R.id.itemPrice);
		
		radioQualityGroup = (RadioGroup) findViewById(R.id.radioGroup2);
		selectedId = radioQualityGroup.getCheckedRadioButtonId();
		radioQualityButton = (RadioButton) findViewById(selectedId);
		
		stringDescription = (EditText) findViewById(R.id.itemDescription);

		title = getStringValue(stringTitle);
		category = (String) radioCategoryButton.getText();

		price = doublePrice.getText().toString();
		quality = (String) radioQualityButton.getText();
		description = getStringValue(stringDescription);
		
		//ensure all inputs are valid
		if (title.matches("") || description.matches("") || price.matches("") || duedate.matches("") || imagePath.equals("noImage")){
			Toast.makeText(this, "Input data is not complete!", Toast.LENGTH_LONG).show();
		}
		else
		{
			double priceDouble = Double.parseDouble(price);
			Intent intent = new Intent(this, LendOptionSubmit.class);
			intent.putExtra("user", thisUser);
			/*Bundle bundle = new Bundle();
			bundle.putString("TITLE", title);
			bundle.putString("CATEGORY", category);
			bundle.putString("QUALITY", quality);
			bundle.putString("DESCRIPTION", description);
			bundle.putDouble("PRICE", priceDouble);
			bundle.putString("DUEDATE", duedate);
			bundle.putString("IMAGE", imagePath);
			intent.putExtras(bundle);*/
			intent.putExtra("TITLE", title);
			intent.putExtra("CATEGORY", category);
			intent.putExtra("QUALITY", quality);
			intent.putExtra("DESCRIPTION", description);
			intent.putExtra("PRICE", priceDouble);
			intent.putExtra("DUEDATE", duedate);
			intent.putExtra("IMAGE", imagePath);
			
			startActivity(intent);

		//Toast.makeText(this, "Successful",Toast.LENGTH_LONG).show();
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
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.lend_option, menu);
	    return true;
	}
	
	/** Called when the user clicks the Action Bar - Menu button */
	public void goToMyProfile(MenuItem item){
		Intent intent = new Intent(this, MyProfile.class);
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Action Bar - Menu button */
	public void goToMenu(MenuItem item){
		Intent intent = new Intent(this, MenuActivity.class);
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}

}
