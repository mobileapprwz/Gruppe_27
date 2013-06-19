package com.example.mobileapp.categories;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.mobileapp.R;
import com.example.mobileapp.SessionData;
import com.example.mobileapp.editevent.EditEventActivity;

import datastructures.Category;
import datastructures.DatabaseManager;
import datastructures.SubCategory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class CategoryListAdapter extends BaseAdapter{
	  private final Context m_context;
	  private ArrayList<HashMap<String, SubCategory>> m_data;
	  View view;
	  public CategoryListAdapter(Context context, ArrayList<HashMap<String, SubCategory>> data) {
		    m_context = context;
		    m_data = data;
		    
	  }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    view = convertView;
		    if(convertView == null)
		    {
		        view = inflater.inflate(R.layout.category_list_item, null);
		    }
		    
		    /*
		     * Set view elements here... (from SubCategory object)
		     */
		    SubCategory sub_category = m_data.get(position).get("Subcategory");
		    final Integer id = sub_category.getId();
		    ImageView imageView = (ImageView) view.findViewById(R.id.cl_imgv_icon);
	        imageView.setImageResource(view.getResources().obtainTypedArray(R.array.subcategory_icons).getResourceId(id, R.drawable.other_34));
	        
		    TextView title = (TextView)view.findViewById(R.id.cl_txtv_title);
		    ImageView arrow = (ImageView)view.findViewById(R.id.cl_imgv_arrow);

		    title.setText(sub_category.getName());
	        arrow.setOnClickListener(new OnClickListener() {
	            @Override
                public void onClick(final View v) {
        	    PopupMenu popup = new PopupMenu(v.getContext(), v);
        	    MenuInflater inflater = popup.getMenuInflater();
        	    
        	    inflater.inflate(R.menu.category_actions, popup.getMenu());
        	    
        	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener()
        	    {
        	           public boolean onMenuItemClick(MenuItem item)
        	           {
        		        	Intent myIntent;
        		       		switch (item.getItemId()) 
        		       		{
        			           case R.id.action_new_event:
        			        	   	myIntent = new Intent(m_context, EditEventActivity.class);
			        				myIntent.putExtra("category", id.toString());
			        				myIntent.putExtra("new", true);
			 			   	    	m_context.startActivity(myIntent);  
			 			   	    	return true;		   	    	
        		       		}
        		       		return false;
        	           }
        	    });
        	    
        	    popup.show();
                }
            });
		    TextView event_count = (TextView)view.findViewById(R.id.cl_txtv_eventcounter);
		    event_count.setText("Active events: " + sub_category.getActiveEvents());	
		
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