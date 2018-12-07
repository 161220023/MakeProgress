package shape;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import frame.ContentPanel;

public class MyRectangle extends MyShape{
	public MyPoint point1,point2,point3,point4;
	public int pounds;
	public Color color;
	public double angle;
	
	public MyRectangle() {
		
	}

	public MyRectangle(MyPoint pointmin, MyPoint pointmax, int pounds, Color color){
		point1=pointmin;
		point2=new MyPoint(pointmax.x,pointmin.y);
		point3=pointmax;
		point4=new MyPoint(pointmin.x,pointmax.y);
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
		if(aroundpoint(x1, y1, x, y))
			return ContentPanel.leftupexpansion;
		else if(aroundpoint(x2, y2, x, y))
			return ContentPanel.rightupexpansion;
		else if(aroundpoint(x3, y3, x, y))
			return ContentPanel.rightdownexpansion;
		else if(aroundpoint(x4, y4, x, y))
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
		else if(inrotate(x,y))
			return ContentPanel.rotate;
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
		else if(cursor==ContentPanel.upexpansion)
			upexpansion(x,y);
		else if(cursor==ContentPanel.rightexpansion)
			rightexpansion(x,y);
		else if(cursor==ContentPanel.downexpansion)
			downexpansion(x,y);
		else if(cursor==ContentPanel.leftexpansion)
			leftexpansion(x,y);
		else if(cursor==ContentPanel.rotate) {
			//以(x,y)与(x0,y0)的夹角为angle
			int x1=point1.x, y1=point1.y;
			int x3=point3.x, y3=point3.y;
			int x0=(x1+x3)/2, y0=(y1+y3)/2;
			if(x==x0&&y==y0) return;
			double tmp=Math.sqrt((x-x0)*(x-x0)+(y-y0)*(y-y0));
			double theta=Math.acos((x-x0)/tmp);
			if(y>y0) theta=2*pi-theta;
			rotate(theta);
		}	
	}
	
	@Override
	public boolean inrotate(int x, int y) {
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x0=(x1+x2)/2,y0=(y1+y2)/2;
		int xtmp=(int)(x0+20*Math.cos(angle));
		int ytmp=(int)(y0-20*Math.sin(angle));
		return aroundpoint(xtmp,ytmp,x,y);
	}
	
	@Override
	public boolean inboarder(int x, int y) {//判断是否在边界上,经过旋转后的边界
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		int x4=point4.x, y4=point4.y;
		if(MyLine.aroundline(x1, y1, x2, y2, x, y))	return true;
		if(MyLine.aroundline(x2, y2, x3, y3, x, y)) return true;
		if(MyLine.aroundline(x3, y3, x4, y4, x, y)) return true;
		if(MyLine.aroundline(x4, y4, x1, y1, x, y)) return true;
		return false;
	}
	
	@Override
	public boolean ininternal(int x, int y) {  //不包括边界
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		double theta1,theta2,a1,b1,c1,c3,a2,b2,c2,c4;//以p1为支点
		theta1=angle+pi/2;
		theta2=angle;
		while(theta1>=2*pi) theta1-=2*pi;
		System.out.println("theta1="+theta1);
		System.out.println("theta2="+theta2);
		
		if(Math.abs(theta1-pi/2)<1e-5||Math.abs(theta1-3*pi/2)<1e-5) {
			a1=1;
			b1=0;
		}
		else {
			b1=1;
			a1=-Math.tan(theta1);
		}
		c1=-(a1*x1+b1*y1);
		c3=-(a1*x3+b1*y3);
		if(Math.abs(theta2-pi/2)<1e-5||Math.abs(theta2-3*pi/2)<1e-5) {
			a2=1;
			b2=0;
		}
		else {
			b2=1;
			a2=-Math.tan(theta2);
		}
		c2=-(a2*x2+b2*y2);
		c4=-(a2*x1+b2*y1);
		
		double l1=a1*x+b1*y+c1;
		double l2=a2*x+b2*y+c2;
		double l3=a1*x+b1*y+c3;
		double l4=a2*x+b2*y+c4;
		System.out.println("a1="+a1+", b1="+b1+", c1="+c1+", c3="+c3);
		System.out.println("a2="+a2+", b2="+b2+", c2="+c2+", c4="+c4);
		System.out.println("l1="+l1+", l3="+l3);
		System.out.println("l2="+l2+", l4="+l4);
		
		if((l1>0&&l3<0||l1<0&&l3>0)&&(l2>0&&l4<0||l2<0&&l4>0))
			return true;
		else
			return false;
	}
	
