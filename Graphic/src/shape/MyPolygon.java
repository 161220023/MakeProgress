package shape;

import java.awt.Graphics;
import java.util.ArrayList;

import frame.ContentPanel;

import java.awt.Color;
import java.awt.Cursor;

class MyScale{
	double x, y;
	
	MyScale(double x, double y){
		this.x=x;
		this.y=y;
	}
}

class MyEdge{
	int number;
	int startx;
	int starty;
	double m;
}

class TwoEdges{
	MyEdge preedge;
	MyEdge aftedge;
}

public class MyPolygon extends MyShape {

	ArrayList<MyPoint> points;
	ArrayList<MyScale> scales;
	MyPoint point1,point2,point3,point4;//外接矩形的坐标,会随着旋转而改变
	double angle;  //外接矩形中心点与p1,p2中点连线与x轴夹角
	int pounds;
	Color bordercolor;
	Color innercolor;
	
	//static int inf=0x7fffffff;
	
	public MyPolygon(){//pounds, color, arraylist<MyPoint>, MyPoint(minx, miny), MyPoint(maxx, maxy), ArrayList<double>(scale)
		//各点按照顺序形成边
	}
	
	public MyPolygon(int pounds, Color color){
		angle=3*pi/2;
		this.pounds=pounds;
		this.bordercolor=color;
		points=new ArrayList<MyPoint>();
		scales=new ArrayList<MyScale>();
	}
	
	public int countpoints() {
		return points.size();
	}
	
	@Override
	public void changepounds(int pounds) {
		this.pounds=pounds;
	}
	
	@Override
	public void changecolor(Color color) {
		this.bordercolor=color;
	}
	
	static void fill(Graphics g, Color innercolor, ArrayList<MyPoint> points) {
		if(innercolor==null) return;
		//先计算各个顶点的y值排序(连带顶点),用队列储存
		//按照顺序判断每个顶点(队列中的每个元素)连接的哪两条边(边号),每条边的斜率分别是什么,该顶点的x值是什么,并储存在队列中
		//对每个[yi+1,y(i+1)-1]范围,处理第yi个顶点连接的两条边(如果有一条边已经存在于list中,则判断该顶点连接的另一条边的另一个顶点y值是否大于yi+1,若是则用该边替换list中的边,否则将两条边加入list)形成list边表
		//对于相同的y(i+1)要连续处理,每个点都要判断连接的边进行添减
		//判断某条边的斜率是否为0,若为0,则其斜率储存为一个很大的数(在处理后续y(i+1)的时候会将此边删除)
		ArrayList<Integer> y=new ArrayList<>();
		ArrayList<TwoEdges> edges=new ArrayList<>();
		int len=points.size();
		for(int i=0;i<len;i++) {
			MyPoint tmppoint=points.get(i);
			TwoEdges twoedges=new TwoEdges();
			MyEdge preedge=new MyEdge();
			MyEdge aftedge=new MyEdge();
			MyPoint prepoint,aftpoint;
			preedge.number=i-1;
			aftedge.number=i;
			if(i==0) {
				preedge.number=len-1;
				prepoint=points.get(len-1);
				aftpoint=points.get(i+1);
			}
			else if(i==len-1){
				prepoint=points.get(i-1);
				aftpoint=points.get(0);
			}
			else {
				prepoint=points.get(i-1);
				aftpoint=points.get(i+1);
			}
			int prex=prepoint.x, prey=prepoint.y;
			int aftx=aftpoint.x, afty=aftpoint.y;
			int curx=tmppoint.x, cury=tmppoint.y;
			preedge.startx=aftedge.startx=curx;
			preedge.starty=aftedge.starty=cury;
			if(prepoint.y==tmppoint.y) preedge.m=inf;
			else preedge.m=((double)(prex-curx))/(prey-cury);
			if(aftpoint.y==tmppoint.y) aftedge.m=inf;
			else aftedge.m=((double)(curx-aftx))/(cury-afty);
			twoedges.preedge=preedge;
			twoedges.aftedge=aftedge;
			edges.add(twoedges);
			Integer tmpy=tmppoint.y;
			y.add(tmpy);
		}
		sort(y,edges);
		ArrayList<MyEdge> edgelist=new ArrayList<>();
		int yi,yi1;
		int ymax=y.get(len-1);
		int i=0;
		
		Color precolor=g.getColor();
		g.setColor(innercolor);
		while(i<len) {
			yi=y.get(i);
			if(yi==ymax) break;
			yi1=yi;
			while(yi1==yi) {
				int size=edgelist.size();
				TwoEdges twoedge=edges.get(i);
				MyEdge leftedge=twoedge.preedge;
				MyEdge rightedge=twoedge.aftedge;
				int number1=leftedge.number;
				int number2=rightedge.number;
				int j1=-1,j2=-1,j;
				for(j=0;j<size;j++) {
					MyEdge tmpedge=edgelist.get(j);
					if(number1==tmpedge.number) j1=j;
					if(number2==tmpedge.number) j2=j;
				}
				if(j1>=0&&j2>=0) {
					if(j1<j2) {
						edgelist.remove(j2);
						edgelist.remove(j1);
					}
					else {
						edgelist.remove(j1);
						edgelist.remove(j2);
					}
				}
				else if(j1>=0) {
					edgelist.remove(j1);
					edgelist.add(j1, rightedge);
				}
				else if(j2>=0) {
					edgelist.remove(j2);
					edgelist.add(j2, leftedge);
				}
				else {
					edgelist.add(leftedge);
					edgelist.add(rightedge);
				}
				i++;
				yi1=y.get(i);
			}
			//[yi+1,yi1]范围内填充
			int size=edgelist.size();
			double[] m=new double[size];
			double[] tmpx=new double[size];
			for(int k=0;k<size;k+=2) {
				MyEdge preedge=edgelist.get(k);
				MyEdge aftedge=edgelist.get(k+1);
				m[k]=preedge.m;
				m[k+1]=aftedge.m;
				tmpx[k]=preedge.startx+m[k]*(yi-preedge.starty);
				tmpx[k+1]=aftedge.startx+m[k+1]*(yi-aftedge.starty);
			}
			double[] tmptmpx=new double[size];
			for(int j=yi+1;j<=yi1;j++) {
				for(int k=0;k<size;k+=2) {
					tmpx[k]+=m[k];
					tmpx[k+1]+=m[k+1];
					tmptmpx[k]=tmpx[k];
					tmptmpx[k+1]=tmpx[k+1];
				}
				//对tmptmpx[size]排个序
				sort(tmptmpx);
				for(int k=0;k<size;k+=2)
					g.drawLine((int)tmptmpx[k], j, (int)tmptmpx[k+1], j);
			}
		}
		g.setColor(precolor);
	}

