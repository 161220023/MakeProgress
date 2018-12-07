package shape;

import java.awt.Color;
import java.awt.Graphics;

public class MyLine extends MyShape{
	
	public MyLine() {
		
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		MyPoint point1=(MyPoint)parameter.get(0);
		MyPoint point2=(MyPoint)parameter.get(1);
		int x1=point1.x;
		int y1=point1.y;
		int x2=point2.x;
		int y2=point2.y;
		int left=(y1-y2)*x+(x2-x1)*y+x1*y2-x2*y1;
		left*=left;
		int right=(x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
		right*=25;
		return left<=right&&((x<=x1&&x>=x2)||(x>=x1&&x<=x2))&&((y<=y1&&y>=y2)||(y>=y1&&y<=y2));
	}
	
	public MyLine(MyPoint point1, MyPoint point2, int pounds, Color color) {//MyPoint(x1, y1), MyPoint(x2, y2), pounds, color
		//前四个是坐标,第5个是粗细,第六个是颜色
		parameter.add(point1);
		parameter.add(point2);
		parameter.add(pounds);
		parameter.add(color);
	}
	
	@Override
	public void move(int x, int y) {
		MyPoint point1=(MyPoint)parameter.get(0);
		MyPoint point2=(MyPoint)parameter.get(1);
		point1.x+=x;
		point1.y+=y;
		point2.x+=x;
		point2.y+=y;
	}
	
	@Override
	public void leftdownexpansion(int x, int y) {
		//改变p1
		MyPoint point=(MyPoint)parameter.get(0);
		point.x=x;
		point.y=y;
	}

	public void rightupexpansion(int x, int y) {
		//改变p2
		MyPoint point=(MyPoint)parameter.get(1);
		point.x=x;
		point.y=y;
	}
	
	@Override
	public void draw(Graphics g) {
		MyPoint point1=(MyPoint)parameter.get(0);
		MyPoint point2=(MyPoint)parameter.get(1);
		int x1=point1.x;
		int y1=point1.y;
		int x2=point2.x;
		int y2=point2.y;
		int pounds=(int)parameter.get(2);
		Color color=(Color)parameter.get(3);
		MyShape.drawline(g, x1, y1, x2, y2, pounds, color);
	}
}
