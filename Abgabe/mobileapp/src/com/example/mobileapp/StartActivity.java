package com.example.mobileapp;

import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;

import com.example.prefs.SettingsActivity;

import datastructures.DatabaseManager;
import datastructures.Event;
import datastructures.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends Activity{

	EditText m_username, m_password;
	Dialog m_login_dialog;
	StartActivityAT m_async_task;
	SharedPreferences m_sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		setUpLoginDialog();
		
		m_sp = PreferenceManager.getDefaultSharedPreferences(this);	
		m_username.setText(m_sp.getString("pref_key_name", "").toString());
		m_username.setSelection(m_username.getText().toString().length());
	}
	
	public void setUpLoginDialog()
	{
		m_login_dialog = new Dialog(StartActivity.this);
		m_login_dialog.setContentView(R.layout.dialog_login);
		m_login_dialog.setTitle("Login");
		
		m_username = (EditText) m_login_dialog.findViewById(R.id.login_username);
		m_password = (EditText) m_login_dialog.findViewById(R.id.login_password);
		
        Button login_button = (Button) m_login_dialog.findViewById(R.id.login_login);
        login_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        		m_login_dialog.dismiss();
        		tryLogin();
            }
        });

        Button cancel_button = (Button) m_login_dialog.findViewById(R.id.login_cancel);
        cancel_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        		m_login_dialog.dismiss();
            }
        });
	}
	

    
    public void goHome()
    {
    	Intent myIntent = new Intent(this, HomeActivity.class);
    	startActivity(myIntent);
    	finish();
    }
    
    public void viewRegister(View v)
    {
    	Log.i("ButtonClick", "Register Button");
    	Intent myIntent = new Intent(this, RegisterActivity.class);
    	startActivity(myIntent);
    }
    
    public Integer checkUserValidity(User user)
    {
    	if(user.getPassword().equals(m_password.getText().toString()))
    	{
    		Log.i("General", "Logged in.");
    		return 0;
    	}
		return 1;
    }

    public void showLoginDialog(View v)
    {
    	m_login_dialog.show();
    }
    
    public void tryLogin()
    {
    	// Prepare querys fo async task
    	String query_user = "SELECT * FROM user WHERE u_name = '" + m_username.getText() + "';";
    	
    	m_async_task = new StartActivityAT();
    	m_async_task.execute("1", query_user);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    
    @Override
    protected void onDestroy(){
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (m_async_task != null && m_async_task.getStatus() != AsyncTask.Status.FINISHED)
        	m_async_task.cancel(true);
        super.onDestroy();
    }
    
    class StartActivityAT extends AsyncTask<String, String, Integer> 
    { 
    	ProgressDialog progress_dialog;
        User temp_user;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(StartActivity.this, "Loading",getApplicationContext().getResources().getString(R.string.toast_loading)+"          ", true);
            //do initialization of required objects objects here                
        }     
        
        protected Integer doInBackground(String... args) 
        {
        	Integer db_operation = Integer.parseInt(args[0]);
        	switch(db_operation)
        	{
        		case 1:
                //get database array with: makeHttpRequest(QUERY);
        		publishProgress(getApplicationContext().getResources().getString(R.string.toast_check_data));
                JSONArray user = DatabaseManager.instance().makeHttpRequest(args[1]);
                
                if(user != null && user.length() != 0)
                {
                	temp_user = new User(user);
                	
                	if(checkUserValidity(temp_user) == 0)
                	{
                		UserSession.instance().setUser(temp_user);
                		
                		publishProgress(getApplicationContext().getResources().getString(R.string.toast_load_categories));
                		SessionData.instance().updateCategories();
                		
                		publishProgress(getApplicationContext().getResources().getString(R.string.toast_load_favs));
                		UserSession.instance().updateFavorites();
                		
                		publishProgress(getApplicationContext().getResources().getString(R.string.toast_load_events));
                		Vector<Event> my_events = SessionData.instance().getEventsByUserId(temp_user.getId());
                		UserSession.instance().setMy_events(my_events);

                		m_sp.edit().putString("pref_key_name", temp_user.getName().toString()).apply();
                		
                		Log.i("Login", "Successful login.");
                		return 10;
                	}
                	else
                	{
                		Log.i("Login", "Wrong password.");
                		return 11;
                	}
                }
                else if(user != null)
                {
                	Log.i("Login", "User not found.");
                	return 12;
                }
                break;
        	}
            return 1;
        }
        
        protected void onProgressUpdate(String... values) {
	         super.onProgressUpdate(values);
	          
	         progress_dialog.setMessage(values[0]);   
        }
        
        protected void onCancelled (Integer result)
        {
        	progress_dialog.dismiss();
        }

        protected void onPostExecute(Integer result_code) 
        {
        	super.onPostExecute(result_code);
        	progress_dialog.dismiss();
    	    switch (result_code) 
    	    {
		        case 10:
			        	goHome(); 
			        	break;
		        case 11:
			        	Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_invalid_pw), Toast.LENGTH_SHORT).show();
			        	break;
		        case 12:
			        	Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_user_not_found), Toast.LENGTH_SHORT).show();
			        	break;
			    default:
		    			Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_unable_to_db), Toast.LENGTH_SHORT).show();
    	    }
        }
    }
}
