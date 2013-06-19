package com.example.mobileapp.editevent;

import java.util.Calendar;
import java.util.Vector;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.R.id;
import com.example.mobileapp.R.layout;
import com.example.mobileapp.R.menu;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.mobileapp.events.EventListActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Event;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public class EditEvent2Activity extends FragmentActivity {
	
	Dialog min_guests_dialog;
	Dialog max_guests_dialog;
	Button time;
	Button register_to_time;
	Button date;
	boolean isnew = true;
	Button register_to_date;
	Event m_event;

	NumberPicker npmin;
	NumberPicker npmax;
	int min_guests=0;
	int max_guests=0;
	 
	 private BroadcastReceiver m_broadcast_receiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (intent.getAction().equals("LOGOUT")) {
	                finish();
	            }
	        }
	    };
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event_2);
		time = (Button) findViewById(R.id.ee2_btn_event_time);
		date = (Button) findViewById(R.id.ee2_btn_event_date);
		register_to_time = (Button) findViewById(R.id.ee2_btn_registration_to_time);
		register_to_date = (Button) findViewById(R.id.ee2_btn_registration_to_date);
		
		// event from activity 1
		m_event = SessionData.instance().getTemp_event();
		
		// set this user as owner
		m_event.setOwner(UserSession.instance().getUser());
		
		//register this activity as a receiver
		getActionBar().setDisplayHomeAsUpEnabled(true);
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
        
		init_time();
		
		setUpNumberOfGuestsDialog();
	}

	SharedPreferences m_sp;
    Menu m_menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		m_menu = menu;
		menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
		return true;
	}
	public void setUpNumberOfGuestsDialog()
	{
		final Button minbutton = (Button) findViewById(R.id.ee2_btn_min_appeals);
		final Button maxbutton = (Button) findViewById(R.id.ee2_btn_max_appeals);
		min_guests_dialog = new Dialog(EditEvent2Activity.this);
		min_guests_dialog.setContentView(R.layout.dialog_min_guests);
		min_guests_dialog.setTitle("Minimum of Guests");
		max_guests_dialog = new Dialog(EditEvent2Activity.this);
		max_guests_dialog.setContentView(R.layout.dialog_max_guests);
		max_guests_dialog.setTitle("Maximum of Guests");
		
        Button savemin_button = (Button) min_guests_dialog.findViewById(R.id.dialog_btn_save_min_guests);
        npmin = (NumberPicker) min_guests_dialog.findViewById(R.id.dialog_picker_min_guests);
        Button savemax_button = (Button) max_guests_dialog.findViewById(R.id.dialog_btn_save_max_guests);
        npmax = (NumberPicker) max_guests_dialog.findViewById(R.id.dialog_picker_max_guests);
		OnValueChangeListener mylistener = new OnValueChangeListener(){
			@Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if(picker.getId()==npmax.getId()){
					max_guests=newVal;
					m_event.setMax_user(newVal);
				}else if(picker.getId()==npmin.getId()){
					min_guests=newVal;
					m_event.setMin_user(newVal);
				}
            }
		};
		
		npmax.setOnValueChangedListener(mylistener);   
		npmin.setOnValueChangedListener(mylistener);   
		npmin.setMinValue(0);
		npmin.setMaxValue(100);
		npmax.setMinValue(1);
		npmax.setMaxValue(100);
		
		if(!getIntent().getBooleanExtra("new", true))
		{
			isnew = false;
			minbutton.setText(m_event.getMin_user().toString());
			maxbutton.setText(m_event.getMax_user().toString());
			npmin.setValue(m_event.getMin_user());
			npmax.setValue(m_event.getMax_user());
		}
        npmax.setWrapSelectorWheel(true);
        npmin.setWrapSelectorWheel(true);
        
        savemin_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
    			npmax.setMinValue(min_guests);
    			npmax.setMaxValue(100);
	        	min_guests_dialog.dismiss();
	        	minbutton.setText(""+min_guests);
            }
        });
        savemax_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		npmin.setMaxValue(max_guests);
        		npmin.setMinValue(0);
            	max_guests_dialog.dismiss();
            	maxbutton.setText(""+max_guests);
            }
        });
	}
	
	public void minButtonclicked(View v)
    {
		min_guests_dialog.show();
    }
	
	public void maxButtonclicked(View v)
    {
		max_guests_dialog.show();
    }
	
	public void showTimePickerDialog(View v) {
	    DialogFragment timepickerfragment = new TimePickerFragment(v.getId());
	    timepickerfragment.show(getSupportFragmentManager(), "Time");
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment datepickerfragment = new DatePickerFragment(v.getId());
	    datepickerfragment.show(getSupportFragmentManager(), "Date");
	}
	
	public void showRegtoTimePickerDialog(View v) {
	    DialogFragment reg_to_timepickerfragment = new TimePickerFragment(v.getId());
	    reg_to_timepickerfragment.show(getSupportFragmentManager(), "Register to Time:");
	}
	
	public void showRegtoDatePickerDialog(View v) {
	    DialogFragment  reg_to_datepickerfragment = new DatePickerFragment(v.getId());
	    reg_to_datepickerfragment.show(getSupportFragmentManager(), "Register to Date:");
	}
	
	public void init_time(){
		final Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = c.get(Calendar.DAY_OF_MONTH);
	    int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		final Button minbutton = (Button) findViewById(R.id.ee2_btn_min_appeals);
		final Button maxbutton = (Button) findViewById(R.id.ee2_btn_max_appeals);
		final Button timebutton = (Button) findViewById(R.id.ee2_btn_event_time);
		final Button datebutton = (Button) findViewById(R.id.ee2_btn_event_date);
		final Button regtotimebutton = (Button) findViewById(R.id.ee2_btn_registration_to_time);
		final Button regtodatebutton = (Button) findViewById(R.id.ee2_btn_registration_to_date);
		datebutton.setText(year+"-"+(month+1)+"-"+day);
		regtodatebutton.setText(year+"-"+(month+1)+"-"+day);
		timebutton.setText(hour+":"+minute);
		regtotimebutton.setText(hour+":"+minute);
	}
	
	@Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(m_broadcast_receiver);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		switch (item.getItemId()) {
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        upIntent.putExtra("new", isnew);
	        NavUtils.navigateUpTo(this, upIntent);
	        return true;
	    case R.id.action_browse:
	    	myIntent = new Intent(this, CategoriesActivity.class);
	    	this.startActivity(myIntent);
	    	return true;
	    case R.id.action_home:
	    	myIntent = new Intent(this, HomeActivity.class);
	    	this.startActivity(myIntent);
	    	return true;
	    case R.id.action_name:
	    	myIntent = new Intent(this, SettingsActivity.class);
	    	myIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, ProfileSettingsFragment.class.getName());
	    	this.startActivity(myIntent);
	    	//TODO bei settings und favoritesettings finish()
	    	return true;
	    case R.id.action_settings:
	    	myIntent = new Intent(this, SettingsActivity.class);
	    	this.startActivity(myIntent);
	    	return true;
	    case R.id.action_logout:
	        UserSession.instance().logout(this);
	        return true;
		}
		return false;
	}
	
    class EditEvent2ActivityAT extends AsyncTask<Integer, String, Integer>
    {
    	ProgressDialog progress_dialog;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(EditEvent2Activity.this, "Loading", getApplicationContext().getResources().getString(R.string.toast_submit_event), true);
            //do initialization of required objects objects here
        }

        protected Integer doInBackground(Integer... args)
        {
        	switch(args[0])
        	{
        	case 1:
        		//Create new event
        		SessionData.instance().submitEvent(m_event);
        		m_event.setId(SessionData.instance().getHighestEventId());
        		SessionData.instance().signIn(m_event);
        		//UserSession.instance().addMyEvent(m_event);
        		return 1;
        	case 2:
        		//Update event
        		SessionData.instance().updateEvent(m_event);
        		return 2;
        	}
        	return 0;
        }

        protected void onCancelled (Integer result)
        {
        	progress_dialog.dismiss();
        }

        protected void onPostExecute(Integer result_code)
        {
        	super.onPostExecute(result_code);
        	progress_dialog.dismiss();
        	switch(result_code)
        	{
        	case 1:
        		Intent browse_intent = new Intent(EditEvent2Activity.this, EventDetailActivity.class);
        		SessionData.instance().setTemp_event(m_event);
            	startActivity(browse_intent);
            	break;
        	case 2:
        		Intent intent = new Intent(EditEvent2Activity.this, EventDetailActivity.class);
        		SessionData.instance().setTemp_event(m_event);
            	startActivity(intent);
            	break;
        	}
        }
    }
    
    public void saveEvent(View v) {
    	m_event.setDeadline(register_to_date.getText()+" "+register_to_time.getText()+ ":00");
    	m_event.setTime(date.getText()+" "+time.getText()+ ":00");
    	
    	if(getIntent().getBooleanExtra("new", true))
    	{
    		m_event.setUser_count(0);
    		new EditEvent2ActivityAT().execute(1);
    	}
    	else
    	{
    		new EditEvent2ActivityAT().execute(2);
    	}  	
	}
}
