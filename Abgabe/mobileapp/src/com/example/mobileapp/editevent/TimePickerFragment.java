package com.example.mobileapp.editevent;

import java.util.Calendar;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.widget.Button;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
	int buttonid;
	
	public TimePickerFragment(int id) {
		// TODO Auto-generated constructor stub
		buttonid=id;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Button time =(Button) getActivity().findViewById(buttonid);
		time.setText(hourOfDay+":"+minute);
	}

}