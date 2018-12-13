package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener {
	public static boolean Polygonflag=false; //是否要画多边形
	public static boolean bsplinecurveflag=false;//是否要画B样条曲线
	public static Color forecolor=null;      //颜色
	public static MyShape shape=null;        //画的形状
	//public static Font font=null;            //字体
	public static MyShape curshape=null;	 //当前形状
	public static int fontsize=0;            //线粗细
	public static MyPolygon polygon=null;           //当前是否在画多边形
	public static MyBSplineCurve bcurve=null;       //当前是否在画B样条曲线
	Graphics g;
	
	FilePopMenu fpm;

	public ArrayList<MyShape> shapes;
	int firstx,firsty;
	int minx,miny,maxx,maxy;
	boolean modify=false;
	boolean start=false;
	boolean edit=false;
	int index=0;
	//还需要有其他...如字体,大小等

	public static Cursor normal=new Cursor(0);            //最常见的鼠标形状
	public static Cursor crisscross=new Cursor(1);  		//十
	public static Cursor text=new Cursor(2);  			//| 输入文本
	public static Cursor loading=new Cursor(3);  			//加载  o
	public static Cursor rightupexpansion=new Cursor(4);  //正斜扩 /
	public static Cursor rightdownexpansion=new Cursor(5);//反斜扩 \
	public static Cursor leftupexpansion=new Cursor(6);  	//反斜扩 \
	public static Cursor leftdownexpansion=new Cursor(7); //正斜扩 /
	public static Cursor upexpansion=new Cursor(8);  		//上下扩大
	public static Cursor downexpansion=new Cursor(9); 	//上下扩大
	public static Cursor leftexpansion=new Cursor(10);	//<-->左右扩大
	public static Cursor rightexpansion=new Cursor(11);	//<-->左右扩大
	public static Cursor hand=new Cursor(12);				//手形
	public static Cursor move=new Cursor(13);				//移动
	public static Toolkit tk=Toolkit.getDefaultToolkit();
	static Image cimage=tk.getImage("E:\\eclipse_workspace\\Graphic\\bin\\frame\\additions\\rotate.png");
	public static Cursor rotate = tk.createCustomCursor(cimage, new Point(30, 30), "norm");
	
	
	Cursor cursor;
	
	int x1,x2,y1,y2;
	int xpos,ypos;
	JLabel location;
	
	ContentPanel(){
		//还需要添加粗细,字体,画笔等方法
		fpm=new FilePopMenu();
		location=new JLabel();
		shapes=new ArrayList<MyShape>();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setBackground(Color.WHITE);
		this.setSize(new Dimension(726,350));
		
		//tk=Toolkit.getDefaultToolkit();
	}
	
	public void initial() {
		g=this.getGraphics();
		
	}
	
	@Override
	public void repaint() {
		super.paint(g);
		if(shapes==null)
			return;
		int len=shapes.size();
		for(int i=0;i<len;i++) {
			MyShape shape=shapes.get(i);
			shape.draw(g);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自动生成的方法存根
		//点之后没有移动然后释放是为点击
		//先press,后release,最后click
		if(e.getClickCount()==2) {//双击生成多边形
			if(ContentPanel.Polygonflag) {    //当前选择的图形是多边形
				if(polygon!=null) {           //当前正在画多边形
					//判断点集大小是否超过1
					int len=polygon.countpoints();
					if(len>1) {//这个多边形不止有一个起始点，则加入此多边形
						polygon.addscale();
						shapes.add(polygon);
						curshape=polygon;
						MyShape.drawline(g, x1, y1, firstx, firsty, 1, forecolor);
						//edit=true;
					}
					polygon=null;
				}
			}
		}
		else {//如果点击的地方满足某个图形的方程(在附近),则设置当前图形为curshape
			if(this.cursor==crisscross&&(polygon==null||start==false)) {//鼠标形状是crisscross且当前没有在画多边形或当前多边形start==false
				int tmplen=shapes.size();
				for(int i=0;i<tmplen;i++) {
					MyShape tmpshape=shapes.get(i);
					if(tmpshape.inboarder(x2, y2)) {
						curshape=tmpshape;
						polygon=null;
						return;
					}
				}
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根
		if(this.getCursor()==crisscross) {
			if(!ContentPanel.Polygonflag) {  //不是画多边形
				x1=e.getX();
				y1=e.getY();
			}
			else {//画多边形
				if(polygon==null) {  //还没开始画
					x1=e.getX();
					y1=e.getY();
					polygon=new MyPolygon(fontsize,forecolor);
					polygon.addpoint(new MyPoint(x1, y1));
					firstx=minx=maxx=x1;
					firsty=miny=maxy=y1;
					start=false;
				}
			}
		}
		else if(curshape!=null) { //处于编辑状态并且当前图形不为空
			edit=true;
			x1=e.getX();
			y1=e.getY();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		x2=e.getX();
		y2=e.getY();
		if(e.isPopupTrigger())
			fpm.show(e.getComponent(), x2, y2);
		else {
			if(x1==x2&&y1==y2)
				return;
			//if(!edit&&this.getCursor()==crisscross) {   //不处于编辑状态,处于画图状态
			if(!edit) {
				if(!ContentPanel.Polygonflag) {   //不是画多边形
					@SuppressWarnings("rawtypes")
					Class type=shape.getClass();
					MyShape shape=null;
					int minx=Math.min(x1, x2);
					int miny=Math.min(y1, y2);
					int	maxx=Math.max(x1, x2);
					int maxy=Math.max(y1, y2);
					if(type==MyLine.class)
						shape=new MyLine(new MyPoint(x1, y1), new MyPoint(x2, y2), fontsize, forecolor);
					else if(type==MyRectangle.class)
						shape=new MyRectangle(new MyPoint(minx, miny), new MyPoint(maxx, maxy), fontsize, forecolor);
					else if(type==MyCircle.class) {
						int r=(int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
						shape=new MyCircle(new MyPoint(x1, y1), r, fontsize, forecolor);
					}
					else if(type==MyOval.class)
						shape=new MyOval(new MyPoint(minx, miny), new MyPoint(maxx, maxy), fontsize, forecolor);
					if(shape!=null) {
						shapes.add(shape);
						shape.draw(g);
						curshape=shape;
					}
				}
				else if(polygon!=null) {    //已经开始画多边形了
					if(!start) {            //还没有画出任何一条边
						if(!nearby(firstx,firsty,x2,y2,5)) {//不在起始点的附近
							start=true;
							polygon.addpoint(new MyPoint(x2, y2));
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
							x1=x2;
							y1=y2;
							if(x2>maxx) maxx=x2;
							if(x2<minx) minx=x2;
							if(y2>maxy) maxy=y2;
							if(y2<miny) miny=y2;
						}
					}
					else {    //已经至少画出了一条边
						if(nearby(firstx,firsty,x2,y2,5)) {
							//离初始点很近,完成图形的绘制
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
							polygon.addminmax();
							shapes.add(polygon);
							curshape=polygon;
							polygon=null;
						}
						else {//添加新点
							MyPoint point=new MyPoint(x2,y2);
							polygon.addpoint(point);
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
							x1=x2;
							y1=y2;
							if(x2>maxx) maxx=x2;
							if(x2<minx) minx=x2;
							if(y2>maxy) maxy=y2;
							if(y2<miny) miny=y2;
						}
					}
				}
			}
			else if(edit){   //处于编辑的状态
				if(this.getCursor()==move) //移动
					curshape.move(x2-x1, y2-y1);
				else {
					if(curshape.getClass()==MyPolygon.class&&cursor==hand)
						((MyPolygon)(curshape)).changepoint(index, x2, y2);
					curshape.changeshape(cursor, x2, y2);
				}
				curshape.draw(g);
				edit=false;      //已经编辑完毕
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根
		//@SuppressWarnings("rawtypes")
		//Class shapetype=shape.getClass();
		//if(shapetype==MyLine.class||shapetype==MyRectangle.class||shapetype==MyOval.class||shapetype==MyCircle.class||shapetype==MyPolygon.class)
		//	this.setCursor(crisscross);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根
		location.setText("");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 自动生成的方法存根
		xpos=e.getX();
		ypos=e.getY();
		location.setText("坐标:" + xpos + "," + ypos);
		
		/*repaint();
		x2=e.getX();
		y2=e.getY();
		if(e.isPopupTrigger())
			fpm.show(e.getComponent(), x2, y2);
		else {
			if(!edit&&this.getCursor()==crisscross) {   //不处于编辑状态,处于画图状态
				if(!ContentPanel.Polygonflag) {   //不是画多边形
					@SuppressWarnings("rawtypes")
					Class type=shape.getClass();
					int minx=Math.min(x1, x2);
					int miny=Math.min(y1, y2);
					int	maxx=Math.max(x1, x2);
					int maxy=Math.max(y1, y2);
					if(type==MyLine.class)
						MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
						//shape=new MyLine(new MyPoint(x1, y1), new MyPoint(x2, y2), fontsize, forecolor);
					else if(type==MyRectangle.class)
						MyShape.drawrectangle(g, minx, miny, maxx, maxy, fontsize, forecolor);
						//shape=new MyRectangle(new MyPoint(minx, miny), new MyPoint(maxx, maxy), fontsize, forecolor);
					else if(type==MyCircle.class) {
						int r=(int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
						MyShape.drawcircle(g, x1, y1, r, fontsize, forecolor);
						//shape=new MyCircle(new MyPoint(x1, y1), r, fontsize, forecolor);
					}
					else if(type==MyOval.class)
						MyShape.drawoval(g, minx, miny, maxx, maxy, fontsize, forecolor);
						//shape=new MyOval(new MyPoint(minx, miny), new MyPoint(maxx, maxy), fontsize, forecolor);
				}
				else if(polygon!=null) {    //已经开始画多边形了
					if(!start) {            //还没有画出任何一条边
						if(!nearby(firstx,firsty,x2,y2,5)) {//不在起始点的附近
							start=true;
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
						}
					}
					else {    //已经至少画出了一条边
						//画之前的边
						@SuppressWarnings("unchecked")
						ArrayList<MyPoint> points=(ArrayList<MyPoint>)polygon.parameter.get(2);
						int tmplen=points.size();
						if(tmplen>1) {
							MyPoint firstpoint=points.get(0);
							int tmpfirstx,tmpfirsty,tmpx1,tmpy1,tmpx2=0,tmpy2=0;
							tmpfirstx=tmpx1=firstpoint.x;
							tmpfirsty=tmpy1=firstpoint.y;
							for(int i=1;i<tmplen;i++) {
								MyPoint tmppoint=points.get(i);
								tmpx2=tmppoint.x;
								tmpy2=tmppoint.y;
								MyShape.drawline(g, tmpx1, tmpy1, tmpx2, tmpy2, fontsize, forecolor);
							}
						}
						if(nearby(firstx,firsty,x2,y2,5)) {
							//离初始点很近,完成图形的绘制
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
						}
						else {//添加新点
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
						}
					}
				}
			}
			else if(edit){   //处于编辑的状态
				if(this.getCursor()==move) {//移动
					curshape.move(x2-x1, y2-y1);
					x1=x2;
					y1=y2;
				}
				else if(this.getCursor()==hand)
					((MyPolygon)curshape).changepoint(index, x2, y2);
				else if(this.getCursor()==leftupexpansion) //向左上扩张
					curshape.leftupexpansion(x2, y2);
				else if(this.getCursor()==upexpansion) //向上扩张
					curshape.upexpansion(y2);
				else if(this.getCursor()==rightupexpansion) //向右上扩张
					curshape.rightupexpansion(x2, y2);
				else if(this.getCursor()==rightexpansion) //向右扩张
					curshape.rightexpansion(x2);
				else if(this.getCursor()==rightdownexpansion) //向右下扩张
					curshape.rightdownexpansion(x2, y2);
				else if(this.getCursor()==downexpansion) //向下扩张
					curshape.downexpansion(y2);
				else if(this.getCursor()==leftdownexpansion) //向左下扩张
					curshape.leftdownexpansion(x2, y2);
				else if(this.getCursor()==leftexpansion) //向左扩张
					curshape.leftexpansion(x2);
				curshape.draw(g);
			}
		}*/
	}

	private boolean nearby(int x1, int y1, int x2, int y2, int dis) {
		//判断点(x1,y1)和(x2,y2)的棋盘距离是否在dis以内
		return Math.abs(x1-x2)<=dis&&Math.abs(y1-y2)<=dis;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根
		xpos=e.getX();
		ypos=e.getY();
		location.setText("坐标:"+xpos+","+ypos);
		if(curshape!=null) {
			cursor=curshape.getCursor(xpos, ypos);
			if(curshape.getClass()==MyPolygon.class&&cursor==hand)
				index=((MyPolygon)(curshape)).nearindex(xpos,ypos);
			this.setCursor(cursor);
		}
		else {
			//当前没有图形
			cursor=crisscross;
			this.setCursor(cursor);
		}
	}
	
}
