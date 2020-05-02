package tutorial.model;

public class Pair<T> {
	public T _1;
	public T _2;
	public Pair(T a, T b) {
		_1 = a;
		_2 = b;
	}
	public String toString() {
		return String.format("(%d,%d)",_1,_2);
	}
}
