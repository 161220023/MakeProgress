package shape;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import frame.ContentPanel;

public class MyOval extends MyShape{
	
	MyPoint point1, point2, point3, point4;//旋转坐标
	int pounds;
	Color bordercolor;
	Color innercolor;
	double angle;//p1和p2的中点与x轴的夹角
	
	public MyOval() {
		
	}
	
	public MyOval(MyPoint pointmin, MyPoint pointmax, int pounds, Color color){
		this.point1=pointmin;
		this.point2=new MyPoint(pointmax.x, pointmin.y);
		this.point3=pointmax;
		this.point4=new MyPoint(pointmin.x, pointmax.y);
		this.pounds=pounds;
		this.bordercolor=color;
		angle=3*pi/2;
	}
	
	@Override
	public void changepounds(int pounds) {
		this.pounds=pounds;
	}
	
	@Override
	public void changecolor(Color color) {
		this.bordercolor=color;
	}
	
	void fill(Graphics g, MyPoint pointmin, MyPoint pointmax, double theta) {
		if(innercolor==null) return;
		Color precolor=g.getColor();
		g.setColor(innercolor);
		
		int minx=pointmin.x, miny=pointmin.y;
		int maxx=pointmax.x, maxy=pointmax.y;
		double rx=((double)maxx-minx)/2;
		double ry=((double)maxy-miny)/2;
		double rx2=rx*rx;
		double ry2=ry*ry;
		double x=0,y=ry;
		double pk=ry2-rx2*ry+0.25*rx2;
		double x0=((double)maxx+minx)/2;
		double y0=((double)maxy+miny)/2;
		//顺时针旋转
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		double xcostheta;
		double xsintheta;
		double ycostheta;
		double ysintheta;
		//绕着(0,0)旋转之后再平移
		while(ry2*x<rx2*y) {
			if(pk<0)
				pk=pk+2*x*ry2+ry2;
			else {
				pk=pk+2*x*ry2+ry2-2*y*rx2+rx2;
				y=y-1;
			}
			x=x+1;
			xcostheta=x*costheta;
			xsintheta=x*sintheta;
			ycostheta=y*costheta;
			ysintheta=y*sintheta;
			//(-x,y,x,y)
			//(-x,-y,x,-y)
			g.drawLine((int)(x0-xcostheta-ysintheta+0.5), (int)(y0-xsintheta+ycostheta+0.5), (int)(x0+xcostheta-ysintheta+0.5), (int)(y0+xsintheta+ycostheta+0.5));
			g.drawLine((int)(x0-xcostheta+ysintheta+0.5), (int)(y0-xsintheta-ycostheta+0.5), (int)(x0+xcostheta+ysintheta+0.5), (int)(y0+xsintheta-ycostheta+0.5));
		}
		pk=rx2+0.25*ry2-rx2*ry2/Math.sqrt(rx2+ry2);
		while(y>0) {
			if(pk<0) {
				pk=pk+2*x*ry2+ry2-2*y*rx2+rx2;
				x++;
			}
			else
				pk=pk-2*y*rx2+rx2;
			y--;
			xcostheta=x*costheta;
			xsintheta=x*sintheta;
			ycostheta=y*costheta;
			ysintheta=y*sintheta;
			g.drawLine((int)(x0-xcostheta-ysintheta+0.5), (int)(y0-xsintheta+ycostheta+0.5), (int)(x0+xcostheta-ysintheta+0.5), (int)(y0+xsintheta+ycostheta+0.5));
			g.drawLine((int)(x0-xcostheta+ysintheta+0.5), (int)(y0-xsintheta-ycostheta+0.5), (int)(x0+xcostheta+ysintheta+0.5), (int)(y0+xsintheta-ycostheta+0.5));
		}
		
		g.setColor(precolor);
	}
	
	void fill(Graphics g) {
		if(innercolor==null) return;
		Color precolor=g.getColor();
		g.setColor(innercolor);
		
		int minx=inf,maxx=-inf,miny=inf,maxy=-inf;
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		int x4=point4.x, y4=point4.y;
		
		if(x1<minx) minx=x1;
		if(x1>maxx) maxx=x1;
		if(x2<minx) minx=x2;
		if(x2>maxx) maxx=x2;
		if(x3<minx) minx=x3;
		if(x3>maxx) maxx=x3;
		if(x4<minx) minx=x4;
		if(x4>maxx) maxx=x4;
		
		if(y1<miny) miny=y1;
		if(y1>maxy) maxy=y1;
		if(y2<miny) miny=y2;
		if(y2>maxy) maxy=y2;
		if(y3<miny) miny=y3;
		if(y3>maxy) maxy=y3;
		if(y4<miny) miny=y4;
		if(y4>maxy) maxy=y4;
		
		int i,j;
		for(i=miny+1;i<maxy;i++) {
			for(j=minx+1;j<maxx;j++) {
				if(ininternal(j,i))
					g.drawLine(j, i, j, i);
			}
		}
		
		g.setColor(precolor);
	}
	
