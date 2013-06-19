package datastructures;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobileapp.SessionData;
import com.example.mobileapp.UserSession;


public class User {
	private String m_name, m_password;
	private Integer m_id;
	private int m_active;
	
	public User(JSONArray data)
	{
		try {
			JSONObject user_set = data.getJSONObject(0);
			m_id = user_set.getInt("u_id");
			m_name = user_set.getString("u_name");
			m_password = user_set.getString("u_pw");
			m_active = user_set.getInt("u_active");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public User()
	{
		m_name = "unknown";
		m_password = "unknown";
		m_id = 0;
		m_active = 0;
	}
	
	public User(int id, String name, String password, int active)
	{
		m_name = name;
		m_password = "";
		m_id = id;
		m_active = active;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public String getPassword() {
		return m_password;
	}

	public void setPassword(String m_password) {
		this.m_password = m_password;
	}

	public Integer getId() {
		return m_id;
	}

	public void setId(Integer m_id) {
		this.m_id = m_id;
	}

	public Integer getActive() {
		return m_active;
	}

	public void setActive(Integer m_active) {
		this.m_active = m_active;
	}
}
