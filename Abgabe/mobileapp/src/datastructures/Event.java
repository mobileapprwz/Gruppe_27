package datastructures;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	Integer id, cid, max_user, min_user, private_userlist, user_count;
	String name, description, location, deadline, time;
	User owner;
	Vector<User> user_list = new Vector<User>();
	public Event()
	{
	}
	
	public Event(JSONObject data)
	{
		try {
			setId(data.getInt("id"));
			setCid(data.getInt("id_category_detail"));
			setMax_user(data.getInt("max_attendant"));
			setMin_user(data.getInt("min_attendant"));
			setPrivate_userlist(data.getInt("private_userlist"));
			setName(data.getString("name"));
			setDescription(data.getString("description"));
			setLocation(data.getString("location"));
			setDeadline(data.getString("deadline"));
			setTime(data.getString("time"));
			
			if(data.has("user_count"))
			{
				setUser_count(data.getInt("user_count"));		
			}
			else
			{
				setUser_count(data.getInt("count(eul.id_event)"));
			}
			
			owner = new User();
			owner.setActive(1);
			owner.setId(data.getInt("id_user"));
			owner.setName(data.getString("u_name"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getMax_user() {
		return max_user;
	}
	public void setMax_user(Integer max_user) {
		this.max_user = max_user;
	}
	public Integer getMin_user() {
		return min_user;
	}
	public void setMin_user(Integer min_user) {
		this.min_user = min_user;
	}
	public Integer getPrivate_userlist() {
		return private_userlist;
	}
	public void setPrivate_userlist(Integer private_userlist) {
		this.private_userlist = private_userlist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


	public String getDeadline() {
		return deadline;
	}


	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
	}


	public Integer getUser_count() {
		return user_count;
	}


	public void setUser_count(Integer user_count) {
		this.user_count = user_count;
	}

	public Vector<User> getUser_list() {
		return user_list;
	}

	public void setUser_list(Vector<User> user_list) {
		this.user_list = user_list;
	}
	
	public void delUser(User user){
		for(int i = 0; i < user_list.size(); i++)
		{
			if(user.getId() == user_list.get(i).getId())
			{
				user_list.remove(i);
			}
		}
	}
	
	public void addUser(User user)
	{
		user_list.add(user);
	}
}
