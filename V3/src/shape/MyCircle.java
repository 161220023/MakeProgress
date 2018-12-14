package shape;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import frame.ContentPanel;

public class MyCircle extends MyShape{
	
	MyPoint point0;
	int r;
	int pounds;
	Color bordercolor;
	Color innercolor;
	
	public MyCircle() {
		
	}

	public MyCircle(MyPoint point0, int r, int pounds, Color color){//不需要旋转属性
		this.point0=point0;
		this.r=r;
		this.pounds=pounds;
		this.bordercolor=color;
	}
	
	@Override
	public void changepounds(int pounds) {
		this.pounds=pounds;
	}
	
	@Override
	public void changecolor(Color color) {
		this.bordercolor=color;
	}
	
	@Override
	void fill(Graphics g) {
		if(innercolor==null) return;
		Color precolor=g.getColor();
		g.setColor(innercolor);
		
		int x0=point0.x, y0=point0.y;
		int x=0,y=r;
		int e=1-r;
		g.drawLine(-y+x0+1, x+y0, y+x0-1, x+y0);  //(y,x)
		while(x<y) {
			if(e<0)//取右点
				e=e+x+x+1;
			else {//取右下点
				e=e+x+x-y-y+3;
				y--;
			}
			x++;
			g.drawLine(-x+x0+1, y+y0, x+x0-1, y+y0);
			g.drawLine(-x+x0+1, -y+y0, x+x0-1, -y+y0);
			g.drawLine(-y+x0+1, x+y0, y+x0-1, x+y0);
			g.drawLine(-y+x0+1, -x+y0, y+x0-1, -x+y0);
			
		}
		
		g.setColor(precolor);
	}
	
	public void fillup(Color color,Graphics g) {
		innercolor=color;
		draw(g);
	}
	
	public Cursor getCursor(int x, int y) {
		if(inleftup(x,y))
			return ContentPanel.leftupexpansion;
		else if(inrightup(x,y))
			return ContentPanel.rightupexpansion;
		else if(inrightdown(x,y))
			return ContentPanel.rightdownexpansion;
		else if(inleftdown(x,y))
			return ContentPanel.leftdownexpansion;
		else if(ininternal(x,y))
			return ContentPanel.move;
		else
			return ContentPanel.crisscross;
	}
	
	@Override
	public void changeshape(Cursor cursor, int x, int y) {
		if(cursor==ContentPanel.leftupexpansion)
			leftupexpansion(x,y);
		else if(cursor==ContentPanel.rightupexpansion)
			rightupexpansion(x,y);
		else if(cursor==ContentPanel.rightdownexpansion)
			rightdownexpansion(x,y);
		else if(cursor==ContentPanel.leftdownexpansion)
			leftdownexpansion(x,y);
	}
	
	@Override
	public boolean inboarder(int x, int y) {
		int x0=point0.x;
		int y0=point0.y;
		double d=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
		return Math.abs(d-r)<=5;
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		int x0=point0.x;
		int y0=point0.y;
		return (x-x0)*(x-x0)+(y-y0)*(y-y0)<r*r;
	}
	
	@Override
	public void move(int x, int y) {
		point0.x+=x;
		point0.y+=y;
	}
	
	public boolean inleftup(int x, int y) {//判断点(x,y)是否在圆的左上部分
		int x0=point0.x;
		int y0=point0.y;
		if(x>x0||y>y0)
			return false;
		double d=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
		return Math.abs(d-r)<=5;
	}
	
	public boolean inrightup(int x, int y) {
		int x0=point0.x;
		int y0=point0.y;
		if(x<x0||y>y0)
			return false;
		double d=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
		return Math.abs(d-r)<=5;
	}
	
	public boolean inrightdown(int x, int y) {
		int x0=point0.x;
		int y0=point0.y;
		if(x<x0||y<y0)
			return false;
		double d=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
		return Math.abs(d-r)<=5;
	}
	
	public boolean inleftdown(int x, int y) {
		int x0=point0.x;
		int y0=point0.y;
		if(x>x0||y<y0)
			return false;
		double d=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
		return Math.abs(d-r)<=5;
	}
	
	@Override
	public void leftupexpansion(int x, int y) {
		changer(x, y);
	}
	
	@Override
	public void rightupexpansion(int x, int y) {
		changer(x, y);
	}
	
	@Override
	public void rightdownexpansion(int x, int y) {
		changer(x, y);
	}	
	
	@Override
	public void leftdownexpansion(int x, int y) {
		changer(x, y);
	}

	private void changer(int x, int y) {
		int x0=point0.x, y0=point0.y;
		r=(int)(Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0)));
	}
	
	@Override
	public void draw(Graphics g) {
		int x0=point0.x;
		int y0=point0.y;
		fill(g);
		MyShape.drawcircle(g, x0, y0, r, pounds, bordercolor);
	}
}
