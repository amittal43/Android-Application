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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Build;

public class LendOption extends FragmentActivity implements OnDataPass {

	private RadioGroup radioCategoryGroup;
	private RadioButton radioCategoryButton;
	private RadioGroup radioQualityGroup;
	private RadioButton radioQualityButton;
	private EditText doublePrice, stringTitle, stringDescription, showDate;
	private Calendar calendar;
	private String duedate = "", title = "", category = "", quality = "", description = "", price = "", thisUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_option);
        thisUser = getIntent().getExtras().getString("user");
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
		if (title.matches("") || description.matches("") || price.matches("") || duedate.matches("")){
			Toast.makeText(this, "Input data is not complete!", Toast.LENGTH_LONG).show();
		}
		else
		{
			double priceDouble = Double.parseDouble(price);
			Intent intent = new Intent(this, LendOptionSubmit.class);
			Bundle bundle = new Bundle();
			bundle.putString("TITLE", title);
			bundle.putString("CATEGORY", category);
			bundle.putString("QUALITY", quality);
			bundle.putString("DESCRIPTION", description);
			bundle.putDouble("PRICE", priceDouble);
			bundle.putString("DUEDATE", duedate);
			intent.putExtras(bundle);
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

}
