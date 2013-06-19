package com.example.prefs;

import com.example.mobileapp.R;
import com.example.mobileapp.StartActivity;
import com.example.mobileapp.R.xml;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;

public class ProfileSettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.profile);
        
    	//register listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
		//close activity if logout appeared previously
        if (getActivity().getIntent().getBooleanExtra("finish", false)) {
            startActivity(new Intent(getActivity(), StartActivity.class));
            getActivity().finish();
            return;
        }
        
    	//set preference summary's
    	findPreference("pref_key_name").setSummary(getPreferenceScreen().getSharedPreferences().getString("pref_key_name", ""));
    }
    
    @Override
    public void onDestroy() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    	super.onDestroy();
    }
    
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	    Preference pref = findPreference(key);

	    if(key.equals("pref_key_name"))
	    {
	    	pref.setSummary(sharedPreferences.getString(key, ""));
	    }
	}
}