package datastructures;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {
	private Integer m_id;
	private Integer m_active;
	private String m_name;
	private Vector<SubCategory> m_subcategories;
	
	public Category(JSONObject data)
	{
		m_subcategories = new Vector<SubCategory>();
		
		try {
			setId(data.getInt("id"));
			setName(data.getString("name"));
			setActive(data.getInt("active"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addSubCategory(SubCategory m_category)
	{
		m_subcategories.add(m_category);
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public Integer getActive() {
		return m_active;
	}

	public void setActive(Integer m_active) {
		this.m_active = m_active;
	}

	public Integer getId() {
		return m_id;
	}

	public void setId(Integer m_id) {
		this.m_id = m_id;
	}

	public Vector<SubCategory> getSubcategories() {
		return m_subcategories;
	}

	public void setSubcategories(Vector<SubCategory> m_subcategories) {
		this.m_subcategories = m_subcategories;
	}
}
