package com.example.prefs;

import java.util.List;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.RegisterActivity;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.StartActivity;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventDetailActivity;


import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
    	//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
        
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}
	
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.headers, target);
    }
    
    
    Menu m_menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		m_menu = menu;

		m_menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
		m_menu.findItem(R.id.action_settings).setVisible(false);
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
	    case R.id.action_logout:
	        UserSession.instance().logout(this);
	        return true;
		}
		return false;
	}
	
    @Override
    public void onResume() {
    	super.onResume(); 
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onDestroy(){
    	unregisterReceiver(m_broadcast_receiver);
    	PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    	super.onDestroy();
    }
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	    if(key.equals("pref_key_name"))
	    {
	    	m_menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString(key, ""));
	    }
	}
	
    private BroadcastReceiver m_broadcast_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOGOUT")) {
                finish();
            }
        }
    };
    
    
}