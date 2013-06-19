package com.example.mobileapp;

import java.lang.Object;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import datastructures.Category;
import datastructures.DatabaseManager;
import datastructures.Event;
import datastructures.SubCategory;
import datastructures.User;

public class SessionData {
	private static SessionData instance;
	private Vector<Category> m_categories;
	private Vector<SubCategory> sub_categories;
	private Event temp_event;
	private Vector<Event> temp_events;
	private Boolean[] favs;	
	//Singleton Pattern
    public static SessionData instance()
    {
        if(instance == null)
        {
            instance = new SessionData();
        }
        return instance;
    }
    
    /*
     * Updates all sub-categories in relation with their parent categories and event count
     */
    public void updateCategories()
    {   
    	String query_categories = "SELECT * FROM category WHERE active='1';";
    	String query_subcategories = "SELECT category_detail.*, COUNT(event.id) AS numberevents " +
    			"FROM category_detail LEFT JOIN event ON event.id_category_detail = category_detail.id" +
    			" AND category_detail.active = 1 AND event.active = 1 GROUP BY name;";
    	
		JSONArray categories = DatabaseManager.instance().makeHttpRequest(query_categories);           		
		JSONArray subcategories = DatabaseManager.instance().makeHttpRequest(query_subcategories);
    	
    	m_categories = new Vector<Category>();
    	sub_categories = new Vector<SubCategory>();
    	
    	Vector<SubCategory> subcategory_buffer = new Vector<SubCategory>();
    	
    	// Buffer subcategories
		for(int i = 0; i < subcategories.length(); i++)
		{
			try{
				subcategory_buffer.add(new SubCategory(subcategories.getJSONObject(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
		// Get categories and add their subcategories
		for(int i = 0; i < categories.length(); i++)
		{
			try{
				Category category_buffer = new Category(categories.getJSONObject(i));
				//insert subcategories
				for(int j = 0; j < subcategory_buffer.size(); j++)
				{
					if(subcategory_buffer.get(j).getPcId().equals(category_buffer.getId()))
					{
						category_buffer.addSubCategory(subcategory_buffer.get(j));
						sub_categories.add(subcategory_buffer.get(j));
					}
				}
				//add Category to Session data
				m_categories.add(category_buffer);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public Vector<Event> getEventsByCategroyId(int id)
    {
    	String query_events = "select event.*, user.u_name, COUNT(event_user_list.id) AS user_count FROM event " +
								"JOIN user ON event.id_user = user.u_id " +
								"LEFT OUTER JOIN event_user_list ON event.id = event_user_list.id_event " +
								"WHERE event.active = 1 " +
								"AND event.id_category_detail = "  + id +
								" GROUP BY event.id;";

    	JSONArray events = DatabaseManager.instance().makeHttpRequest(query_events); 
    	return getEventsByJSONArray(events);
    }
    
    public Integer getHighestEventId()
    {
    	String query_event = "SELECT * FROM event ORDER BY id DESC LIMIT 0, 1";
    	
    	JSONArray event = DatabaseManager.instance().makeHttpRequest(query_event); 
    	Integer id = 0;
    	
    	if(event.length() > 0)
    	{
			for(int i = 0; i < event.length(); i++)
			{
				try{
					id = event.getJSONObject(i).getInt("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}    	
    	}    	
    	return id;
    }
    
    public Vector<Event> getEventsByUserId(int id)
    {
    	String query_events = "SELECT EventAlias.*, AttendantAlias.* FROM " +
    			"(SELECT e.*, u.u_name FROM event e, user u, event_user_list " +
    			"eul WHERE eul.id_event = e.id AND u.u_id = e.id_user AND " +
    			"eul.id_user = " + id +" AND active = '1' ) AS EventAlias, (SELECT count(eul.id_event), " +
    			"e.id FROM event e, event_user_list eul WHERE eul.id_event = e.id AND active = '1' " +
    			"GROUP BY eul.id_event) AS AttendantAlias WHERE EventAlias.id = AttendantAlias.id";
    	
    	JSONArray events = DatabaseManager.instance().makeHttpRequest(query_events);     	
    	return getEventsByJSONArray(events);
    }
    
    public Vector<Event> getEventsByJSONArray(JSONArray events)
    {
    	Vector<Event> v_events = null;
    	if(events.length() > 0)
    	{
    		v_events = new Vector<Event>();
			for(int i = 0; i < events.length(); i++)
			{
				try{
					v_events.add(new Event(events.getJSONObject(i)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}    	
    	}
    	return v_events;
    }
    
    public void submitEvent(Event event)
    {
    	String insert = "INSERT INTO event(`name`, `description`, " +
    			"`location`, `time`, `id_category_detail`, `id_user`, " +
    			"`max_attendant`, `deadline`, `min_attendant`, `private_userlist`, " +
    			"`active`) VALUES('" + pQ(event.getName()) + "', '" + pQ(event.getDescription()) + "', '" + 
    			pQ(event.getLocation()) + "', '" + event.getTime() + "', '" + event.getCid() + "', '" +
    			event.getOwner().getId() + "', '" + event.getMax_user() + "', '" + event.getDeadline() +
    			"', '" + event.getMin_user() + "', '" + event.getPrivate_userlist() + "', '" + 1 + "');";
    	
    	DatabaseManager.instance().makeHttpRequest(insert);
    }    
    
    //Prepare Query
    public String pQ(String str)
    {
    	return str.replaceAll("[^A-Za-z0-9\\.\\,\\(\\)\\-\\ \\:\\_]", "");
    }
    
    public void updateEvent(Event event)
    {    	
    	String update = "UPDATE event SET name='" + pQ(event.getName()) + "',  description='" + pQ(event.getDescription()) + "', " +
    			"location='" + pQ(event.getLocation()) + "', time='" + event.getTime() + "', id_category_detail='" + event.getCid() + "', " +
    			"id_user='" + event.getOwner().getId() + "', max_attendant='" + event.getMax_user() + "', deadline='" + event.getDeadline() + "', " +
    			"min_attendant='" + event.getMin_user() + "', private_userlist='" + event.getPrivate_userlist() + "' " +
    			"WHERE id='" + event.getId() + "';";
    	
    	DatabaseManager.instance().makeHttpRequest(update);
    }
    
    public void deleteEventById(int id)
    {
    	String delete_event = "UPDATE event SET active = 0 WHERE id = " + id + ";";
    	DatabaseManager.instance().makeHttpRequest(delete_event);
    }
    
    public void getUserlistByEvent(Event event)
    {
    	String user = "SELECT * FROM user JOIN event_user_list ON user.u_id = event_user_list.id_user " +
    			"WHERE event_user_list.id_event = " + event.getId() + ";";
    	
    	Vector<User> u_list = new Vector<User>();
    	
    	JSONArray event_user_list = DatabaseManager.instance().makeHttpRequest(user); 
    	
    	for(int i = 0; i < event_user_list.length(); i++)
    	{
			try{
				JSONObject row = event_user_list.getJSONObject(i);
				User row_u = new User(row.getInt("u_id"), row.getString("u_name"), "", row.getInt("u_active"));
				u_list.add(row_u);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	}
    	event.setUser_list(u_list);
    }
    
    public boolean signIn(Event event)
    {
    	String query_signin = "INSERT INTO event_user_list(`id_event`, `id_user`) " +
    			"VALUES('" + event.getId() + "', '" + UserSession.instance().getUser().getId() + "');";
    	
    	DatabaseManager.instance().makeHttpRequest(query_signin);
    	event.setUser_count(event.getUser_count() + 1);
		return true;
    }
    
    public boolean signOut(Event event)
    {
    	String query_signout = "DELETE FROM event_user_list WHERE " +
    			"id_event = " + event.getId() + " AND id_user = " + 
    			UserSession.instance().getUser().getId() + ";";
    	
    	DatabaseManager.instance().makeHttpRequest(query_signout);
    	event.setUser_count(event.getUser_count() - 1);
		return true;
    }
    
    public void addCategory(Category category)
    {
    	m_categories.add(category);
    }
	
    public Vector<Category> getCategories() {
		return m_categories;
	}

	public void setCategories(Vector<Category> m_categories) {
		this.m_categories = m_categories;
	}

	public Event getTemp_event() {
		return temp_event;
	}

	public void setTemp_event(Event temp_event) {
		this.temp_event = temp_event;
	}

	public Vector<SubCategory> getSub_categories() {
		return sub_categories;
	}

	public void setSub_categories(Vector<SubCategory> sub_categories) {
		this.sub_categories = sub_categories;
	}

	public Vector<Event> getTemp_events() {
		return temp_events;
	}

	public void setTemp_events(Vector<Event> temp_events) {
		this.temp_events = temp_events;
	}

	public Boolean[] getFavSettingIds() {
		return this.favs;
	}

	public void setFavSettingIds(Boolean[] favs) {
		this.favs = favs;
	}
	
	public void updateFav_setting_ids(int id)
	{
		favs[id] = !favs[id];
	}
}
