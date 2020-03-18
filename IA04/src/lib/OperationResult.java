package lib;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OperationResult 
{
	public OperationResult()
	{
		value    = 0;
		comment  = "";
	}
	
	public OperationResult(int val, String cmt)
	{
		value    = val;
		comment  = cmt;
	}

	public String toJson()
	{
		ObjectMapper mapper = new ObjectMapper();
		String s = "";
		try 
		{
			s = mapper.writeValueAsString(this);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return s;
	}
	
	public static OperationResult read(String jsonString) 
	{
		ObjectMapper mapper = new ObjectMapper();
		OperationResult p = null;
		
		try {
			p = mapper.readValue(jsonString, OperationResult.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return p;
	}
	
	
	public String getComment()
	{
		return this.comment;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	private int value;
	private String comment;
}
