package shape;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class MyShape {
	//用列表存储参数
	public ArrayList<Object> parameter=new ArrayList<>();
	
	public void changepounds(int pounds) {}
	
	public void changecolor(Color color) {}
	
	public void move(int x, int y) {}//移动(x,y)的距离
	
	public void leftexpansion(int minx) {}//左边的扩张线
	
	public void rightexpansion(int maxx) {}//右边的扩张线
	
	public void upexpansion(int miny) {}//上边的扩张线
	
	public void downexpansion(int maxy) {}//下边的扩张线
	
	public void leftupexpansion(int minx, int miny) {}//左上的扩张线
	
	public void rightupexpansion(int maxx, int miny) {}//右上的扩张线

	public void leftdownexpansion(int minx, int maxy) {}//左下的扩张线
	
	public void rightdownexpansion(int maxx, int maxy) {}//右下的扩张线
	
	public void draw(Graphics g) {}
	
	public boolean ininternal(int x, int y) {return false;}
	
	private static void draw1poundline(Graphics g, int x1, int y1, int x2, int y2) {
		double e=Math.abs(x2-x1)>Math.abs(y2-y1)?Math.abs(x2-x1):Math.abs(y2-y1);
		double xgap=(x2-x1)/e;
		double ygap=(y2-y1)/e;
		double xtmp=x1;
		double ytmp=y1;
		g.drawLine(x1, y1, x1, y1);
		while(Math.abs(xtmp-x2)>=1||Math.abs(ytmp-y2)>=1) {
			xtmp+=xgap;
			ytmp+=ygap;
			int intxtmp=(int)(xtmp+0.5);
			int intytmp=(int)(ytmp+0.5);
			g.drawLine(intxtmp, intytmp, intxtmp, intytmp);
		}
	}
	
	private static void draw1poundcircle(Graphics g, int x0, int y0, int r) {
		int x=0,y=r;
		int e=1-r;
		g.drawLine(x+x0, y+y0, x+x0, y+y0);  //(x,y)
		g.drawLine(x+x0, -y+y0, x+x0, -y+y0);//(x,-y)
		g.drawLine(y+x0, x+y0, y+x0, x+y0);  //(y,x)
		g.drawLine(-y+x0, x+y0, -y+x0, x+y0);//(-y,x)
		while(x<y) {
			if(e<0)
				e=e+x+x+1;
			else {
				e=e+x+x-y-y+3;
				y--;
			}
			x++;
			g.drawLine(x+x0, y+y0, x+x0, y+y0);    //(x,y)
			g.drawLine(y+x0, x+y0, y+x0, x+y0);    //(y,x)
			g.drawLine(y+x0, -x+y0, y+x0, -x+y0);  //(y,-x)
			g.drawLine(x+x0, -y+y0, x+x0, -y+y0);  //(x,-y)
			g.drawLine(-x+x0, -y+y0, -x+x0, -y+y0);//(-x,-y)
			g.drawLine(-y+x0, -x+y0, -y+x0, -x+y0);//(-y,-x)
			g.drawLine(-y+x0, x+y0, -y+x0, x+y0);  //(-y,x)
			g.drawLine(-x+x0, y+y0, -x+x0, y+y0);  //(-x,y)
		}
	}
	
	public static void drawline(Graphics g, int x1, int y1, int x2, int y2, int pounds, Color color) {
		Color precolor=g.getColor();
		g.setColor(color);
		draw1poundline(g, x1, y1, x2, y2);
		double deltax=0,deltay=0;
		double up;
		double down;
		if(pounds%2==0) {
			up=pounds/2;
			down=pounds-up-1;
		}
		else {
			up=(pounds-1)/2;
			down=up;
		}
		double b1=0,b2=0;
		if(y1==y2) {
			deltay=-1;
			b2=-1;
		}
		else if(x1==x2) {
			deltax=-1;
			b1=-1;
		}
		else {
			double k1=-(double)(x2-x1)/(y2-y1);
			double e=Math.abs(k1)>1?k1:1;
			deltax=-1/e;
			deltay=-k1/e;
			if(k1<0&&k1>=-1) {
				deltax=-deltax;
				deltay=-deltay;
			}
			double delta=Math.sqrt(deltax*deltax+deltay*deltay);
			b1=deltax/delta;
			b2=deltay/delta;
		}
		double j;
		int prex1=x1,prey1=y1,prex2=x2,prey2=y2;
		int curx1,cury1,curx2,cury2;
		double xtmp=0;
		double ytmp=0;
		j=0;
		while(j<up) {
			xtmp+=deltax;
			ytmp+=deltay;
			curx1=(int)(x1+xtmp+0.5);
			cury1=(int)(y1+ytmp+0.5);
			curx2=(int)(x2+xtmp+0.5);
			cury2=(int)(y2+ytmp+0.5);
			if(Math.abs(prex1-curx1)!=1||Math.abs(prey1-cury1)!=1) {
				double a1=curx1-prex1;
				double a2=cury1-prey1;
				double add=a1*b1+a2*b2;
				if(Math.abs(j+add-up)>Math.abs(j-up))
					break;
				j+=add;
				prey1=cury1;
				prey2=cury2;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2);
			}
			else {
				double a1=curx1-prex1;
				double add=a1*b1;
				if(Math.abs(j+add-up)>Math.abs(j-up))
					break;
				j+=add;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2);
				if(j<up) {
					double a2=cury1-prey1;
					add=a2*b2;
					if(Math.abs(j+add-up)>Math.abs(j-up))
						break;
					j+=add;
					prey1=cury1;
					prey2=cury2;
					draw1poundline(g, prex1, prey1, prex2, prey2);
				}
			}
		}
		xtmp=0;
		ytmp=0;
		deltax=-deltax;
		deltay=-deltay;
		b1=-b1;
		b2=-b2;
		prex1=x1;
		prey1=y1;
		prex2=x2;
		prey2=y2;
		j=0;
		while(j<down) {
			xtmp+=deltax;
			ytmp+=deltay;
			curx1=(int)(x1+xtmp+0.5);
			cury1=(int)(y1+ytmp+0.5);
			curx2=(int)(x2+xtmp+0.5);
			cury2=(int)(y2+ytmp+0.5);
			if(Math.abs(prex1-curx1)!=1||Math.abs(prey1-cury1)!=1) {
				double a1=curx1-prex1;
				double a2=cury1-prey1;
				double add=a1*b1+a2*b2;
				if(Math.abs(j+add-down)>Math.abs(j-down))
					break;
				j+=add;
				prey1=cury1;
				prey2=cury2;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2);
			}
			else {
				double a1=curx1-prex1;
				double add=a1*b1;
				if(Math.abs(j+add-down)>Math.abs(j-down))
					break;
				j+=add;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2);
				if(j<down) {
					double a2=cury1-prey1;
					add=a2*b2;
					if(Math.abs(j+add-down)>Math.abs(j-down))
						break;
					j+=add;
					prey1=cury1;
					prey2=cury2;
					draw1poundline(g, prex1, prey1, prex2, prey2);
				}
			}
		}
		/*double deltax=0,deltay=0;
		double np=pounds-1;
		double b1=0,b2=0;
		if(y1==y2) {
			deltay=-1;
			b2=-1;
		}
		else if(x1==x2) {
			deltax=-1;
			b1=-1;
		}
		else {
			double k1=-(double)(x2-x1)/(y2-y1);
			double e=Math.abs(k1)>1?k1:1;
			deltax=-1/e;
			deltay=-k1/e;
			if(k1<0&&k1>=-1) {
				deltax=-deltax;
				deltay=-deltay;
			}
			double delta=Math.sqrt(deltax*deltax+deltay*deltay);
			b1=deltax/delta;
			b2=deltay/delta;
		}
		double j;
		int prex1=x1,prey1=y1,prex2=x2,prey2=y2;
		int curx1,cury1,curx2,cury2;
		double xtmp=0;
		double ytmp=0;
		j=0;
		while(j<np) {
			xtmp+=deltax;
			ytmp+=deltay;
			curx1=(int)(x1+xtmp+0.5);
			cury1=(int)(y1+ytmp+0.5);
			curx2=(int)(x2+xtmp+0.5);
			cury2=(int)(y2+ytmp+0.5);
			if(Math.abs(prex1-curx1)!=1||Math.abs(prey1-cury1)!=1) {
				double a1=curx1-prex1;
				double a2=cury1-prey1;
				double add=a1*b1+a2*b2;
				if(Math.abs(j+add-np)>Math.abs(j-np))
					break;
				j+=add;
				prey1=cury1;
				prey2=cury2;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2, color);
			}
			else {
				double a1=curx1-prex1;
				double add=a1*b1;
				if(Math.abs(j+add-np)>Math.abs(j-np))
					break;
				j+=add;
				prex1=curx1;
				prex2=curx2;
				draw1poundline(g, prex1, prey1, prex2, prey2, color);
				if(j<np) {
					double a2=cury1-prey1;
					add=a2*b2;
					if(Math.abs(j+add-np)>Math.abs(j-np))
						break;
					j+=add;
					prey1=cury1;
					prey2=cury2;
					draw1poundline(g, prex1, prey1, prex2, prey2, color);
				}
			}
		}*/
		g.setColor(precolor);
	}
	
	public static void drawrectangle(Graphics g, int minx, int miny, int maxx, int maxy, int pounds, Color color) {
		Color precolor=g.getColor();
		g.setColor(color);
		int up;
		int down;
		if(pounds%2==0) {
			up=pounds/2;
			down=pounds-up-1;
		}
		else {
			up=(pounds-1)/2;
			down=up;
		}
		drawline(g, minx-up, miny, maxx+down, miny, pounds, color);
		drawline(g, maxx, miny-up, maxx, maxy+down, pounds, color);
		drawline(g, minx-up, maxy, maxx+down, maxy, pounds, color);
		drawline(g, minx, miny-up, minx, maxy+down, pounds, color);
		g.setColor(precolor);
	}
	
	public static void drawcircle(Graphics g, int x0, int y0, int r, int pounds, Color color) {
		Color precolor=g.getColor();
		g.setColor(color);
		/*int up=pounds/2;
		int down=pounds-up-1;
		for(int i=-down;i<=up;i++)
			draw1poundcircle(g, x0, y0, r+i, color);*/
		draw1poundcircle(g, x0, y0, r);
		/*int i;
		int x=0,y=r;
		int e=1-r;
		for(i=0;i<pounds;i++) {
			g.drawLine(x+x0, y-i+y0, x+x0, y-i+y0);  //(x,y)
			g.drawLine(x+x0, i-y+y0, x+x0, i-y+y0);//(x,-y)
			g.drawLine(y-i+x0, x+y0, y-i+x0, x+y0);  //(y,x)
			g.drawLine(i-y+x0, x+y0, i-y+x0, x+y0);//(-y,x)
			
			g.drawLine(x+x0, y+i+y0, x+x0, y+i+y0);  //(x,y)
			g.drawLine(x+x0, -i-y+y0, x+x0, -i-y+y0);//(x,-y)
			g.drawLine(y+i+x0, x+y0, y+i+x0, x+y0);  //(y,x)
			g.drawLine(-i-y+x0, x+y0, -i-y+x0, x+y0);//(-y,x)
		}
		boolean flag;
		while(x<y) {
			flag=false;
			if(e<0)
				e=e+x+x+1;
			else {
				e=e+x+x-y-y+3;
				y--;
				flag=true;
			}
			x++;
			if(flag) {
				for(i=0;i<=pounds;i++) {
					g.drawLine(x+x0, y+i+y0, x+x0, y+i+y0);    //(x,y)
					g.drawLine(y+i+x0, x+y0, y+i+x0, x+y0);    //(y,x)
					g.drawLine(y+i+x0, -x+y0, y+i+x0, -x+y0);  //(y,-x)
					g.drawLine(x+x0, -i-y+y0, x+x0, -i-y+y0);  //(x,-y)
					g.drawLine(-x+x0, -i-y+y0, -x+x0, -i-y+y0);//(-x,-y)
					g.drawLine(-i-y+x0, -x+y0, -i-y+x0, -x+y0);//(-y,-x)
					g.drawLine(-i-y+x0, x+y0, -i-y+x0, x+y0);  //(-y,x)
					g.drawLine(-x+x0, y+i+y0, -x+x0, y+i+y0);  //(-x,y)
				}
			}
			else {
				for(i=0;i<pounds;i++) {
					g.drawLine(x+x0, y-i+y0, x+x0, y-i+y0);    //(x,y)
					g.drawLine(y-i+x0, x+y0, y-i+x0, x+y0);    //(y,x)
					g.drawLine(y-i+x0, -x+y0, y-i+x0, -x+y0);  //(y,-x)
					g.drawLine(x+x0, i-y+y0, x+x0, i-y+y0);  //(x,-y)
					g.drawLine(-x+x0, i-y+y0, -x+x0, i-y+y0);//(-x,-y)
					g.drawLine(i-y+x0, -x+y0, i-y+x0, -x+y0);//(-y,-x)
					g.drawLine(i-y+x0, x+y0, i-y+x0, x+y0);  //(-y,x)
					g.drawLine(-x+x0, y-i+y0, -x+x0, y-i+y0);  //(-x,y)
					
					g.drawLine(x+x0, y+i+y0, x+x0, y+i+y0);    //(x,y)
					g.drawLine(y+i+x0, x+y0, y+i+x0, x+y0);    //(y,x)
					g.drawLine(y+i+x0, -x+y0, y+i+x0, -x+y0);  //(y,-x)
					g.drawLine(x+x0, -i-y+y0, x+x0, -i-y+y0);  //(x,-y)
					g.drawLine(-x+x0, -i-y+y0, -x+x0, -i-y+y0);//(-x,-y)
					g.drawLine(-i-y+x0, -x+y0, -i-y+x0, -x+y0);//(-y,-x)
					g.drawLine(-i-y+x0, x+y0, -i-y+x0, x+y0);  //(-y,x)
					g.drawLine(-x+x0, y+i+y0, -x+x0, y+i+y0);  //(-x,y)
				}
			}
		}*/
		g.setColor(precolor);
	}
	
	public static void drawoval(Graphics g, int minx, int miny, int maxx, int maxy, int pounds, Color color) {
		Color precolor=g.getColor();
		g.setColor(color);
		int rx=(maxx-minx)/2;
		int ry=(maxy-miny)/2;
		int rx2=rx*rx;
		int ry2=ry*ry;
		int x=0,y=ry;
		double pk=ry2-rx2*ry+0.25*rx2;
		int x0=(maxx+minx)/2;
		int y0=(maxy+miny)/2;
		g.drawLine(x+x0, y+y0, x+x0, y+y0);//(x,y)
		g.drawLine(x+x0, -y+y0, x+x0, -y+y0);//(x,-y)
		while(ry2*x<rx2*y) {
			if(pk<0)
				pk=pk+2*x*ry2+ry2;
			else {
				pk=pk+2*x*ry2+ry2-2*y*rx2+rx2;
				y--;
			}
			x++;
			g.drawLine(x+x0, y+y0, x+x0, y+y0);//(x,y)
			g.drawLine(x+x0, -y+y0, x+x0, -y+y0);//(x,-y)
			g.drawLine(-x+x0, -y+y0, -x+x0, -y+y0);//(-x,-y)
			g.drawLine(-x+x0, y+y0, -x+x0, y+y0);//(-x,y)
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
			g.drawLine(x+x0, y+y0, x+x0, y+y0);//(x,y)
			g.drawLine(x+x0, -y+y0, x+x0, -y+y0);//(x,-y)
			g.drawLine(-x+x0, -y+y0, -x+x0, -y+y0);//(-x,-y)
			g.drawLine(-x+x0, y+y0, -x+x0, y+y0);//(-x,y)
		}
		g.setColor(precolor);
	}
	
	public static void drawpolygonborder(Graphics g, ArrayList<MyPoint> points, int pounds, Color color) {
		//绘制宽度为pounds的多边形的边框
		Color precolor=g.getColor();
		g.setColor(color);
		int len=points.size();
		if(len>1) {
			int x1, y1, x2=0, y2=0, startx, starty;
			MyPoint start=points.get(0);
			startx=x1=start.x;
			starty=y1=start.y;
			for(int i=1;i<len;i++) {
				MyPoint tmp=points.get(i);
				x2=tmp.x;
				y2=tmp.y;
				draw1poundline(g, x1, y1, x2, y2);
				x1=x2;
				y1=y2;
			}
			draw1poundline(g, x2, y2, startx, starty);
		}
		g.setColor(precolor);
	}
}