package shape;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import frame.ContentPanel;

public class MyLine extends MyShape{
	MyPoint point1,point2;
	int pounds;
	Color color;
	double angle;
	
	public MyLine(MyPoint point1, MyPoint point2, int pounds, Color color) {//MyPoint(x1, y1), MyPoint(x2, y2), pounds, color
		//前四个是坐标,第5个是粗细,第六个是颜色
		//不需要旋转属性,直接改变直线的点
		this.point1=point1;
		this.point2=point2;
		this.pounds=pounds;
		this.color=color;
	}

	@Override
	public void changepounds(int pounds) {
		this.pounds=pounds;
	}
	
	@Override
	public void changecolor(Color color) {
		this.color=color;
	}
	
	@Override
	public Cursor getCursor(int x, int y) {
		if(aroundpoint(point1.x, point1.y, x, y))
			return ContentPanel.leftupexpansion;
		else if(aroundpoint(point2.x, point2.y, x, y))
			return ContentPanel.rightdownexpansion;
		else if(ininternal(x,y))
			return ContentPanel.move;
		//还有旋转
		else
			return ContentPanel.normal;
	}
	
	@Override
	public void changeshape(Cursor cursor, int x, int y) {
		if(cursor==ContentPanel.leftupexpansion)
			changepoint1(x,y);
		else if(cursor==ContentPanel.rightdownexpansion)
			changepoint2(x,y);
		//不包括移动
		//还有旋转
	}
	
	/*@Override
	public boolean inrotate(int x, int y) {
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		
		return false;
	}*/
	
	@Override
	public boolean inboarder(int x, int y) {
		return ininternal(x, y);
	}
		
	@Override
 	public boolean ininternal(int x, int y) {
		int x1=point1.x;
		int y1=point1.y;
		int x2=point2.x;
		int y2=point2.y;
		return aroundline(x1, y1, x2, y2, x, y);
	}
	
	@Override
	public void move(int x, int y) {
		point1.x+=x;
		point1.y+=y;
		point2.x+=x;
		point2.y+=y;
	}
	
	public void changepoint1(int x, int y) {
		//改变p1
		point1.x=x;
		point1.y=y;
	}

	public void changepoint2(int x, int y) {
		//改变p2
		point2.x=x;
		point2.y=y;
	}
	
	@Override
	public void draw(Graphics g) {
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		MyShape.drawline(g, x1, y1, x2, y2, pounds, color);
	}
	
	public static boolean aroundline(int x1, int y1, int x2, int y2, int x, int y) {
		//5个像素的距离
		if(x<x1-5&&x<x2-5||x>x1+5&&x>x2+5||y<y1-5&&y<y2-5||y>y1+5&&y>y2+5)
			return false;
		int left=(y1-y2)*x+(x2-x1)*y+x1*y2-x2*y1;
		left*=left;
		int right=(x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
		right*=25;
		return left<=right;
	}
}
