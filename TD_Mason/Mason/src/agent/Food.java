package agent;

import model.Beings;
import sim.util.Int2D;

public class Food {
	
	private int    amount;
	Int2D 		   location;
	
	private Beings environment;
	
	public Food(int nbFood, Beings environment, Int2D location) 
	{
		this.amount      = nbFood;
		this.environment = environment;
		this.location    = location;
		
		System.out.println("Food add at ["+ location.x + ", " +location.y +"]. Stock = " + amount);
	}
	
	public int consume(int amount) 
	{	
		if (amount >= this.amount)
		{
			environment.yard.remove(this);
			environment.addFood();
			
			System.out.println("Food at ["+ location.x + ", " + location.y +"] runs out");
			
			return this.amount;
		}
		
		this.amount -= amount;
		
		System.out.println("Food at ["+ location.x + ", " +location.y +"] remains : " + this.amount);
		
		return amount;
	}
	
	public Int2D location()
	{
		return location;
	}
}
