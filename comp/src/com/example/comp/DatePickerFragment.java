package com.example.comp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
	
	//Declare the interface to pass data between fragment and activity
	public interface OnDataPass {
		public void onDataPass(Calendar calendar);
	}
	
	OnDataPass dataPasser;
	
	//Connect the OnDataPass interface implementation to the fragment 
	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    dataPasser = (OnDataPass) a;
	}
	
	//Function to handle passing of data
	public void passData(Calendar calendar) {
	    dataPasser.onDataPass(calendar);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		Calendar calendar = new GregorianCalendar(year, month, day);
		passData(calendar);
	}
}
