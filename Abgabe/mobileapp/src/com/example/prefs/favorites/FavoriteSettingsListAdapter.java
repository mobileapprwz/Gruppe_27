package com.example.prefs.favorites;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;

import datastructures.DatabaseManager;
import datastructures.SubCategory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteSettingsListAdapter extends BaseAdapter{
	  private final Context m_context;
	  private ArrayList<HashMap<String, SubCategory>> m_data;
	
	  public FavoriteSettingsListAdapter(Context context, ArrayList<HashMap<String, SubCategory>> data) {
		    m_context = context;
		    m_data = data;
	  }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = convertView;
		    if(convertView == null)
		    {
		        view = inflater.inflate(R.layout.favorite_settings_list_item, null);
		    }
		    
		    SubCategory sub_category = m_data.get(position).get("Subcategory");
		    final Integer id = sub_category.getId();
		    ImageView imageView = (ImageView) view.findViewById(R.id.fsl_imgv_icon);
	        imageView.setImageResource(view.getResources().obtainTypedArray(R.array.subcategory_icons).getResourceId(id, R.drawable.other_34));
		    
		    /*
		     * Set view elements here... (from SubCategory object)
		     */
		    TextView title = (TextView)view.findViewById(R.id.fsl_txtv_title);
		    title.setText(sub_category.getName());
		    
		    CheckBox check = (CheckBox)view.findViewById(R.id.fsl_checkbox);
		    
		    for(int i = 0; i < UserSession.instance().getFavorites().size(); i++)
		    {
			    if(UserSession.instance().getFavorites().get(i).getId() == sub_category.getId())
			    {
			    	check.setChecked(true);
			    }
		    }
		    
		    return view;
	  }
	
	  public int getCount() {
	      return m_data.size();
	  }
	
	  public Integer getItem(int position) {
		  SubCategory sub_category = (SubCategory) m_data.get(position).get("Subcategory");
	      return sub_category.getId();
	  }
	
	  public long getItemId(int position) {
	      return position;
	  }
} 