package com.example.mobileapp.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.RegisterActivity;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.R.layout;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.editevent.EditEvent2Activity;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.SessionData;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Event;
import datastructures.SubCategory;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailActivity extends FragmentActivity {

	Event m_event;
	Dialog m_ul_dialog;
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
		setContentView(R.layout.activity_event_detail);
		
		m_event = SessionData.instance().getTemp_event();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		/*
		 * fill text fields
		 */
	    TextView title = (TextView) findViewById(R.id.ed_txtv_title);
	    title.setText(m_event.getName());
	    //TODO creator
	    TextView creator = (TextView) findViewById(R.id.ed_txtv_creator);
	    creator.setText(m_event.getOwner().getName());
	    
	    ImageView imgview= (ImageView) findViewById(R.id.ed_imgv_icon);
	    
	    TextView category = (TextView) findViewById(R.id.ed_txtv_category);
	    Vector<SubCategory> sub_categories = SessionData.instance().getSub_categories();
	    for(int i = 0; i < sub_categories.size(); i++)
	    {
	    	if(sub_categories.get(i).getId() == m_event.getCid())
	    	{
	    		imgview.setImageResource(this.getResources().obtainTypedArray(R.array.subcategory_icons).getResourceId(sub_categories.get(i).getId(), R.drawable.other_34));
	    		category.setText(sub_categories.get(i).getName());
	    	}
	    }
	    
	    TextView description = (TextView) findViewById(R.id.ed_txtv_description);
	    description.setText(m_event.getDescription());
	    
	    TextView deadline = (TextView) findViewById(R.id.ed_txtv_deadline);
	    TextView time = (TextView) findViewById(R.id.ed_txtv_stated_time);

		try {
			Date date_dlf, date;
			date_dlf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m_event.getDeadline());
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m_event.getTime());
		    deadline.setText(date_dlf.toGMTString());
		    time.setText(date.toGMTString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    TextView location = (TextView) findViewById(R.id.ed_txtv_location);
	    location.setText(m_event.getLocation());
	    
	    TextView minmax = (TextView) findViewById(R.id.ed_txtv_currentattendance);
	    minmax.setText(m_event.getUser_count() + " / " + m_event.getMax_user());
	    
		if(UserSession.instance().getMy_events() != null)
		{
			for(int i = 0; i < UserSession.instance().getMy_events().size(); i++)
			{
				if(UserSession.instance().getMy_events().get(i).getId() == m_event.getId())
				{
				    Button btn_signout = (Button) findViewById(R.id.ed_btn_sign_off);
				    btn_signout.setVisibility(View.VISIBLE);
				    Button btn_signin = (Button) findViewById(R.id.ed_btn_sign_in);
				    btn_signin.setVisibility(View.INVISIBLE);
				    
				    if(m_event.getOwner().getId() == UserSession.instance().getUser().getId()){
				    	btn_signout.setVisibility(View.INVISIBLE);
				    }
				}
			}
		}
		
		setUpULDialog();
		
    	//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
	}

	public void setUpULDialog()
	{
		m_ul_dialog = new Dialog(EventDetailActivity.this);
		m_ul_dialog.setContentView(R.layout.dialog_user_list);
		m_ul_dialog.setTitle("Userlist");
		
        Button accept_button = (Button) m_ul_dialog.findViewById(R.id.ul_accepts);
        accept_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	m_ul_dialog.dismiss();
            }
        });
	}
	
	public void signIn(View v)
	{
		Date date_dlf = null;
		try {
			
			date_dlf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m_event.getDeadline());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Calendar actual = Calendar.getInstance();
		Date today = actual.getTime();
		
		if(m_event.getUser_count() < m_event.getMax_user())
		{
			if(today.compareTo(date_dlf) < 0)
			{
				boolean signin = true;
				if(UserSession.instance().getMy_events() != null)
				{
					for(int i = 0; i < UserSession.instance().getMy_events().size(); i++)
					{
						if(UserSession.instance().getMy_events().get(i).getId() == m_event.getId())
						{
							
							Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_already_joined), Toast.LENGTH_SHORT).show();
							signin = false;
							break;
						}
					}
				}
				if(signin)
				{
					new EventDetailActivityAT().execute(1);
				}
			}
			else
			{
				Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_deadline_expired), Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_max_user_count), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void signOff(View v)
	{
		//TODO check if deadline is reached
		new EventDetailActivityAT().execute(2);
	}
	
    class EventDetailActivityAT extends AsyncTask<Integer, String, Integer> 
    { 
    	ProgressDialog progress_dialog;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(EventDetailActivity.this, "Loading",getApplicationContext().getResources().getString(R.string.toast_sending), true);
            //do initialization of required objects objects here                
        }     
        
        protected Integer doInBackground(Integer... args) 
        {
        	switch(args[0])
        	{
        	case 1:
        		//Signin
        		SessionData.instance().signIn(m_event);
        		//UserSession.instance().addMyEvent(m_event);
        		return 1;
        	case 2:
        		//Signoff
        		SessionData.instance().signOut(m_event);
        		//UserSession.instance().delMyEvent(m_event);
        		return 2;
        	case 3:
        		//Menu user list
        		SessionData.instance().getUserlistByEvent(m_event);
        		return 3;
        	case 4:
        		//Delete event
        		SessionData.instance().deleteEventById(m_event.getId());
        		return 4;
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
        	TextView minmax = (TextView) findViewById(R.id.ed_txtv_currentattendance);
        	Button btn_signout = (Button) findViewById(R.id.ed_btn_sign_off);
        	Button btn_signin = (Button) findViewById(R.id.ed_btn_sign_in);
        	switch(result_code)
        	{
        	case 1:
        		Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_log_in), Toast.LENGTH_SHORT).show();		
            	minmax.setText(m_event.getUser_count() + " / " + m_event.getMax_user());
            	btn_signout.setVisibility(View.VISIBLE);
            	btn_signin.setVisibility(View.INVISIBLE);
            	break;
        	case 2:
        		Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_sign_off), Toast.LENGTH_SHORT).show();
        		btn_signout.setVisibility(View.INVISIBLE);
        		btn_signin.setVisibility(View.VISIBLE);
        	    minmax.setText(m_event.getUser_count() + " / " + m_event.getMax_user());
            	break;
        	case 3:
        		if(m_event.getUser_list().size() > 0)
        		{
	        		m_ul_dialog.show();
	        		TextView ul_txt = (TextView) m_ul_dialog.findViewById(R.id.ul_txtv);
	        		ul_txt.setText("");
	        		for(int i = 0; i < m_event.getUser_list().size(); i++)
	        		{
	        			ul_txt.setText(ul_txt.getText() + m_event.getUser_list().get(i).getName() + "; ");
	        		}
        		}
        		else
        		{
        			Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_userlist), Toast.LENGTH_SHORT).show();
        		}
        		break;
        	case 4:
        		Toast.makeText(EventDetailActivity.this, getApplicationContext().getResources().getString(R.string.toast_event_delete), Toast.LENGTH_SHORT).show();
    	    	Intent myIntent = new Intent(EventDetailActivity.this, HomeActivity.class);
    	    	startActivity(myIntent);
    	    	finish();
        		break;
        	}
        }
    }
    
    SharedPreferences m_sp;
    Menu m_menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_menu, menu);
		m_menu = menu;
		menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
    	if(UserSession.instance().getUser().getId() != m_event.getOwner().getId())
    	{
			menu.findItem(R.id.action_delete).setVisible(false);
			menu.findItem(R.id.action_edit).setVisible(false); 
    	}
		return true;
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
	    case R.id.action_edit:
	    	SessionData.instance().setTemp_event(m_event);
	    	myIntent = new Intent(this, EditEventActivity.class);
	    	myIntent.putExtra("new", false);
	    	this.startActivity(myIntent);
	    	return true;
	    case R.id.action_delete:
	    	if(UserSession.instance().getUser().getId() == m_event.getOwner().getId())
	    	{
	    		new EventDetailActivityAT().execute(4);
	    	}
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
	    case R.id.action_userlist:
	    	new EventDetailActivityAT().execute(3);
	    	//TODO
	    	//myIntent = new Intent(this, UserlistActivity.class);
	    	//startActivity(myIntent);
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
