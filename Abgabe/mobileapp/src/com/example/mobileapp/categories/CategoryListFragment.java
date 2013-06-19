package com.example.mobileapp.categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.example.mobileapp.R;
import com.example.mobileapp.editevent.EditEventActivity;
import com.example.mobileapp.events.EventListActivity;
import com.example.mobileapp.SessionData;

import datastructures.Category;
import datastructures.Event;
import datastructures.SubCategory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

 
// Instances of this class are fragments representing a single
//object in our collection.
public class CategoryListFragment extends Fragment {
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {		 
	     View rootView = inflater.inflate(R.layout.fragment_category_list, container, false);
	     Bundle args = getArguments();
	
		 ArrayList<HashMap<String, SubCategory>> categoryList = new ArrayList<HashMap<String, SubCategory>>();
		 final ListView listview = (ListView) rootView.findViewById(R.id.cl_lv_listview);
		 
		 // Fill the ArrayList for the Adapter
		 Vector<Category> categories = SessionData.instance().getCategories();
		 
		 for(int i = 0; i < categories.size(); i++)
		 {
			 if(categories.get(i).getId() == args.getInt("Category"))
			 {
				 for(int j = 0; j < categories.get(i).getSubcategories().size(); j++)
				 {
					 HashMap<String, SubCategory> map = new HashMap<String, SubCategory>();
					 map.put("Subcategory", categories.get(i).getSubcategories().get(j));
					 
					 categoryList.add(map);
				 }
			 }
		 }
		 
		 // Adapt the list to view
		 final CategoryListAdapter adapter = new CategoryListAdapter(rootView.getContext(), categoryList);
		 listview.setAdapter(adapter);
		 listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() 
		 {
			 @Override
		 	 public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			 {
				 new CategoryListFragmentAT().execute(1, adapter.getItem(position));
			 }
		 });		 
	     return rootView;
	 }
	 
	 
	 
    class CategoryListFragmentAT extends AsyncTask<Integer, String, Integer> 
    { 
    	ProgressDialog progress_dialog;
    	Vector<Event> events;

        @Override
        protected void onPreExecute()
        {
        	super.onPreExecute();
            progress_dialog = ProgressDialog.show(getActivity(), "Loading","Retrieving event data...", true);
            //do initialization of required objects objects here                
        }     
        
        protected Integer doInBackground(Integer... args) 
        {
        	switch(args[0])
        	{
	        	case 1:
		        	Integer id = args[1];
		        	events = SessionData.instance().getEventsByCategroyId(id);
		        	return 1;
        	}
        	return 0;
        }
        
        protected void onCancelled (Integer result)
        {
        	progress_dialog.dismiss();
        }

        protected void onPostExecute(Integer result) 
        {
        	super.onPostExecute(result);
        	progress_dialog.dismiss();
        	switch(result)
        	{
	        	case 1:
		        	if(events != null)
		        	{
		        		SessionData.instance().setTemp_events(events);
						Intent myIntent = new Intent(getActivity(), EventListActivity.class);
						startActivity(myIntent);	
		        	}
		        	else
		        	{
		        		Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_events), Toast.LENGTH_SHORT).show();
		        	}
	        	break;
	        	default:
    				Toast.makeText(getActivity(), getResources().getString(R.string.toast_unable_to_db), Toast.LENGTH_SHORT).show();
        	}
    	}
    }
}

