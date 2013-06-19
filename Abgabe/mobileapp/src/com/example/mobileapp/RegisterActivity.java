package com.example.mobileapp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;

import com.example.mobileapp.categories.CategoriesActivity;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventDetailActivity;
import com.example.prefs.ProfileSettingsFragment;
import com.example.prefs.SettingsActivity;

import datastructures.DatabaseManager;
import datastructures.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText m_username, m_password, m_rep_password, m_email;
	RegisterActivityAT m_async_task;
	InputMethodManager m_keyboard_manager;
	Dialog m_agb_dialog;
	CheckBox m_checkbox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		m_username = (EditText) findViewById(R.id.reg_name);
		m_password = (EditText) findViewById(R.id.reg_password);
		m_rep_password = (EditText) findViewById(R.id.reg_reppassword);
		m_email = (EditText) findViewById(R.id.reg_email);
		m_checkbox = (CheckBox) findViewById(R.id.reg_agb);
		
		setUpAGBDialog();
		
		m_keyboard_manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	public void setUpAGBDialog()
	{
		m_agb_dialog = new Dialog(RegisterActivity.this);
		m_agb_dialog.setContentView(R.layout.dialog_agb);
		m_agb_dialog.setTitle("General Business Terms");
		
        Button accept_button = (Button) m_agb_dialog.findViewById(R.id.agb_accept);
        accept_button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        		m_agb_dialog.dismiss();
            }
        });
	}
	
    class RegisterActivityAT extends AsyncTask<String, String, Integer> 
    { 
        ProgressDialog progress_dialog;
        User temp_user;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(RegisterActivity.this,"Loading", getApplicationContext().getResources().getString(R.string.toast_sending), true);
           
            //do initialization of required objects objects here                
        }     
        
        protected Integer doInBackground(String... args) 
        {
        	Integer db_operation = Integer.parseInt(args[0]);
        	switch(db_operation)
        	{
        		case 1:
                //get database array with: makeHttpRequest(QUERY);
        		publishProgress(getApplicationContext().getResources().getString(R.string.toast_check_username));
                JSONArray json = DatabaseManager.instance().makeHttpRequest(args[1]);
                
                if(json.length() == 0)
                {
                	publishProgress(getApplicationContext().getResources().getString(R.string.toast_transfer_userdata));
                	JSONArray insert = DatabaseManager.instance().makeHttpRequest(args[2]);
                	
                	if(insert != null)
                	{
                		Log.i("Register", getApplicationContext().getResources().getString(R.string.toast_registrated_success));
                		return 10;
                	}
                	else
                	{
                		Log.i("Register", getApplicationContext().getResources().getString(R.string.toast_registrated_failed));
                		return 11;
                	}
                }
                else
                {
                	Log.i("Register",  getApplicationContext().getResources().getString(R.string.toast_name_conflict));
                	return 12;
                }
        	}
            return 1;
        }
        
        protected void onProgressUpdate(String... values) {
	         super.onProgressUpdate(values);
	          
	         progress_dialog.setMessage(values[0]);   
        }

        protected void onPostExecute(Integer result_code) 
        {
        	super.onPostExecute(result_code);
        	progress_dialog.dismiss();
        	switch(result_code)
        	{
        		case 12:
        		Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_name_conflict), Toast.LENGTH_SHORT).show();
        		m_password.setText("");
        		m_rep_password.setText("");
        		break;
        		case 10:
            	Intent myIntent = new Intent(RegisterActivity.this, StartActivity.class);
            	startActivity(myIntent);
            	finish();
            	break;
        	}
        }
    }
	
    public void viewStartCancel(View v)
    {
    	Intent myIntent = new Intent(RegisterActivity.this, StartActivity.class);
    	startActivity(myIntent);
    	finish();
    }
    
    public void viewStartSubmit(View v)
    {
    	m_keyboard_manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); ;
    	if(!m_username.getText().toString().equals(""))
    	{
        	if(!m_email.getText().toString().equals(""))
        	{
            	if(!m_password.getText().toString().equals(""))
            	{
                	if(m_rep_password.getText().toString().equals(m_password.getText().toString()))
                	{
                		if(m_checkbox.isChecked())
                		{
	                		String query1 = "SELECT * FROM user WHERE u_name = '" + m_username.getText() + "';";
	                		String query2 = "INSERT INTO user(`u_name`, `u_pw`, `u_active`) VALUES('" + m_username.getText() + "', '" + m_password.getText() + "', '1');"; 
	                		
                    		m_async_task = new RegisterActivityAT();
                    		m_async_task.execute("1", query1, query2);
	                	}

                		else
                		{
                			Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_business_terms), Toast.LENGTH_SHORT).show();
                		}
                	}
                	else
                	{
                		Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_password_conflict), Toast.LENGTH_SHORT).show();
                		m_password.setText("");
                		m_rep_password.setText("");
                	}
            	}
            	else
            	{
            		Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_insert_password), Toast.LENGTH_SHORT).show();
            	}
        	}
        	else
        	{
        		Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_insert_email), Toast.LENGTH_SHORT).show();
        	}
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.toast_insert_username), Toast.LENGTH_SHORT).show();
    	}
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		switch (item.getItemId()) {
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        NavUtils.navigateUpTo(this, upIntent);
	        return true;
		}
		return false;
	}
    
    public void onCheckboxClicked(View v)
    {
    	m_agb_dialog.show();
    }
    
    @Override
    protected void onDestroy(){
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (m_async_task != null && m_async_task.getStatus() != AsyncTask.Status.FINISHED)
        	m_async_task.cancel(true);
        super.onDestroy();
    }
}