	@Override
	public void fillup(Color color,Graphics g) {
		innercolor=color;
		draw(g);
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
		else if(inrotate(x,y,point1,point2,angle))
			return ContentPanel.rotate;
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
		else if(cursor==ContentPanel.rotate) {
			//以(x,y)与(x0,y0)的夹角为angle
			int x1=point1.x, y1=point1.y;
			int x3=point3.x, y3=point3.y;
			double x0=((double)x1+x3)/2, y0=((double)y1+y3)/2;
			if(x==x0&&y==y0) return;
			double tmp=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
			double theta=Math.acos((x-x0)/tmp);
			if(y<y0) theta=2*pi-theta;
			rotate(theta);
		}
	}
	
	@Override
	public boolean inboarder(int x, int y) {
		//求出中心点,将点(x,y)逆时针旋转回去,判断点是否落在标准椭圆方程上
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		double x0=((double)x1+x3)/2;
		double y0=((double)y1+y3)/2;
		double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2;
		double b=Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))/2;
		if(b==0) return false;
		double theta=angle-3*pi/2;
		while(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		//逆时针转回去
		double afterx=x0+(x-x0)*costheta+(y-y0)*sintheta;
		double aftery=y0+(x0-x)*sintheta+(y-y0)*costheta;
		if(Math.abs(aftery-y0)>=b)
			return false;
		double tmp=a*Math.sqrt(1-(aftery-y0)*(aftery-y0)/(b*b));
		if(Math.abs(afterx-x0-tmp)<10||Math.abs(afterx-x0+tmp)<10)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		//先求将点(x,y)逆时针旋转回去的点，再判断该点有没有在标准椭圆方程内
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		double x0=((double)x1+x3)/2;
		double y0=((double)y1+y3)/2;
		double a=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/2;
		double b=Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3))/2;
		double theta=angle-3*pi/2;
		if(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		//逆时针转回去
		double afterx=x0+(x-x0)*costheta+(y-y0)*sintheta;
		double aftery=y0+(x0-x)*sintheta+(y-y0)*costheta;
		//求(afterx,aftery)是否落在椭圆内
		double d=Math.sqrt(b*b*(afterx-x0)*(afterx-x0)+a*a*(aftery-y0)*(aftery-y0));
		return d<a*b;
	}

	public void rotate(double theta) {
		while(theta>=2*pi) theta-=2*pi;
		while(theta<0) theta+=2*pi;
		//各个点顺时针旋转theta-angle
		double deltatheta=theta-angle;
		angle=theta;
		
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		int x4=point4.x, y4=point4.y;
		double x0=((double)x1+x3)/2, y0=((double)y1+y3)/2;
		double costheta=Math.cos(deltatheta);
		double sintheta=Math.sin(deltatheta);
		//顺时针旋转
		point1.x=(int)(x0+(x1-x0)*costheta+(y0-y1)*sintheta);
		point1.y=(int)(y0+(x1-x0)*sintheta+(y1-y0)*costheta);
		point2.x=(int)(x0+(x2-x0)*costheta+(y0-y2)*sintheta);
		point2.y=(int)(y0+(x2-x0)*sintheta+(y2-y0)*costheta);
		point3.x=(int)(x0+(x3-x0)*costheta+(y0-y3)*sintheta);
		point3.y=(int)(y0+(x3-x0)*sintheta+(y3-y0)*costheta);
		point4.x=(int)(x0+(x4-x0)*costheta+(y0-y4)*sintheta);
		point4.y=(int)(y0+(x4-x0)*sintheta+(y4-y0)*costheta);
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
		//fill(g);
		//逆时针转回去摆正
		double theta=angle-3*pi/2;
		if(theta<0) theta+=2*pi;
		double costheta=Math.cos(theta);
		double sintheta=Math.sin(theta);
		int x1=point1.x, y1=point1.y;
		int x3=point3.x, y3=point3.y;
		double x0=((double)x1+x3)/2;
		double y0=((double)y1+y3)/2;
		int afterx1=(int)(x0+(x1-x0)*costheta+(y1-y0)*sintheta);
		int aftery1=(int)(y0+(x0-x1)*sintheta+(y1-y0)*costheta);
		int afterx3=(int)(x0+(x3-x0)*costheta+(y3-y0)*sintheta);
		int aftery3=(int)(y0+(x0-x3)*sintheta+(y3-y0)*costheta);
		MyPoint tpoint1=new MyPoint(afterx1,aftery1);
		MyPoint tpoint3=new MyPoint(afterx3,aftery3);
		fill(g,tpoint1,tpoint3,theta);
		//fill(g);
		MyShape.drawoval(g, tpoint1, tpoint3, theta, pounds, bordercolor);
	}
}
