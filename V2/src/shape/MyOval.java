package shape;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import frame.ContentPanel;

public class MyOval extends MyShape{
	
	MyPoint point1, point2, point3, point4;//旋转坐标
	int pounds;
	Color color;
	double angle;//p1和p2的中点与x轴的夹角
	
	public MyOval() {
		
	}
	
	public MyOval(MyPoint pointmin, MyPoint pointmax, int pounds, Color color){
		this.point1=pointmin;
		this.point2=new MyPoint(pointmax.x, pointmin.y);
		this.point3=pointmax;
		this.point4=new MyPoint(pointmin.x, pointmax.y);
		this.pounds=pounds;
		this.color=color;
		angle=pi/2;
	}
	
	@Override
	public void changepounds(int pounds) {
		this.pounds=pounds;
	}
	
	@Override
	public void changecolor(Color color) {
		this.color=color;
	}
	
	public Cursor getCursor(int x, int y) {
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		int x4=point4.x, y4=point4.y;
		if(aroundpoint(x1,y1,x,y))
			return ContentPanel.leftupexpansion;
		else if(aroundpoint(x2,y2,x,y))
			return ContentPanel.rightupexpansion;
		else if(aroundpoint(x3,y3,x,y))
			return ContentPanel.rightdownexpansion;
		else if(aroundpoint(x4,y4,x,y))
			return ContentPanel.leftdownexpansion;
		else if(MyLine.aroundline(x1, y1, x2, y2, x, y))
			return ContentPanel.upexpansion;
		else if(MyLine.aroundline(x2, y2, x3, y3, x, y))
			return ContentPanel.rightexpansion;
		else if(MyLine.aroundline(x3, y3, x4, y4, x, y))
			return ContentPanel.downexpansion;
		else if(MyLine.aroundline(x4, y4, x1, y1, x, y))
			return ContentPanel.leftexpansion;
		else if(ininternal(x,y))
			return ContentPanel.move;
		//还有旋转
		else
			return ContentPanel.crisscross;
	}

	public void changeshape(Cursor cursor, int x, int y) {
		if(cursor==ContentPanel.leftupexpansion)
			leftupexpansion(x,y);
		else if(cursor==ContentPanel.rightupexpansion)
			rightupexpansion(x,y);
		else if(cursor==ContentPanel.rightdownexpansion)
			rightdownexpansion(x,y);
		else if(cursor==ContentPanel.leftdownexpansion)
			leftdownexpansion(x,y);
		else if(cursor==ContentPanel.upexpansion)
			upexpansion(x,y);
		else if(cursor==ContentPanel.rightexpansion)
			rightexpansion(x,y);
		else if(cursor==ContentPanel.downexpansion)
			downexpansion(x,y);
		else if(cursor==ContentPanel.leftexpansion)
			leftexpansion(x,y);
		//还有旋转
	}
	
	@Override
	public boolean inboarder(int x, int y) {
		//求出中心点,将点(x,y)顺时针旋转回去,判断点是否落在标准椭圆方程上
		double x0=(point1.x+point3.x)/2;
		double y0=(point1.y+point3.y)/2;
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2;
		double b=Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))/2;
		if(b==0)
			return false;
		double theta=angle-pi/2;
		while(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		double afterx=x0+(x-x0)*costheta+(y0-y)*sintheta;
		double aftery=y0+(x-x0)*sintheta+(y-y0)*costheta;
		if(Math.abs(aftery-y0)>=b)
			return false;
		double tmp=a*Math.sqrt(1-(aftery-y0)*(aftery-y0)/(b*b));
		if(Math.abs(afterx-x0-tmp)<10||Math.abs(afterx-x0+tmp)<10)
			return true;
		else
			return false;
		//求(afterx,aftery)是否落在椭圆的边界
		/*double d=Math.sqrt(b*b*(afterx-x0)*(afterx-x0)+a*a*(aftery-y0)*(aftery-y0));
		System.out.println("afterx-x0="+(afterx-x0)+", aftery-y0="+(aftery-y0)+", a="+a+", b="+b);
		System.out.println("d="+d+", a*b="+a*b);
		return Math.abs(d-a*b)<=100;*/
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		//先求将点(x,y)旋转回去的点，再判断该点有没有在标准椭圆方程内
		int x0=(point1.x+point3.x)/2;
		int y0=(point1.y+point3.y)/2;
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2;
		double b=Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))/2;
		double theta=angle-pi/2;
		if(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		double afterx=x0+(x-x0)*costheta+(y0-y)*sintheta;
		double aftery=y0+(x-x0)*sintheta+(y-y0)*costheta;
		//求(afterx,aftery)是否落在椭圆内
		double d=Math.sqrt(b*b*(afterx-x0)*(afterx-x0)+a*a*(aftery-y0)*(aftery-y0));
		return d<a*b;
	}
	
	public void move(int x, int y) {
		point1.x+=x; point1.y+=y;
		point2.x+=x; point2.y+=y;
		point3.x+=x; point3.y+=y;
		point4.x+=x; point4.y+=y;
	}
	
	public void leftupexpansion(int x, int y) {
		if(MyRectangle.pointexpansion(point1, point2, point3, point4, angle, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}

	public void rightupexpansion(int x, int y) {
		if(MyRectangle.pointexpansion(point2,point1,point4,point3,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void rightdownexpansion(int x, int y) {
		if(MyRectangle.pointexpansion(point3, point4, point1, point2, angle, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void leftdownexpansion(int x, int y) {
		if(MyRectangle.pointexpansion(point4,point3,point2,point1,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void upexpansion(int x, int y) {
		if(MyRectangle.edgeexpansion(point1, point2, point3, point4, angle+pi/2, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
		
	public void rightexpansion(int x, int y) {
		MyRectangle.edgeexpansion(point2, point3, point4, point1, angle, x, y);
	}
	
	public void downexpansion(int x, int y) {
		if(MyRectangle.edgeexpansion(point3, point4, point1, point2, angle+pi/2, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void leftexpansion(int x, int y) {
		MyRectangle.edgeexpansion(point4, point1, point2, point3, angle, x, y);
	}
	
	@Override
	public void draw(Graphics g) {
		//顺时针转回去摆正
		double theta=angle-pi/2;
		if(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		int x1=point1.x, y1=point1.y;
		int x3=point3.x, y3=point3.y;
		int x0=(x1+x3)/2;
		int y0=(y1+y3)/2;
		int afterx1=(int)(x0+(x1-x0)*costheta+(y0-y1)*sintheta);
		int aftery1=(int)(y0+(x1-x0)*sintheta+(y1-y0)*costheta);
		int afterx3=(int)(x0+(x3-x0)*costheta+(y0-y3)*sintheta);
		int aftery3=(int)(y0+(x3-x0)*sintheta+(y3-y0)*costheta);
		MyPoint tpoint1=new MyPoint(afterx1,aftery1);
		MyPoint tpoint3=new MyPoint(afterx3,aftery3);
		MyShape.drawoval(g, tpoint1, tpoint3, theta, pounds, color);
	}
}
