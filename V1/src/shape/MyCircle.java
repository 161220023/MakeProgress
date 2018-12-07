package shape;

import java.awt.Color;
import java.awt.Graphics;

public class MyCircle extends MyShape{
	
	public MyCircle() {
		
	}

	public MyCircle(MyPoint point0, int r, int pounds, Color color){
		parameter.add(point0);
		parameter.add(r);
		parameter.add(pounds);
		parameter.add(color);
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		int x0=point0.x;
		int y0=point0.y;
		int r=(int)parameter.get(1);
		r*=r;
		x=(x-x0)*(x-x0);
		y=(y-y0)*(y-y0);
		return x+y<=r;
	}
	
	public void changer(int r) {
		parameter.remove(1);
		parameter.add(1, r);
	}
	
	@Override
	public void move(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		point0.x+=x;
		point0.y+=y;
	}
	
	private void changer(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		int r=(int)Math.sqrt((point0.x-x)*(point0.x-x)+(point0.y-y)*(point0.y-y));
		changer(r);
	}
	
	public boolean inleftup(int x, int y) {//判断点(x,y)是否在圆的左上部分
		MyPoint point0=(MyPoint)parameter.get(0);
		int r=(int)parameter.get(1);
		int x0=point0.x;
		int y0=point0.y;
		return x<=x0&&y<=y0&&Math.abs((x-x0)*(x-x0)+(y-y0)*(y-y0)-r*r)<=100;
	}
	
	public boolean inrightup(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		int r=(int)parameter.get(1);
		int x0=point0.x;
		int y0=point0.y;
		return x>=x0&&y<=y0&&Math.abs((x-x0)*(x-x0)+(y-y0)*(y-y0)-r*r)<=100;
	}
	
	public boolean inrightdown(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		int r=(int)parameter.get(1);
		int x0=point0.x;
		int y0=point0.y;
		return x>=x0&&y>=y0&&Math.abs((x-x0)*(x-x0)+(y-y0)*(y-y0)-r*r)<=100;
	}
	
	public boolean inleftdown(int x, int y) {
		MyPoint point0=(MyPoint)parameter.get(0);
		int r=(int)parameter.get(1);
		int x0=point0.x;
		int y0=point0.y;
		return x<=x0&&y>=y0&&Math.abs((x-x0)*(x-x0)+(y-y0)*(y-y0)-r*r)<=100;
	}
	
	@Override
	public void leftupexpansion(int minx, int miny) {
		changer(minx, miny);
	}
	
	@Override
	public void rightupexpansion(int maxx, int miny) {
		changer(maxx,miny);
	}
	
	@Override
	public void rightdownexpansion(int maxx, int maxy) {
		changer(maxx,maxy);
	}
	
	@Override
	public void leftdownexpansion(int minx, int maxy) {
		changer(minx,maxy);
	}
	
	@Override
	public void changepounds(int pounds) {
		parameter.remove(2);
		parameter.add(2,pounds);
	}
	
	@Override
	public void changecolor(Color color) {
		parameter.remove(3);
		parameter.add(3,color);
	}
	
	@Override
	public void draw(Graphics g) {
		MyPoint pointmin=(MyPoint)parameter.get(0);
		int x0=pointmin.x;
		int y0=pointmin.y;
		int r=(int)parameter.get(1);
		int pounds=(int)parameter.get(2);
		Color color=(Color)parameter.get(3);
		MyShape.drawcircle(g, x0, y0, r, pounds, color);
	}
}
