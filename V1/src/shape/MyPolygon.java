package shape;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

class MyScale{
	double x, y;
	MyScale(double x, double y){
		this.x=x;
		this.y=y;
	}
}

public class MyPolygon extends MyShape {

	public MyPolygon(){//pounds, color, arraylist<MyPoint>, MyPoint(minx, miny), MyPoint(maxx, maxy), ArrayList<double>(scale)
		//���㰴��˳���γɱ�
	}
	
	public MyPolygon(int pounds, Color color){
		parameter.add(pounds);
		parameter.add(color);
		parameter.add(new ArrayList<MyPoint>());
	}
	
	public void addpoint(MyPoint point) {
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		points.add(point);
	}
	
	public void addpoints(ArrayList<MyPoint> points) {
		int len=points.size();
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> mypoints=(ArrayList<MyPoint>)parameter.get(2);
		for(int i=0;i<len;i++)
			mypoints.add(points.get(i));
	}
	
	public void addminmax(MyPoint pointmin, MyPoint pointmax) {
		parameter.add(pointmin);
		parameter.add(pointmax);
	}
	
	public void addminmax() {
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		int len=points.size();
		if(len>1) {
			int minx=1000,miny=1000,maxx=-1000,maxy=-1000;
			for(int i=0;i<len;i++) {
				MyPoint point=points.get(i);
				if(point.x<minx)
					minx=point.x;
				if(point.x>maxx)
					maxx=point.x;
				if(point.y<miny)
					miny=point.y;
				if(point.y>maxy)
					maxy=point.y;
			}
			parameter.add(new MyPoint(minx,miny));
			parameter.add(new MyPoint(maxx,maxy));
		}
	}
	
	public void addscale() {
		//���ÿһ������x��y�еı���(��������ϽǶ���)
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		double xgap=pointmax.x-pointmin.x;
		double ygap=pointmax.y-pointmin.y;
		int minx=pointmin.x;
		int miny=pointmin.y;
		int len=points.size();
		if(len>1) {
			ArrayList<MyScale> scales=new ArrayList<MyScale>();
			for(int i=0;i<len;i++) {
				MyPoint point=points.get(i);
				double scalex=(point.x-minx)/xgap;
				double scaley=(point.y-miny)/ygap;
				scales.add(new MyScale(scalex,scaley));
			}
			parameter.add(scales);
		}
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		if(x<pointmin.x||x>pointmax.x||y<pointmin.y||y>pointmax.y)
			return false;
		//�Ӷ���(x,y)�������ҷ��������ж������εıߵ��ཻ���
		len=points.size();
		int count=0;
		if(len>1) {
			int firstx=0,firsty=0,x1=0,y1=0,x2=0,y2=0;
			MyPoint firstpoint=points.get(0);
			firstx=x1=firstpoint.x;
			firsty=y1=firstpoint.y;
			if(firstx==x&&firsty==y)
				return true;
			for(int i=1;i<len;i++) {
				MyPoint tmp=points.get(i);
				x2=tmp.x;
				y2=tmp.y;
				if(x2==x&&y2==y)//�붥���غ�
					return true;
				if(Math.abs((x-x1)*(y-y2)-(x-x2)*(y-y1))<=2)//�ڱ���
					return true;
				double xcross=(x2*y1-x1*y2-(x2-x1)*y)/(y1-y2);
				if(xcross>=x&&((y1<y&&y2>=y)||(y2<y&&y1>=y)))
					count++;
				x1=x2;
				y1=y2;
			}
			x1=x2;
			y1=y2;
			x2=firstx;
			y2=firsty;
			if(Math.abs((x-x1)*(y-y2)-(x-x2)*(y-y1))<=2)//�ڱ���
				return true;
			double xcross=(x2*y1-x1*y2-(x2-x1)*y)/(y1-y2);
			if(xcross>=x&&((y1<y&&y2>=y)||(y2<y&&y1>=y)))
				count++;
		}
		if(count%2==0)
			return false;
		else
			return true;
	}
	
	@Override
	public void move(int x, int y) {
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		len=points.size();
		for(int i=0;i<len;i++) {
			MyPoint point=points.get(i);
			point.x+=x;
			point.y+=y;
		}
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		pointmin.x+=x;
		pointmin.y+=y;
		pointmax.x+=x;
		pointmax.y+=y;
	}
	
