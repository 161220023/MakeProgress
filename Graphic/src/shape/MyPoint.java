package shape;

public class MyPoint {
	public int x, y;
	public MyPoint(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public void changex(int x) {
		this.x=x;
	}
	
	public void changey(int y) {
		this.y=y;
	}
	
	public void change(int x, int y) {
		this.x=x;
		this.y=y;
	}
}
