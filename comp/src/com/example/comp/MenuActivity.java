package com.example.comp;


import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MenuActivity extends Activity {

	String thisUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		thisUser = getIntent().getExtras().getString("user");
		//Toast.makeText(this, thisUser, Toast.LENGTH_LONG).show();
		
	}
	
	
		/** Called when the user clicks the Buy or Borrow button */
		public void openSearchResult (View view){
			Intent intent = new Intent(this, SearchActivity.class);
			switch(view.getId()){
			case R.id.buyClick:
				intent.putExtra("menu", "buy");
				break;
			case R.id.borrowClick:
				intent.putExtra("menu", "borrow");
				break;
			}
			intent.putExtra("user", thisUser);
			startActivity(intent);
		}

		/** Called when the user clicks the Sell button */
		public void sellOption (View view){
			Intent intent = new Intent(this, SellOption.class);
			intent.putExtra("user", thisUser);
			startActivity(intent);
		}
		
		/** Called when the user clicks the Lend button */
		public void lendOption (View view){
			Intent intent = new Intent(this, LendOption.class);
			intent.putExtra("user", thisUser);
			startActivity(intent);
		}
		
		/** Called when the user clicks the Message button */
		public void showMessages (View view){
			Intent intent = new Intent(this, MessageActivity.class);
			intent.putExtra("user", thisUser);
			startActivity(intent);
		}
		

		
		//TODO: when back is pressed, directly close the app
		
		/*@Override
	    public void onBackPressed() {
	        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
	                .setMessage("Are you sure you want to exit?")
	                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        finish();
	                    }
	                }).setNegativeButton("No", null).show();
	    }*/
		
		/** Handle Action Bar Menu */
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.menu, menu);
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
