package src.objects;

import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.util.leap.HashMap;


/*
 * JSON object of a Cell is in form:
 * Cell : 
 * {
 * 		'value' : x,
 * 		'possibleValues' : "x1x2x3"
 * }
 */

public class Cell 
{
	public Cell()
	{
		m_value 		 = 0;
		m_possibleValues = new Vector<Integer>();
		
		for (int i = 1; i < 10; ++i)
		{
			m_possibleValues.add(i);
		}
	}
	
	public Cell(int value)
	{
		m_value = value;
		
		m_possibleValues = null;
	}
	
	public Cell(String json)
	{
		Map<String, Object> map = jsonToMap(json);
		
		m_value = (Integer) map.get("value");
		
		if (m_value > 10 || m_value < 0)
		{
			m_value = 0;
		}
		
		m_possibleValues = new Vector<Integer>();
		
		String values = (String) map.get("possibleValues");
		
		for (int i = 0; i < values.length(); ++i)
		{
			int possibleValue = values.charAt(i) - '0';
			
			if (possibleValue < 10 && possibleValue > 0)
			{
				m_possibleValues.add(possibleValue);
			}
		}
	}
	
	public Vector<Integer> getPossibleValues()
	{
		return m_possibleValues;
	}
	
	public int getValue()
	{
		return m_value;
	}
	
	public boolean updatePossbileValues(Vector<Integer> values)
	{
		if (m_possibleValues == null)
		{
			return true;
		}
		
		if (values.size() == 1)
		{
			m_value = values.elementAt(0);
			
			m_possibleValues = null;
			
			return true;
		}
	
		m_possibleValues = values;
		
		return false;
	}
	
	@SuppressWarnings("null")
	public String cellToJson()
	{
		Map<String, Object> map = null;
		
		map.put("value", m_value);
		
		String values = new String();
		
		for (int i : m_possibleValues)
		{
			values += ('0' + i);
		}
		
		map.put("possibleValues", values);
		
		return mapToJson(map);
	}
	
	static public Map<String, Object> jsonToMap(String json)
	{
		Map<String, Object> map = null;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try 
		{
			map = mapper.readValue(json, Map.class);
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
		
		return map;
	}
	
	static public String mapToJson(Map<String, Object> map)
	{	
		ObjectMapper mapper = new ObjectMapper();
		
		String json = new String();
		
		try
		{
			json = mapper.writeValueAsString(map);
			
			System.out.println(json);
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
		
		return json;
	}
	
	private int 			m_value;
	
	private Vector<Integer> m_possibleValues;
}
