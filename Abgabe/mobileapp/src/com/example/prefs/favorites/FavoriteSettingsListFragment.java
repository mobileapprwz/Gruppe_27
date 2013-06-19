package com.example.prefs.favorites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.example.mobileapp.R;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.Advanceable;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

 
// Instances of this class are fragments representing a single
//object in our collection.
public class FavoriteSettingsListFragment extends Fragment {
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {		 
	     View rootView = inflater.inflate(R.layout.fragment_favorite_settings_list, container, false);
	     Bundle args = getArguments();
	
		 ArrayList<HashMap<String, SubCategory>> categoryList = new ArrayList<HashMap<String, SubCategory>>();
		 final ListView listview = (ListView) rootView.findViewById(R.id.fsl_lv_listview);
		 
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
		 final FavoriteSettingsListAdapter adapter = new FavoriteSettingsListAdapter(rootView.getContext(), categoryList);
		 listview.setAdapter(adapter);
		 listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() 
		 {
			 @Override
		 	 public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			 {
				 CheckBox check = (CheckBox) view.findViewById(R.id.fsl_checkbox); 
				 check.setChecked(!check.isChecked());
				 Integer cid = adapter.getItem(position);
				 Log.i("fav", "meh" + cid);
				 SessionData.instance().updateFav_setting_ids(cid);
			 }
		 });
	     return rootView;
	 }
}