	@Override
	public void fillup(Color color, Graphics g) {
		innercolor=color;
		//fill(g,innercolor,points);
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
		else if(MyLine.aroundline(x1,y1,x2,y2,x,y))
			return ContentPanel.upexpansion;
		else if(MyLine.aroundline(x2,y2,x3,y3,x,y))
			return ContentPanel.rightexpansion;
		else if(MyLine.aroundline(x3,y3,x4,y4,x,y))
			return ContentPanel.downexpansion;
		else if(MyLine.aroundline(x4,y4,x1,y1,x,y))
			return ContentPanel.leftexpansion;
		else {
			int len=points.size();
			for(int i=0;i<len;i++) {
				MyPoint curpoint=points.get(i);
				if(aroundpoint(curpoint.x,curpoint.y,x,y))
					return ContentPanel.hand;
			}
			if(ininternal(x,y))
				return ContentPanel.move;
			else if(inrotate(x,y,point1,point2,angle))
				return ContentPanel.rotate;
			return ContentPanel.crisscross;
		}
	}

	public int nearindex(int x, int y) {
		int len=points.size();
		for(int i=0;i<len;i++) {
			MyPoint curpoint=points.get(i);
			if(aroundpoint(curpoint.x,curpoint.y,x,y))
				return i;
		}
		return -1;
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
	
	public void addpoint(MyPoint point) {
		points.add(point);
	}
	
	public void addpoints(ArrayList<MyPoint> points) {
		int len=points.size();
		for(int i=0;i<len;i++)
			this.points.add(points.get(i));
	}
	
	public void addminmax() {
		//根据angle添加外接矩形4个点的坐标
		double a1,a2,b1,b2,c1=-10000000,c2=10000000,c3=-10000000,c4=10000000;
		double theta1=angle;
		double theta2=angle+pi/2;
		if(theta2>=2*pi) theta2-=2*pi;
		if(Math.abs(theta1-pi/2)<1e-5||Math.abs(theta1-3*pi/2)<1e-5) {
			b1=0;
			a1=1;
		}
		else {
			b1=1;
			a1=-Math.tan(theta1);
		}
		if(Math.abs(theta2-pi/2)<1e-5||Math.abs(theta2-3*pi/2)<1e-5) {
			b2=0;
			a2=1;
		}
		else {
			b2=1;
			a2=-Math.tan(theta2);
		}
		int len=points.size();
		double tmpc;
		for(int i=0;i<len;i++) {
			MyPoint curpoint=points.get(i);
			int x=curpoint.x, y=curpoint.y;
			tmpc=-(a1*x+b1*y);
			if(tmpc>c1) c1=tmpc;
			if(tmpc<c2) c2=tmpc;
			tmpc=-(a2*x+b2*y);
			if(tmpc>c3) c3=tmpc;
			if(tmpc<c4) c4=tmpc;
		}
		MyPoint p13=MyRectangle.intersection(a1, b1, c1, a2, b2, c3);
		MyPoint p23=MyRectangle.intersection(a1, b1, c2, a2, b2, c3);
		MyPoint p14=MyRectangle.intersection(a1, b1, c1, a2, b2, c4);
		MyPoint p24=MyRectangle.intersection(a1, b1, c2, a2, b2, c4);
		if(angle>=0&&angle<=pi/2) {
			point1=p14;
			point2=p24;
			point3=p23;
			point4=p13;
		}
		else if(angle>pi/2&&angle<=pi) {
			point1=p24;
			point2=p14;
			point3=p13;
			point4=p23;
		}
		else if(angle>pi&&angle<=3*pi/2) {
			point1=p23;
			point2=p13;
			point3=p14;
			point4=p24;
		}
		else {
			point1=p13;
			point2=p23;
			point3=p24;
			point4=p14;
		}
	}
	
	public void addscale() {
		//添加每一个点在x和y中的比例(相对于p1而言)
		//求出宽度和高度|p1p2|和|p1p4|
		if(point1==null||point2==null||point3==null||point4==null)
			addminmax();
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x4=point4.x, y4=point4.y;
		double d12=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		double d14=Math.sqrt((x1-x4)*(x1-x4)+(y1-y4)*(y1-y4));
		double theta12=angle+pi/2;
		if(theta12>=2*pi) theta12-=2*pi;
		double theta14=angle;
		double a12,a14,b12,b14,c12,c14;
		if(Math.abs(theta12-pi/2)<1e-5||Math.abs(theta12-3*pi/2)<1e-5) {
			b12=0;
			a12=1;
		}
		else {
			b12=1;
			a12=-Math.tan(theta12);
		}
		c12=-(a12*x1+b12*y1);
		if(Math.abs(theta14-pi/2)<1e-5||Math.abs(theta14-3*pi/2)<1e-5) {
			b14=0;
			a14=1;
		}
		else {
			b14=1;
			a14=-Math.tan(theta14);
		}
		c14=-(a14*x1+b14*y1);
		int len=points.size();
		scales.clear();
		for(int i=0;i<len;i++) {
			MyPoint curpoint=points.get(i);
			int curx=curpoint.x, cury=curpoint.y;
			MyPoint p14=MyRectangle.intersection(a14, b14, c14, a12, b12, -(a12*curx+b12*cury));
			MyPoint p12=MyRectangle.intersection(a12, b12, c12, a14, b14, -(a14*curx+b14*cury));
			double tmp12=Math.sqrt((x1-p12.x)*(x1-p12.x)+(y1-p12.y)*(y1-p12.y));
			tmp12/=d12;
			double tmp14=Math.sqrt((x1-p14.x)*(x1-p14.x)+(y1-p14.y)*(y1-p14.y));
			tmp14/=d14;
			scales.add(new MyScale(tmp12,tmp14));
		}
	}
	
	@Override
	public boolean inboarder(int x, int y) {
		//是否在多边形的某一条边上
		int len=points.size();
		MyPoint firstpoint=points.get(0);
		MyPoint prepoint=firstpoint;
		for(int i=1;i<len;i++) {
			MyPoint curpoint=points.get(i);
			if(MyLine.aroundline(prepoint.x, prepoint.y, curpoint.x, curpoint.y, x, y))
				return true;
			prepoint=curpoint;
		}
		if(MyLine.aroundline(prepoint.x, prepoint.y, firstpoint.x, firstpoint.y, x, y))
			return true;
		else
			return false;
	}
	
	@Override
	public boolean ininternal(int x, int y) {
		int len=points.size();
		//从顶点(x,y)出发向右发出射线判断与多边形的边的相交情况
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
				if(x2==x&&y2==y)//与顶点重合
					return true;
				if(Math.abs((x-x1)*(y-y2)-(x-x2)*(y-y1))<=2)//在边上
					return true;
				if(y1!=y2) {
					double xcross=(x2*y1-x1*y2-(x2-x1)*y)/(y1-y2);
					if(xcross>=x&&((y1<y&&y2>=y)||(y2<y&&y1>=y)))
						count++;
				}
				x1=x2;
				y1=y2;
			}
			x1=x2;
			y1=y2;
			x2=firstx;
			y2=firsty;
			if(Math.abs((x-x1)*(y-y2)-(x-x2)*(y-y1))<=2)//在边上
				return true;
			if(y1!=y2) {
				double xcross=(x2*y1-x1*y2-(x2-x1)*y)/(y1-y2);
				if(xcross>=x&&((y1<y&&y2>=y)||(y2<y&&y1>=y)))
					count++;
			}
		}
		if(count%2==0)
			return false;
		else
			return true;
	}
	
	@Override
	public void rotate(double theta) {
		//外接矩形旋转，各个点旋转，绕着中心点
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
		point1.x=(int)(x0+(x1-x0)*costheta+(y0-y1)*sintheta);
		point1.y=(int)(y0+(x1-x0)*sintheta+(y1-y0)*costheta);
		point2.x=(int)(x0+(x2-x0)*costheta+(y0-y2)*sintheta);
		point2.y=(int)(y0+(x2-x0)*sintheta+(y2-y0)*costheta);
		point3.x=(int)(x0+(x3-x0)*costheta+(y0-y3)*sintheta);
		point3.y=(int)(y0+(x3-x0)*sintheta+(y3-y0)*costheta);
		point4.x=(int)(x0+(x4-x0)*costheta+(y0-y4)*sintheta);
		point4.y=(int)(y0+(x4-x0)*sintheta+(y4-y0)*costheta);
		int len=points.size();
		for(int i=0;i<len;i++) {
			MyPoint curpoint=points.get(i);
			int curx=curpoint.x, cury=curpoint.y;
			curpoint.x=(int)(x0+(curx-x0)*costheta+(y0-cury)*sintheta);
			curpoint.y=(int)(y0+(curx-x0)*sintheta+(cury-y0)*costheta);
		}
	}
	
	@Override
	public void move(int x, int y) {
		int len=points.size();
		for(int i=0;i<len;i++) {
			MyPoint point=points.get(i);
			point.x+=x;
			point.y+=y;
		}
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		point1.x+=x; point1.y+=y;
		point2.x+=x; point2.y+=y;
		point3.x+=x; point3.y+=y;
		point4.x+=x; point4.y+=y;
	}
	
	private void recountpoints() {
		//根据scale和外接矩形重新计算点的坐标
		int x1=point1.x, y1=point1.y;
		int x2=point2.x, y2=point2.y;
		int x4=point4.x, y4=point4.y;
		double a1,a2,b1,b2,c1,c2,c3,c4;
		double theta1=angle+pi/2;
		while(theta1>=2*pi) theta1-=2*pi;
		double theta2=angle;
		if(Math.abs(theta1-pi/2)<1e-5||Math.abs(theta1-3*pi/2)<1e-5) {
			b1=0;
			a1=1;
		}
		else {
			b1=1;
			a1=-Math.tan(theta1);
		}
		c1=-(a1*x1+b1*y1);
		c2=-(a1*x4+b1*y4);
		if(Math.abs(theta2-pi/2)<1e-5||Math.abs(theta2-3*pi/2)<1e-5) {
			b2=0;
			a2=1;
		}
		else {
			b2=1;
			a2=-Math.tan(theta2);
		}
		c3=-(a2*x1+b2*y1);
		c4=-(a2*x2+b2*y2);
		double tmpc1,tmpc2;
		int len=scales.size();
		points.clear();
		for(int i=0;i<len;i++) {
			MyScale curscale=scales.get(i);
			tmpc1=curscale.y*(c2-c1)+c1;
			tmpc2=curscale.x*(c4-c3)+c3;
			MyPoint curpoint=MyRectangle.intersection(a1, b1, tmpc1, a2, b2, tmpc2);
			points.add(curpoint);
		}
	}

	@Override
	public void leftupexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.pointexpansion(point1,point2,point3,point4,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}
	
	@Override
	public void rightupexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.pointexpansion(point2,point1,point4,point3,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}
	
	@Override
	public void rightdownexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.pointexpansion(point3,point4,point1,point2,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}

	@Override
	public void leftdownexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.pointexpansion(point4,point3,point2,point1,angle,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}
	
	@Override
	public void upexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.edgeexpansion(point1,point2,point3,point4,angle+pi/2,x,y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}
	
	@Override
	public void rightexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		MyRectangle.edgeexpansion(point2, point3, point4, point1, angle, x, y);
		recountpoints();
	}
	
	@Override
	public void downexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		if(MyRectangle.edgeexpansion(point3, point4, point1, point2, angle+pi/2, x, y)) {
			angle+=pi;
			if(angle>=2*pi)
				angle-=2*pi;
		}
		recountpoints();
	}
	
	@Override
	public void leftexpansion(int x, int y) {
		if(point1==null||point2==null||point3==null||point4==null)
			addscale();
		MyRectangle.edgeexpansion(point4,point1,point2,point3,angle,x,y);
		recountpoints();
	}
	
	public void changepoint(int index, int x, int y) {
		//改变第index个点的坐标,pointmin,pointmax,以及所有点的比例scale
		MyPoint curpoint=points.get(index);
		curpoint.x=x;
		curpoint.y=y;
		addminmax();
		addscale();
	}
	
	@Override
	public void draw(Graphics g) {
		fill(g,innercolor,points);
		MyShape.drawpolygonborder(g, points, pounds, bordercolor);
	}
}
