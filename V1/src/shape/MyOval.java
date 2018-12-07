package shape;

import java.awt.Color;
import java.awt.Graphics;

public class MyOval extends MyShape{
	
	public MyOval() {
		
	}
	
	public MyOval(MyPoint pointmin, MyPoint pointmax, int pounds, Color color){
		/*parameter.add(minx);
		parameter.add(miny);
		parameter.add(maxx);
		parameter.add(maxy);*/
		parameter.add(pointmin);
		parameter.add(pointmax);
		parameter.add(pounds);
		parameter.add(color);
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		int minx=pointmin.x;
		int miny=pointmin.y;
		int maxx=pointmax.x;
		int maxy=pointmax.y;
		int a=(maxx-minx)/2;
		int b=(maxy-miny)/2;
		int x2=x-minx-a;
		x2*=x2;
		int y2=y-miny-b;
		y2*=y2;
		int a2=a*a;
		int b2=b*b;
		if(b2*x2+a2*y2<=a2*b2)
			return true;
		else
			return false;
	}
	
	public void move(int x, int y) {
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		pointmin.x+=x;
		pointmin.y+=y;
		pointmax.x+=x;
		pointmax.y+=y;
	}
	
	public void leftupexpansion(int minx, int miny) {
		MyPoint pointmax=(MyPoint)parameter.get(1);
		MyPoint pointmin=(MyPoint)parameter.get(0);
		if(minx>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=minx;
		if(miny>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=miny;
	}

	public void upexpansion(int miny) {
		//改变的是miny,不能超过maxy
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(miny>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=miny;
	}
	
	public void rightupexpansion(int maxx, int miny) {
		//x不能小于minx,y不能大于maxy
		//改变的是miny,maxx
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(maxx<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=maxx;
		if(miny>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=miny;
	}
	
	public void rightexpansion(int maxx) {
		//改变的是maxx,不能小于minx
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(maxx<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=maxx;
	}
	
	public void rightdownexpansion(int maxx, int maxy) {
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(maxx<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=maxx;
		if(maxy<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=maxy;
	}
	
	public void downexpansion(int maxy) {
		//改变的是maxy,不能小于miny
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(maxy<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=maxy;
	}
	
	public void leftdownexpansion(int minx, int maxy) {
		//x不能大于maxx,y不能小于miny
		//改变的是maxy,minx
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(minx>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=minx;
		if(maxy<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=maxy;
	}
	
	public void leftexpansion(int minx) {
		//改变的是minx,不能大于maxx
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		if(minx>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=minx;
	}
	
	@Override
	public void draw(Graphics g) {
		MyPoint pointmin=(MyPoint)parameter.get(0);
		MyPoint pointmax=(MyPoint)parameter.get(1);
		int minx=pointmin.x;
		int miny=pointmin.y;
		int maxx=pointmax.x;
		int maxy=pointmax.y;
		int pounds=(int)parameter.get(2);
		Color color=(Color)parameter.get(3);
		MyShape.drawoval(g, minx, miny, maxx, maxy, pounds, color);
	}
}
