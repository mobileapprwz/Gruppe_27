package com.example.mobileapp.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Event;

import android.app.Activity;
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
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;


public class EventListActivity extends Activity implements OnMenuItemClickListener {
	
	Context m_context;
	 private BroadcastReceiver m_broadcast_receiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (intent.getAction().equals("LOGOUT")) {
	                finish();
	            }
	        }
	    };
	
	  ArrayList<HashMap<String, Event>> m_event_list;
	  ListView m_list_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_event_list);
	  
	  m_context = this;
	  
	  m_event_list = new ArrayList<HashMap<String, Event>>();
	  m_list_view = (ListView) findViewById(R.id.el_lv_listview);
	  
	  Intent intent = getIntent();
	  Integer id = intent.getIntExtra("case", 0);
	  getActionBar().setDisplayHomeAsUpEnabled(true);
	  Vector<Event> events = null;
	  
	  if(id == 0)
	  {
		  events = SessionData.instance().getTemp_events();
	  }
	  else
	  {
		  events = UserSession.instance().getMy_events();
	  }

	  // get event data by id
  	  if(events != null)
	  {
		  for (int i = 0; i < events.size(); ++i) 
		  {
			  HashMap<String, Event> map = new HashMap<String, Event>();
			  map.put("Event", events.get(i));
			  m_event_list.add(map);
		  }
	  }
	  
	  EventListAdapter adapter = new EventListAdapter(this, m_event_list);
	  m_list_view.setAdapter(adapter);
	  m_list_view.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			  Intent myIntent = new Intent(view.getContext(), EventDetailActivity.class);
			  SessionData.instance().setTemp_event(m_event_list.get(position).get("Event"));
			  startActivity(myIntent);	
		  }
	  });
	  
  		//register this activity as a receiver
	    IntentFilter filter = new IntentFilter();
	    filter.addAction("LOGOUT");
	    registerReceiver(m_broadcast_receiver, filter);
    }
	  
	  public boolean onMenuItemClick(MenuItem item) {
		return false;
	  }

    class EventListActivityAT extends AsyncTask<Integer, String, Vector<Event>> 
    { 
    	ProgressDialog progress_dialog;
    	Vector<Event> events;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(EventListActivity.this, "Loading",getApplicationContext().getResources().getString(R.string.toast_get_event), true);
            //do initialization of required objects objects here                
        }     
        
        protected Vector<Event> doInBackground(Integer... args) 
        {
        	Integer id = args[0];
        	return SessionData.instance().getEventsByCategroyId(id);
        }
        
        protected void onCancelled (Vector<Event> result)
        {
        	progress_dialog.dismiss();
        }

        protected void onPostExecute(Vector<Event> result) 
        {
        	super.onPostExecute(result);
        	progress_dialog.dismiss();
        	
      	  if(result != null)
    	  {
    		  for (int i = 0; i < result.size(); ++i) 
    		  {
    			  HashMap<String, Event> map = new HashMap<String, Event>();
    			  map.put("Event", result.get(i));
    			  m_event_list.add(map);
    		  }
    	  }
    	  
    	  EventListAdapter adapter = new EventListAdapter(m_context, m_event_list);
    	  m_list_view.setAdapter(adapter);
    	  m_list_view.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
    		  @Override
    		  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    			  Intent myIntent = new Intent(view.getContext(), EventDetailActivity.class);
    			  SessionData.instance().setTemp_event(m_event_list.get(position).get("Event"));
    			  startActivity(myIntent);	
    		  }
    	  });
        }
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
