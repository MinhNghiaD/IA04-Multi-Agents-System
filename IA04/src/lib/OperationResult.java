package lib;

import java.io.IOException;
import jade.core.AID;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OperationResult 
{
	OperationResult(int val, String cmt)
	{
		value    = v;
		comment  = c;
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
	
	public void setCustomer(AID cust)
	{
		customer = cust;
	}
	
	public void setTimeSend()
	{
		time = System.currentTimeMillis();
	}
	
	
	private int value;
	private String comment;
	AID customer;
	long time;
}
