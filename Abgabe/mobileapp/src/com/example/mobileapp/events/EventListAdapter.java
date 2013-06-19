package com.example.mobileapp.events;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.mobileapp.R;
import com.example.mobileapp.R.id;
import com.example.mobileapp.R.layout;

import datastructures.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter{
  private final Context context;
  private ArrayList<HashMap<String, Event>> data;

  public EventListAdapter(Context context, ArrayList<HashMap<String, Event>> eventList) {
    this.context = context;
    this.data = eventList;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view=convertView;
    if(convertView==null)
    {
        view = inflater.inflate(R.layout.event_list_item, null);
    }
    
    /*
     * Get required data for the view here... (from event object)
     */
    Event event = data.get(position).get("Event");
    
    TextView title = (TextView)view.findViewById(R.id.el_txtv_title);
    title.setText(event.getName());
    
    TextView min_max = (TextView)view.findViewById(R.id.el_txtv_currentattendance);
    min_max.setText(event.getUser_count() +  " / " + event.getMax_user());
    if(event.getUser_count()==event.getMax_user()){
    	min_max.setTextColor(0xFFCD3333);
    }
    else if (event.getUser_count()>event.getMin_user()){
    	min_max.setTextColor(0xFF228B22);
    }
    else{
    	min_max.setTextColor(0xFFFFC125);
    }
    
    TextView creator = (TextView)view.findViewById(R.id.el_txtv_creator);
    creator.setText(event.getOwner().getName());
    /*
     * ----------------------------------------------------------
     */
    return view;
  }

  public int getCount() {
      return data.size();
  }

  public Event getItem(int position) {
      return data.get(position).get("Event");
  }

  public long getItemId(int position) {
      return position;
  }
} 