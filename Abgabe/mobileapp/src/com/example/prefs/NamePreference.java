package com.example.prefs;

import org.json.JSONArray;

import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;

import datastructures.DatabaseManager;
import datastructures.User;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NamePreference extends DialogPreference {
	
	EditText m_newname, m_password;
	String m_sp_name;
	Context m_context;
	
    public NamePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        m_context = context;
        
        //Set up dialog
        this.setDialogLayoutResource(R.layout.dialog_editname);
        this.setPositiveButtonText(android.R.string.ok);
        this.setNegativeButtonText(android.R.string.cancel);
        this.setDialogIcon(null);
    }

    /*
     * Set dialog value
     */
    @Override
    public void onBindDialogView(View view){
    	super.onBindDialogView(view);
        m_newname = (EditText) view.findViewById(R.id.pref_newname);
        m_password = (EditText) view.findViewById(R.id.pref_password);
        m_newname.setText(m_sp_name);
        m_newname.setSelection(m_newname.getText().toString().length());
    }
    
    
    
    /*
     * Restore default value for this preference
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
        	m_sp_name = this.getPersistedString("");
        } else {
            // Set default state from the XML attribute
        	m_sp_name = defaultValue.toString();
            persistString(m_sp_name);
        }
        Log.i("Dialog", "setting");
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
        	if(!m_sp_name.equals(m_newname.getText().toString()))
        	{
	            String get_userdata_query = "SELECT * FROM user WHERE u_name = '" + m_sp_name + "';";
	            String get_newuser_query = "SELECT * FROM user WHERE u_name = '" + m_newname.getText() + "';";
	            String set_name_query = "UPDATE user SET u_name='" + m_newname.getText() + "' WHERE u_name='" + m_sp_name + "';";
	            new NamePreferenceAT().execute(get_userdata_query, get_newuser_query, set_name_query);
        	}
        	else
        	{
        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_nothing_to_change), Toast.LENGTH_SHORT).show();
        	}
        }
    }
    
    class NamePreferenceAT extends AsyncTask<String, String, Integer> 
    { 
    	ProgressDialog progress_dialog;
    	String password, name, newname;
        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();   
        	password = m_password.getText().toString();
        	newname = m_newname.getText().toString();
        	name = m_sp_name;
        	
        	progress_dialog = ProgressDialog.show(m_context, "Loading",getContext().getResources().getString(R.string.toast_change_username), true);
        }     
        
        protected Integer doInBackground(String... args) 
        {
            //get database array with: makeHttpRequest(QUERY);
        	publishProgress("Checking password validity...");
            JSONArray userdata = DatabaseManager.instance().makeHttpRequest(args[0]);
            
            if(userdata != null && userdata.length() != 0)
            {
            	User temp_user = new User(userdata);
            	
            	if(temp_user.getPassword().equals(password))
            	{
            		publishProgress(getContext().getResources().getString(R.string.toast_check_username));
            		JSONArray newuser = DatabaseManager.instance().makeHttpRequest(args[1]);
            		if(newuser.length() == 0)
            		{
            			// Set name
            			publishProgress(getContext().getResources().getString(R.string.toast_change_username));
            			DatabaseManager.instance().makeHttpRequest(args[2]);
            			return 10;
            		}
            		else
            		{
            			Log.i("Edit", "User with this name exists already.");
            			return 13;
            		}
            	}
            	else
            	{
            		Log.i("Edit", "Wrong password.");
            		return 12;
            	}
            }
            else if(userdata == null)
            {
            	Log.i("Edit", "Current user not found.");
            	return 11;
            }
            return 1;
        }
        
        protected void onProgressUpdate(String... values) {
	         super.onProgressUpdate(values);
	          
	         progress_dialog.setMessage(values[0]);   
       }

        protected void onPostExecute(Integer result_code) 
        {
        	switch(result_code)
        	{
        		case 10:
		        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_change_name_success), Toast.LENGTH_SHORT).show();
		        		persistString(newname);
		        		UserSession.instance().getUser().setName(newname);
		        		m_sp_name = newname;
		        		break;
        		case 11:
		        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_user_not_found), Toast.LENGTH_SHORT).show();
		        		break;
        		case 12:
		        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_invalid_pw), Toast.LENGTH_SHORT).show();
		        		break;
        		case 13:
		        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_name_conflict), Toast.LENGTH_SHORT).show();
		        		break;
        	}
        	progress_dialog.dismiss();
        	super.onPostExecute(result_code);
        }
    }
}