package com.example.prefs.favorites;

import java.util.Arrays;
import java.util.Vector;

import com.example.mobileapp.HomeActivity;
import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;
import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.mobileapp.events.EventListActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.Category;
import datastructures.Event;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FavoriteSettingsActivity extends FragmentActivity {

	FavoritesTabAdapter mSectionsPagerAdapter;


	ViewPager mViewPager;
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
		setContentView(R.layout.activity_favorite_settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Create the adapter that will return a fragment for each of the
		// primary sections of the app.
		mSectionsPagerAdapter = new FavoritesTabAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.fs_vp_viewpager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		//get highest id to initialize fav array	~50	
		Boolean[] temp_favs = new Boolean[50];
		Arrays.fill(temp_favs, Boolean.FALSE);
		SessionData.instance().setFavSettingIds(temp_favs);
		
		for(int i = 0; i < UserSession.instance().getFavorites().size(); i++)
		{
			temp_favs[UserSession.instance().getFavorites().get(i).getId()] = true;
		}
		SessionData.instance().setFavSettingIds(temp_favs);
		
		
    	//register this activity as a receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGOUT");
        registerReceiver(m_broadcast_receiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		menu.findItem(R.id.action_name).setTitle(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_name", ""));
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

	public void updateFavorites(View v)
	{
		new FavoriteSettingsActivityAT().execute(1);
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
	
    class FavoriteSettingsActivityAT extends AsyncTask<Integer, String, Integer>
    {
    	ProgressDialog progress_dialog;
    	Vector<Event> events;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(FavoriteSettingsActivity.this, "Loading", getApplicationContext().getResources().getString(R.string.toast_loading), true);
            //do initialization of required objects objects here
        }

        protected Integer doInBackground(Integer... args)
        {
        	switch(args[0])
        	{
        	case 1:
        		//update button
        		UserSession.instance().updateFavoritesOnDatabase();
        		UserSession.instance().updateFavorites();
        		return 1;
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
        		Toast.makeText(FavoriteSettingsActivity.this, getApplicationContext().getResources().getString(R.string.toast_favorite_changed), Toast.LENGTH_SHORT).show();
            	break;
        	}
        }
    }
}

