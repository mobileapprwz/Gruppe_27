package com.example.mobileapp.categories;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Category;
import datastructures.SubCategory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;


public class CategoriesActivity extends FragmentActivity implements OnMenuItemClickListener{
    CategoryTabAdapter tabadapter;

    Menu m_menu;
    ViewPager mViewPager;
    int count=10;
    
    private BroadcastReceiver m_broadcast_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOGOUT")) {
                finish();
            }
        }
    };
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        tabadapter = new CategoryTabAdapter(getSupportFragmentManager(), count);
        mViewPager = (ViewPager) findViewById(R.id.c_vp_viewpager);
        mViewPager.setAdapter(tabadapter);
        
    	//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
    }
    
    public void popupmenu(final View v) {	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		m_menu = menu;
		menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
		m_menu.findItem(R.id.action_browse).setVisible(false);
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
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}
	
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	unregisterReceiver(m_broadcast_receiver);
    }
    
}