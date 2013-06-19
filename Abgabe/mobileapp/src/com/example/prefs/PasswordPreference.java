package com.example.prefs;

import org.json.JSONArray;

import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.StartActivity;
import com.example.mobileapp.UserSession;
import com.example.prefs.NamePreference.NamePreferenceAT;

import datastructures.DatabaseManager;
import datastructures.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordPreference extends DialogPreference {
	
	EditText m_reppassword, m_newpassword, m_oldpassword;
	String m_sp_name;
	Context m_context;
	
    public PasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        m_context = context;
        
        setDialogLayoutResource(R.layout.dialog_editpassword);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }
    
    /*
     * Set dialog value
     */
    @Override
    public void onBindDialogView(View view){
    	super.onBindDialogView(view);
    	m_oldpassword = (EditText) view.findViewById(R.id.pref_oldpassword);
    	m_newpassword = (EditText) view.findViewById(R.id.pref_newpassword);
    	m_reppassword = (EditText) view.findViewById(R.id.pref_reppassword);
    	
    	m_sp_name = UserSession.instance().getUser().getName();
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
        	if(m_newpassword.getText().toString().length() < 20 && m_newpassword.getText().toString().length() > 0)
        	{
        		if(m_newpassword.getText().toString().equals(m_reppassword.getText().toString()))
        		{
		            String get_userdata_query = "SELECT * FROM user WHERE u_name = '" + m_sp_name + "';";
		            String set_password_query = "UPDATE user SET u_pw='" + m_newpassword.getText() + "' WHERE u_name='" + m_sp_name + "';";
		            new PasswordPreferenceAT().execute(get_userdata_query, set_password_query);
        		}
        		else
        		{
        			Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_password_conflict), Toast.LENGTH_SHORT).show();
        		}
        	}
        	else
        	{
        		Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_valid_pw_plz), Toast.LENGTH_SHORT).show();
        	}
        }
    }
    
    class PasswordPreferenceAT extends AsyncTask<String, String, Integer> 
    { 
    	String oldpassword, newpassword, reppassword;
    	ProgressDialog progress_dialog;
        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();   
        	oldpassword = m_oldpassword.getText().toString();
        	newpassword = m_newpassword.getText().toString();
        	reppassword = m_reppassword.getText().toString();
        	
        	progress_dialog = ProgressDialog.show(m_context, "Loading", getContext().getResources().getString(R.string.toast_change_pw), true);
        }     
        
        protected Integer doInBackground(String... args) 
        {
                //get database array with: makeHttpRequest(QUERY);
        		publishProgress(getContext().getResources().getString(R.string.toast_check_old_pw));
                JSONArray userdata = DatabaseManager.instance().makeHttpRequest(args[0]);
                
                if(userdata != null && userdata.length() != 0)
                {
                	User temp_user = new User(userdata);
                	
                	if(temp_user.getPassword().equals(oldpassword))
                	{
                		publishProgress(getContext().getResources().getString(R.string.toast_change_pw));
                		DatabaseManager.instance().makeHttpRequest(args[1]);
                		return 10;
                	}
                	else
                	{
                		Log.i("Edit", getContext().getResources().getString(R.string.toast_invalid_pw));
                		return 12;
                	}
                }
                else if(userdata == null)
                {
                	Log.i("Edit", getContext().getResources().getString(R.string.toast_user_not_found));
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
        			Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_pw_change_success), Toast.LENGTH_SHORT).show();
        			break;
        		case 11:
        			Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_user_not_found), Toast.LENGTH_SHORT).show();
        			break;
        		case 12:
        			Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_invalid_pw), Toast.LENGTH_SHORT).show();
        			break;
        	} 
        	progress_dialog.dismiss();
        	super.onPostExecute(result_code);
        }
    }
}