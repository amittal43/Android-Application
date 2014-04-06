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
import android.widget.ImageView;

public class SearchResultsActivity extends Activity {
	
	private static final int SELECT_PHOTO = 100;

	Listing item;
	Product prod;
	
	ImageView imageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		
		
		
//		ArrayList<Listing> collection = item.getBeddingListing();
//		int imageIcon = prod.getIcon();
		
		
//		ImageView img = (ImageView) findViewById(R.id.imageView1);
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO); 
		 
	}	
	
//	protected void onActivityResult(int requestCode, int resultCode, 
//		       Intent imageReturnedIntent) {
//		    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
//
//		    switch(requestCode) { 
//		    case SELECT_PHOTO:
//		        if(resultCode == RESULT_OK){  
//		            Uri selectedImage = imageReturnedIntent.getData();
//		            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//		            Cursor cursor = getContentResolver().query(
//		                               selectedImage, filePathColumn, null, null, null);
//		            cursor.moveToFirst();
//
//		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//		            String filePath = cursor.getString(columnIndex);
//		            cursor.close();
//		            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
//		           
//		            
//		        }
//		    }
//		}

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