	@Override
	public void rotate(double theta) {
		while(theta>=2*pi) theta-=2*pi;
		while(theta<0) theta+=2*pi;
		//各个点旋转theta-angle
		double deltatheta=theta-angle;
		angle=theta;
		
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x3=point3.x, y3=point3.y;
		int x4=point4.x, y4=point4.y;
		double x0=((double)x1+x3)/2, y0=((double)y1+y3)/2;
		double costheta=Math.cos(deltatheta);
		double sintheta=Math.sin(deltatheta);
		point1.x=(int)(x0+(x1-x0)*costheta+(y1-y0)*sintheta);
		point1.y=(int)(y0+(x0-x1)*sintheta+(y1-y0)*costheta);
		point2.x=(int)(x0+(x2-x0)*costheta+(y2-y0)*sintheta);
		point2.y=(int)(y0+(x0-x2)*sintheta+(y2-y0)*costheta);
		point3.x=(int)(x0+(x3-x0)*costheta+(y3-y0)*sintheta);
		point3.y=(int)(y0+(x0-x3)*sintheta+(y3-y0)*costheta);
		point4.x=(int)(x0+(x4-x0)*costheta+(y4-y0)*sintheta);
		point4.y=(int)(y0+(x0-x4)*sintheta+(y4-y0)*costheta);
	}
	
	@Override
	public void move(int x, int y) {
		point1.x+=x; point1.y+=y;
		point2.x+=x; point2.y+=y;
		point3.x+=x; point3.y+=y;
		point4.x+=x; point4.y+=y;
	}
	
