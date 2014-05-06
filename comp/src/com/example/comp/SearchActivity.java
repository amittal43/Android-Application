package com.example.comp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SearchActivity extends Activity {
	/*private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Top Rated", "Games", "Movies" }; */
	
	String menu, thisUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		thisUser = getIntent().getExtras().getString("user");
		menu = this.getIntent().getExtras().getString("menu");		
	}
	
	public void onClick(View view){
		
		Intent intent;
		if (menu.matches("buy")) {
			intent = new Intent(this, SearchItem.class);
			intent.putExtra("menu", "buy");
		}
		else {
			intent = new Intent(this, SearchItemExchange.class);
			intent.putExtra("menu", "borrow");
		}
		switch(view.getId()){
			case R.id.buttonBook:
				intent.putExtra("CATEGORY", "Book");
				break;
			case R.id.buttonBedding:
				intent.putExtra("CATEGORY", "Bedding");
				break;
			case R.id.buttonSport:
				intent.putExtra("CATEGORY", "Sport");
				break;
			case R.id.buttonFashion:
				intent.putExtra("CATEGORY", "Fashion");
				break;
			case R.id.buttonCooking:
				intent.putExtra("CATEGORY", "Cooking Utensils");
				break;
			case R.id.buttonElectronics:
				intent.putExtra("CATEGORY", "Electronics");
				break;
		}
		intent.putExtra("user", thisUser);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.search, menu);
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