package com.example.mobileapp;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import datastructures.DatabaseManager;
import datastructures.Event;
import datastructures.Favorite;
import datastructures.User;

public class UserSession {
	private User m_user;	
	private static UserSession instance;
	private Vector<Favorite> m_favorites;
	private Vector<Event> my_events = new Vector<Event>();
	
    //Singleton Pattern
    public static UserSession instance()
    {
        if(instance == null)
        {
            instance = new UserSession();
        }
        return instance;
    }
	
    public void logout(Activity context)
    {
    	setUser(null);
    	Intent broadcastIntent = new Intent();
    	broadcastIntent.setAction("LOGOUT");
    	 
    	Intent myIntent = new Intent(context, StartActivity.class);
    	context.startActivity(myIntent);
    	
    	context.sendBroadcast(broadcastIntent);
    }
    
    public void updateFavorites()
    {
    	String query = 	"SELECT category_detail.* FROM category_detail " +
    					"LEFT JOIN favorit_category ON category_detail.id=favorit_category.id_category WHERE id_user = " +  
    					m_user.getId() + ";";
    	
    	JSONArray favorites = DatabaseManager.instance().makeHttpRequest(query);
    	m_favorites = new Vector<Favorite>();
    	
		for(int i = 0; i < favorites.length(); i++)
		{
			try{
				m_favorites.add(new Favorite(favorites.getJSONObject(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
		
	public boolean updateFavoritesOnDatabase()
	{
		Boolean[] favs = SessionData.instance().getFavSettingIds();
		
		String delete_query = "DELETE FROM favorit_category WHERE id_user = "+m_user.getId()+";";
		
		String insert_query = "INSERT INTO favorit_category (id_category, id_user) VALUES ";
		
		boolean first = true;
		
		for(int i = 0; i < favs.length; i++)
		{
			if(favs[i])
			{
				if(!first)
				{
					insert_query += ", ";
				}
				first = false;
				insert_query += "(" + i + ", " + m_user.getId() + ")"; 
			}
		}
		
		insert_query+= ";"; 
		
		Log.i("deleteq", delete_query);
		Log.i("insertq", insert_query);
		
		DatabaseManager.instance().makeHttpRequest(delete_query);
		DatabaseManager.instance().makeHttpRequest(insert_query);
		return true;
	}

	public Vector<Favorite> getFavorites() {
		return m_favorites;
	}

	public void setFavorites(Vector<Favorite> favorites) {
		this.m_favorites = favorites;
	}

	public User getUser() {
		return m_user;
	}

	public void setUser(User m_user) {
		this.m_user = m_user;
	}

	public Vector<Event> getMy_events() {
		return my_events;
	}
	
	public void addMyEvent(Event event)
	{
		if(my_events == null)
		{
			my_events = new Vector<Event>();
		}
		
		my_events.add(event);
	}
	
	public void delMyEvent(Event event)
	{
		if(my_events != null)
		{
			for(int i = 0; i < my_events.size(); i++)
			{
				if(my_events.get(i).getId() == event.getId())
				{
					my_events.remove(i);
				}
			}
		}
	}

	public void setMy_events(Vector<Event> my_events) {
		this.my_events = my_events;
	}
}
