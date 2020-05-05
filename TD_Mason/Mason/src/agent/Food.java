package agent;

import model.Beings;

public class Food {
	
	private int remainFood;
	private Beings model;
	
	public Food(int nbFood) {
		this.remainFood = nbFood;
	}
	
	
	public void decreaseFood() {
		--remainFood;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRemainFood() {
		return remainFood;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	private int x;
	private int y;

	public void remove(Beings beings) {
		
		remainFood -= 1;
		System.out.println("Food remove at ["+ this.x + ", " +this.y +"]. Remain : " + remainFood);
		
		if (remainFood == 0) {
			beings.yard.remove(this);
			beings.addFood();
		}
	}
}