	public void leftupexpansion(int x, int y) {  //改变point1的位置,point3的位置不变
		if(pointexpansion(point1,point2,point3,point4,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}

	public void rightupexpansion(int x, int y) { //改变的point2的位置
		if(pointexpansion(point2,point1,point4,point3,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void rightdownexpansion(int x, int y) {
		if(pointexpansion(point3,point4,point1,point2,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}

	public void leftdownexpansion(int x, int y) {
		if(pointexpansion(point4,point3,point2,point1,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void upexpansion(int x, int y) {
		if(edgeexpansion(point1, point2, point3, point4, angle+pi/2, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void rightexpansion(int x, int y) {
		edgeexpansion(point2, point3, point4, point1, angle, x, y);
	}
	
	public void downexpansion(int x, int y) {
		if(edgeexpansion(point3, point4, point1, point2, angle+pi/2, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
	}
	
	public void leftexpansion(int x, int y) {
		edgeexpansion(point4,point1,point2,point3,angle,x,y);
	}
	
	@Override
	public void draw(Graphics g) {
		MyShape.drawrectangle(g, point1,point2,point3,point4, pounds, color);
	}
	
	public static MyPoint intersection(double a1, double b1, double c1, double a2, double b2, double c2) {
		//直线a1x+b1y+c1=0, a2x+b2y+c2=0的交点(x在前一条直线上)
		double denominator=b1*a2-a1*b2;
		if(Math.abs(denominator)<1e-5)
			return null;
		double y=(a1*c2-c1*a2)/denominator;
		double x=0;
		if(Math.abs(a1)<1e-5)
			x=(-c2-b2*y)/a2;
		else
			x=(-c1-b1*y)/a1;
		return new MyPoint((int)x,(int)y);
	}
	
	public static boolean pointexpansion(MyPoint tpoint1, MyPoint tpoint2, MyPoint tpoint3, MyPoint tpoint4, double theta, int x, int y) {
		//改变point1的位置,point3的位置不变,求出新的point2和point4
		//返回angle是否增加pi
		//theta是point2,point3的角度
		//theta+pi/2是point3,point4的角度
		int x3=tpoint3.x;
		int y3=tpoint3.y;
		double theta1,theta2,a1,a2,b1,b2,c1,c2;
		theta1=theta;
		theta2=theta+pi/2;
		while(theta1>=2*pi) theta1-=2*pi;
		while(theta1<0) theta1+=2*pi;
		while(theta2>=2*pi) theta2-=2*pi;
		while(theta2<0) theta2+=2*pi;
		if(Math.abs(theta1-pi/2)<1e-5||Math.abs(theta1-3*pi/2)<1e-5) {
			b1=0;
			a1=1;
		}
		else {
			b1=1;
			a1=-Math.tan(theta1);
		}
		c1=-(a1*x3+b1*y3);
		if(Math.abs(theta2-pi/2)<1e-5||Math.abs(theta2-3*pi/2)<1e-5) {
			b2=0;
			a2=1;
		}
		else {
			b2=1;
			a2=-Math.tan(theta2);
		}
		c2=-(a2*x3+b2*y3);
		MyPoint tmppoint2=intersection(a1,b1,c1,a2,b2,-(a2*x+b2*y));
		MyPoint tmppoint4=intersection(a2,b2,c2,a1,b1,-(a1*x+b1*y));
		tpoint2.x=tmppoint2.x;
		tpoint2.y=tmppoint2.y;
		tpoint4.x=tmppoint4.x;
		tpoint4.y=tmppoint4.y;
		int prep1x=tpoint1.x;
		int prep1y=tpoint1.y;
		tpoint1.x=x;
		tpoint1.y=y;
		
		double tmp1=a2*prep1x+b2*prep1y+c2;
		double tmp2=a2*x+b2*y+c2;
		if(tmp1>=0&&tmp2<=0||tmp1<=0&&tmp2>=0)
			return true;
		else
			return false;
	}
	
	public static boolean edgeexpansion(MyPoint tpoint1, MyPoint tpoint2, MyPoint tpoint3, MyPoint tpoint4, double theta, int x, int y) {
		//改变p1,p2坐标
		//theta是p3,p4的斜率
		//以p3为支点
		int x3=tpoint3.x, y3=tpoint3.y;
		int x4=tpoint4.x, y4=tpoint4.y;
		double theta1,theta2,a1,a2,b1,b2,c1,c2,c3,c4;
		theta1=theta; theta2=theta+pi/2;
		while(theta1>=2*pi) theta1-=2*pi;
		while(theta1<0) theta1+=2*pi;
		while(theta2>=2*pi) theta2-=2*pi;
		while(theta2<0) theta2+=2*pi;
		if(Math.abs(theta1-pi/2)<1e-5||Math.abs(theta1-3*pi/2)<1e-5) {
			a1=1;
			b1=0;
		}
		else {
			b1=1;
			a1=-Math.tan(theta1);
		}
		c1=-(a1*x3+b1*y3);
		c4=-(a1*x+b1*y);
		if(Math.abs(theta2-pi/2)<1e-5||Math.abs(theta2-3*pi/2)<1e-5) {
			a2=1;
			b2=0;
		}
		else {
			b2=1;
			a2=-Math.tan(theta2);
		}
		c2=-(a2*x3+b2*y3);
		c3=-(a2*x4+b2*y4);
		MyPoint tmppoint2=intersection(a1,b1,c4,a2,b2,c2);
		tpoint2.x=tmppoint2.x;
		tpoint2.y=tmppoint2.y;
		MyPoint tmppoint1=intersection(a1,b1,c4,a2,b2,c3);
		int prep1x=tpoint1.x;
		int prep1y=tpoint1.y;
		tpoint1.x=tmppoint1.x;
		tpoint1.y=tmppoint1.y;
		double tmp1=a1*prep1x+b1*prep1y+c1;
		double tmp2=a1*x+b1*y+c1;
		if(tmp1>=0&&tmp2<=0||tmp1<=0&&tmp2>=0)
			return true;
		else
			return false;
	}
}