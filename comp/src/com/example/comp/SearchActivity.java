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

public class SearchActivity extends Activity {
	/*private static int RESULT_LOAD_IMAGE = 1;
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" }; */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		
		
		/**
		 * load pictures
		 */
		/*Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
	    buttonLoadImage.setOnClickListener(new View.OnClickListener() {
	         
	        @Override
	        public void onClick(View arg0) {
	             
	            Intent i = new Intent(
	                    Intent.ACTION_PICK,
	                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	             
	            startActivityForResult(i, RESULT_LOAD_IMAGE);
	        }
	    });*/
		
	}
	
	public void searchBooksFunc (View view){
		Intent intent = new Intent(this, SearchBooks.class);
		startActivity(intent);
	}
	
	/*public void searchFashion(View view){
		Intent intent = new Intent(this, SearchFashion.class);
		startActivity(intent);
	}

	public void searchBedding(View view){
		Intent intent = new Intent(this, SearchBedding.class);
		startActivity(intent);
	}
 
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	     
	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	
	        Cursor cursor = getContentResolver().query(selectedImage,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();
	
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        String picturePath = cursor.getString(columnIndex);
	        cursor.close();
	         
	        ImageView imageView = (ImageView) findViewById(R.id.imgView);
	        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	     
	    }
 
 
	}*/
	
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_search,
					container, false);
			return rootView;
		}
	}*/

}