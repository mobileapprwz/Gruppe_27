package datastructures;

import org.json.JSONException;
import org.json.JSONObject;

public class SubCategory {
	private Integer m_id, m_pc_id, m_active_events;
	private String m_name;
	
	public SubCategory(JSONObject data)
	{
		try {
			m_id = data.getInt("id");
			m_name = data.getString("name");
			m_pc_id = data.getInt("id_category");
			m_active_events = data.getInt("numberevents");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		this.m_name = name;
	}

	public Integer getId() {
		return m_id;
	}

	public void setId(Integer id) {
		this.m_id = id;
	}

	public Integer getActiveEvents() {
		return m_active_events;
	}

	public void setActiveEvents(Integer active_events) {
		this.m_active_events = active_events;
	}

	public Integer getPcId() {
		return m_pc_id;
	}

	public void setPcId(Integer pc_id) {
		this.m_pc_id = pc_id;
	}
}