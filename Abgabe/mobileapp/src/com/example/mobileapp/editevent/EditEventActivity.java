package com.example.mobileapp.editevent;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.StartActivity;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.R.id;
import com.example.mobileapp.R.layout;
import com.example.mobileapp.R.menu;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Event;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditEventActivity extends Activity {

	Dialog edit_description_dialog;
	TextView txtv_description;
	String catid;
	Event m_event;
	boolean isnew;
	
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
		setContentView(R.layout.activity_edit_event);
		txtv_description = (TextView) findViewById(R.id.ee_txtv_description);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
        
        isnew = getIntent().getExtras().getBoolean("new", true);
        if(isnew)
        {	
        	this.setTitle("New Event");
        	m_event = new Event();
        	catid = getIntent().getExtras().getString("category");
        }
        else
        {
        	m_event = SessionData.instance().getTemp_event();
        	
        	if(m_event != null)
        	{
        		this.setTitle("Edit Event");
        		
	        	TextView title = (TextView) findViewById(R.id.ee_editv_title);
	        	title.setText(m_event.getName());
	        	
	        	TextView desc = (TextView) findViewById(R.id.ee_txtv_description);
	        	desc.setText(m_event.getDescription());
	
	        	TextView loc = (TextView) findViewById(R.id.ee_editv_location);
	        	loc.setText(m_event.getLocation());
        	}
        }
        setUpEditDescriptionDialog();
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	public void showEditEvent2(View v)
	{
		EditText title = (EditText) findViewById(R.id.ee_editv_title);
		EditText location = (EditText) findViewById(R.id.ee_editv_location);
		TextView description = (TextView) findViewById(R.id.ee_txtv_description);
		
		if(title.getText().length()<3){
			Toast.makeText(this, "Title is too short", Toast.LENGTH_SHORT).show();
		}
		else if(title.getText().length()>25)
		{	
			Toast.makeText(this, "Title is too long", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(location.getText().length()==0){
				Toast.makeText(this, "You must insert a location", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Intent myIntent = new Intent(v.getContext(), EditEvent2Activity.class);
				myIntent.putExtra("category", catid);
				myIntent.putExtra("title", title.getText().toString());
				myIntent.putExtra("location", location.getText().toString());
				myIntent.putExtra("description", description.getText().toString());
				
				m_event.setName(title.getText().toString());
				m_event.setLocation(location.getText().toString());
				if(isnew == true)
				{
					m_event.setCid(Integer.parseInt(catid));
					myIntent.putExtra("new", true);
				}
				else
				{
					myIntent.putExtra("new", false);
				}
				
				SessionData.instance().setTemp_event(m_event);
				
				startActivity(myIntent);
			}
		}
	}
	
	EditText edittxt_description;
	
	public void setUpEditDescriptionDialog()
	{
		edit_description_dialog = new Dialog(EditEventActivity.this);
		edit_description_dialog.setContentView(R.layout.dialog_description);
		edit_description_dialog.setTitle("Edit Description");
		Button save_button = (Button) edit_description_dialog.findViewById(R.id.dialog_btn_description_accept);
        edittxt_description = (EditText) edit_description_dialog.findViewById(R.id.dialog_edittxt_description);
        edittxt_description.setText(m_event.getDescription());
        save_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        		edit_description_dialog.dismiss();
        		m_event.setDescription(edittxt_description.getText().toString());
        		txtv_description.setText(edittxt_description.getText());
            }
        });
	}
	
	public void showEditDescriptionDialog(View v){
		edit_description_dialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		switch (item.getItemId()) {
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
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

	@Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(m_broadcast_receiver);
    }
}
