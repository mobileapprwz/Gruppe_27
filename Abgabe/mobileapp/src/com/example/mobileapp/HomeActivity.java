package com.example.mobileapp;

import java.util.Vector;

import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.categories.CategoryListAdapter;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.mobileapp.events.EventListActivity;
import com.example.prefs.SettingsActivity;
import com.example.prefs.ProfileSettingsFragment;

import datastructures.Event;
import datastructures.Favorite;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {

    IntentFilter m_intent_filter;
    GridView m_gridview;
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
		setContentView(R.layout.activity_home);
	    
    	//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
		
		m_sp = PreferenceManager.getDefaultSharedPreferences(this);	
	}
	
	SharedPreferences m_sp;
    Menu m_menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		m_menu = menu;
		menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
		m_menu.findItem(R.id.action_home).setVisible(false);
		m_menu.findItem(R.id.action_browse).setVisible(false);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		switch (item.getItemId()) {

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
	    case R.id.action_new_event:
	    	myIntent = new Intent(this, EditEventActivity.class);
	    	myIntent.putExtra("new", true);
	    	this.startActivity(myIntent);
	    	return true;
		}
		return false;
	}
	
	
    @Override
    public void onResume() {
    	if(m_menu != null)
    	{
    		m_menu.findItem(R.id.action_name).setTitle(m_sp.getString("pref_key_name", ""));
    	}
    	
	    // Favorites GridView
		m_gridview = (GridView) findViewById(R.id.home_grid_favorites);
	    final BaseAdapter adapter = new HomeGridAdapter(this, getResources().obtainTypedArray(R.array.subcategory_icons));
	    m_gridview.setAdapter(adapter);

	    m_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Favorite favorite = (Favorite) adapter.getItem(position);
				new HomeActivityAT().execute(3, favorite.getId());
			}
	    });
    	
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(m_broadcast_receiver);
    }
    
    public void viewCategories(View v)
    {
    	new HomeActivityAT().execute(1);
    }
    
    public void viewMyEvents(View v)
    {
    	new HomeActivityAT().execute(2);
    }
    
    class HomeActivityAT extends AsyncTask<Integer, String, Integer>
    {
    	ProgressDialog progress_dialog;
    	Vector<Event> events;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(HomeActivity.this, "Loading", getApplicationContext().getResources().getString(R.string.toast_loading), true);
            //do initialization of required objects objects here
        }

        protected Integer doInBackground(Integer... args)
        {
        	switch(args[0])
        	{
        	case 1:
        		//Browse button click
        		SessionData.instance().updateCategories();
        		return 1;
        	case 2:
        		events = SessionData.instance().getEventsByUserId(UserSession.instance().getUser().getId());
        		return 2;
        	case 3:
        		events = SessionData.instance().getEventsByCategroyId(args[1]);
        		return 3;
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
        		Intent browse_intent = new Intent(HomeActivity.this, CategoriesActivity.class);
            	startActivity(browse_intent);
            	break;
        	case 2:
        		if(events != null)
        		{
	        		Intent myevent_intent = new Intent(HomeActivity.this, EventListActivity.class);
	        		UserSession.instance().setMy_events(events);
	        		myevent_intent.putExtra("case", 1);
	            	startActivity(myevent_intent);
        		}
        		else
        		{
        			Toast.makeText(HomeActivity.this, getApplicationContext().getResources().getString(R.string.toast_no_events), Toast.LENGTH_SHORT).show();
        		}
        		break;
        	case 3:
        		if(events != null)
        		{
        			SessionData.instance().setTemp_events(events);
    				Intent intent = new Intent(HomeActivity.this, EventListActivity.class);
    				intent.putExtra("case", 0);
    				startActivity(intent);
        		}
        		else
        		{
        			Toast.makeText(HomeActivity.this, getApplicationContext().getResources().getString(R.string.toast_no_events), Toast.LENGTH_SHORT).show();
        		}
            	break;
        	}
        }
    }
}