	private void recountpoints() {
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		int minx=pointmin.x;
		int miny=pointmin.y;
		int xgap=pointmax.x-pointmin.x;
		int ygap=pointmax.y-pointmin.y;
		@SuppressWarnings("unchecked")
		ArrayList<MyScale> scales=(ArrayList<MyScale>)parameter.get(5);
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		int len=scales.size();
		for(int i=0;i<len;i++) {//���ݱ��������µ�����λ��
			MyScale curscale=scales.get(i);
			MyPoint curpoint=points.get(i);
			double deltax=curscale.x*xgap;
			double deltay=curscale.y*ygap;
			curpoint.x=(int) (minx+deltax);
			curpoint.y=(int) (miny+deltay);
		}
	}
	
	@Override
	public void leftupexpansion(int x, int y) {
		//�ı�minx,miny,���ܳ���maxx,maxy
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=x;
		if(y>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=y;
		recountpoints();
	}
	
	@Override
	public void upexpansion(int y) {
		//�ı�miny,���ܳ���maxy
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(y>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=y;
		recountpoints();
	}
	
	public void rightupexpansion(int x, int y) {
		//�ı�maxx,����С��minx,�ı�miny,���ܳ���maxy
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=x;
		if(y>pointmax.y)
			pointmin.y=pointmax.y;
		else
			pointmin.y=y;
		recountpoints();
	}
	
	@Override
	public void rightexpansion(int x) {
		//�ı�maxx,����С��minx
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=x;
		recountpoints();
	}
	
	@Override
	public void rightdownexpansion(int x, int y) {
		//�ı�max,maxy,����С��minx,miny
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x<pointmin.x)
			pointmax.x=pointmin.x;
		else
			pointmax.x=x;
		if(y<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=y;
		recountpoints();
	}

	@Override
	public void downexpansion(int y) {
		//�ı�maxy,����С��miny
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(y<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=y;
		recountpoints();
	}
	
	public void leftdownexpansion(int x, int y) {
		//�ı�minx,���ܳ���maxx,�ı�maxy,����С��miny
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=x;
		if(y<pointmin.y)
			pointmax.y=pointmin.y;
		else
			pointmax.y=y;
		recountpoints();
	}
	
	@Override
	public void leftexpansion(int x) {
		//�ı�minx,���ܳ���maxx
		int len=parameter.size();
		if(len<=3) {
			addminmax();
			addscale();
		}
		else if(len<=5)
			addscale();
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		if(x>pointmax.x)
			pointmin.x=pointmax.x;
		else
			pointmin.x=x;
		recountpoints();
	}
		
	private void recountscale() {
		//���¼������е�ı���
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		@SuppressWarnings("unchecked")
		ArrayList<MyScale> scales=(ArrayList<MyScale>)parameter.get(5);
		MyPoint pointmin=(MyPoint)parameter.get(3);
		MyPoint pointmax=(MyPoint)parameter.get(4);
		int len=points.size();
		if(len<=1)
			return;
		int minx=10000,miny=10000,maxx=-10000,maxy=-10000;
		for(int i=0;i<len;i++) {
			MyPoint curpoint=points.get(i);
			if(curpoint.x<minx)
				minx=curpoint.x;
			if(curpoint.x>maxx)
				maxx=curpoint.x;
			if(curpoint.y<miny)
				miny=curpoint.y;
			if(curpoint.y>maxy)
				maxy=curpoint.y;
		}
		pointmin.x=minx;
		pointmin.y=miny;
		pointmax.x=maxx;
		pointmax.y=maxy;
		double xgap=maxx-minx;
		double ygap=maxy-miny;
		for(int i=0;i<len;i++) {
			MyPoint point=points.get(i);
			double scalex=(point.x-minx)/xgap;
			double scaley=(point.y-miny)/ygap;
			MyScale scale=scales.get(i);
			scale.x=scalex;
			scale.y=scaley;
		}
	}
	
	public void changepoint(int index, int x, int y) {
		//�ı��index���������,pointmin,pointmax,�Լ����е�ı���scale
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		int len=points.size();
		if(index>=len)
			return;
		MyPoint curpoint=points.get(index);
		curpoint.x=x;
		curpoint.y=y;
		recountscale();
	}
	
	@Override
	public void draw(Graphics g) {
		@SuppressWarnings("unchecked")
		ArrayList<MyPoint> points=(ArrayList<MyPoint>)parameter.get(2);
		int pounds=(int)parameter.get(0);
		Color color=(Color)parameter.get(1);
		MyShape.drawpolygonborder(g, points, pounds, color);
	}
}
