package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener {
	public Color forecolor=null;      //颜色
	public MyShape curshape=null;	 //当前形状
	public int fontsize=0;            //线粗细
	public MyPolygon polygon=null;           //当前是否在画多边形
	public MyBSplineCurve bcurve=null;       //当前是否在画B样条曲线
	public Graphics2D g;
	
	FilePopMenu fpm;

	public ArrayList<MyShape> shapes;
	int firstx,firsty;

	boolean modify=false;
	boolean start=false;
	boolean edit=false;
	
	public ShapeType shapetype;
	
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
	@SuppressWarnings("rawtypes")
	static Class cl=ContentPanel.class;
	static Image cimage=tk.getImage(cl.getResource("additions/rotate.png"));
	public static Cursor rotate = tk.createCustomCursor(cimage, new Point(30, 30), "rotate");
	
	static Image paintimage=tk.getImage(cl.getResource("additions/paint.png"));
	public static Cursor paint=tk.createCustomCursor(paintimage, new Point(30,30), "paint");
	
	Cursor cursor;
	
	int x1,x2,y1,y2;
	int xpos,ypos;
	JLabel location;
	
	ContentPanel(){
		//还需要添加粗细,字体,画笔等方法
		//fpm=new FilePopMenu();
		location=new JLabel();
		shapes=new ArrayList<MyShape>();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setBackground(Color.WHITE);
		this.setSize(new Dimension(726,350));
	}
	
	public void initial() {
		g=(Graphics2D)this.getGraphics();	
	}
	
	@Override
	public void repaint() {
		super.paint(g);
		if(shapes==null) return;
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
		if(cursor==normal) {
			int len=shapes.size();
			int tmpx=e.getX();
			int tmpy=e.getY();
			for(int i=0;i<len;i++) {
				MyShape tmpshape=shapes.get(i);
				if(tmpshape.inboarder(tmpx, tmpy)) {
					curshape=tmpshape;
					//移到最上面
					shapes.remove(i);
					shapes.add(curshape);
					curshape.draw(g);
					break;
				}
			}
		}
		else if(cursor==crisscross&&polygon!=null) {//在画多边形
			if(e.getClickCount()==2) {
				int len=polygon.countpoints();
				if(len>1) {
					polygon.addscale();
					shapes.add(polygon);
					polygon.draw(g);
				}
				polygon=null;
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//区分当前的鼠标形状
		if(cursor==normal) {
			curshape=null;
			return;
		}
		else if(cursor==paint) {//填充
			x1=e.getX();
			y1=e.getY();
			int len=shapes.size();
			for(int i=0;i<len;i++) {
				MyShape tmpshape=shapes.get(i);
				if(tmpshape.ininternal(x1, y1)) {
					tmpshape.fillup(forecolor, g);
					shapes.remove(i);
					shapes.add(tmpshape);
					//填充当前图形，退出
					return;
				}
			}
			//没有点到任何图形内部
			return;
		}
		else if(cursor!=crisscross) {//不是画图形,移动、缩放、旋转
			//处于编辑状态,记录下此刻的坐标
			x1=e.getX();
			y1=e.getY();
		}
		//crisscross是画图
		//区分多边形、曲线和其他形状
		else {//画图
			switch(shapetype) {
			case LINE:
			case RECT:
			case CIRCLE:
			case OVAL:
				x1=e.getX();
				y1=e.getY();
				break;
			case POLYGON://画多边形
				if(polygon==null) {
					x1=e.getX();
					y1=e.getY();
					polygon=new MyPolygon(fontsize,forecolor);
					polygon.addpoint(new MyPoint(x1, y1));
					firstx=x1;
					firsty=y1;
					start=false;
				}
				break;
			case BSPLINECURVE:
				
				break;
			default:
				System.out.println("未知类型");
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根
		repaint();
		x2=e.getX();
		y2=e.getY();
		if(x1==x2&&y1==y2) return;
		//看释放时鼠标是什么形状(也即点击时鼠标是什么形状)
		if(cursor==normal) return;
		else if(cursor==paint) return;
		else if(cursor!=crisscross) {//编辑状态
			if(curshape==null) {
				System.out.println("Curshape should not be null but it is.");
				System.out.println("Or it should not be edit but it is.");
				return;
			}
			if(cursor==move)
				curshape.move(x2-x1, y2-y1);
			else {
				if(curshape instanceof MyPolygon && cursor==hand)
					((MyPolygon)(curshape)).changepoint(index, x2, y2);
				//还有更多情况
				else
					curshape.changeshape(cursor, x2, y2);
			}
			curshape.draw(g);
		}
		else {//画图
			MyShape tmpshape=null;
			int tmpminx=Math.min(x1, x2);
			int tmpmaxx=Math.max(x1, x2);
			int tmpminy=Math.min(y1, y2);
			int tmpmaxy=Math.max(y1, y2);
			switch(shapetype) {
			case LINE:
				tmpshape=new MyLine(new MyPoint(x1,y1),new MyPoint(x2,y2),fontsize,forecolor);
				break;
			case RECT:
				tmpshape=new MyRectangle(new MyPoint(tmpminx,tmpminy),new MyPoint(tmpmaxx,tmpmaxy),fontsize,forecolor);
				break;
			case CIRCLE:
				int r=(int)(Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
				tmpshape=new MyCircle(new MyPoint(x1,y1),r,fontsize,forecolor);
				break;
			case OVAL:
				tmpshape=new MyOval(new MyPoint(tmpminx,tmpminy),new MyPoint(tmpmaxx,tmpmaxy),fontsize,forecolor);
				break;
			case POLYGON:
				if(polygon!=null) {
					if(!start) {//还没有画出任何一条边
						if(!MyShape.aroundchesspoint(firstx, firsty, x2, y2, 5)) {//不在起始点附近
							start=true;
							polygon.addpoint(new MyPoint(x2, y2));
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
							x1=x2;
							y1=y2;
						}
					}
					else {    //已经至少画出了一条边
						if(MyShape.aroundchesspoint(firstx, firsty, x2, y2, 5)) {//在起始点附近
							//离初始点很近,完成图形的绘制
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
							polygon.addscale();
							shapes.add(polygon);
							polygon.draw(g);
							polygon=null;
						}
						else {//添加新点
							MyPoint point=new MyPoint(x2,y2);
							polygon.addpoint(point);
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
							x1=x2;
							y1=y2;
						}
					}
				}
				return;
			case BSPLINECURVE:
				return;
			default:
				System.out.println("Unknown shape type "+shapetype);
				break;
			}
			if(tmpshape!=null) {
				shapes.add(tmpshape);
				tmpshape.draw(g);
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
		//repaint();
		
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

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO 自动生成的方法存根
		xpos=e.getX();
		ypos=e.getY();
		location.setText("坐标:"+xpos+","+ypos);
		
		//判断图形button
		//得到鼠标形状
		switch(shapetype) {
		case NORMAL://编辑状态
			if(curshape!=null) {//当前选中了某个图形，判断鼠标位置与其的关系
				cursor=curshape.getCursor(xpos, ypos);
				if(curshape instanceof MyPolygon && cursor==hand)//在多边形某个顶点附近
					index=((MyPolygon)(curshape)).nearindex(xpos, ypos);
				//更多情况
			}
			else
				cursor=normal;
			break;
		case PAINT://填充状态
			cursor=paint;
			break;
		default:
			cursor=crisscross;
		}
		this.setCursor(cursor);
	}

	public void addpolygon() {
		//将当前未画完的polygon添加到shapes中并且设置curshape为polygon
		if(polygon!=null) {
			if(polygon.countpoints()<=1) {
				polygon=null;
				return;
			}
			shapes.add(polygon);
			curshape=polygon;
			polygon=null;
		}
	}
	
	public void addbsplinecurve() {
		if(bcurve!=null) {
			/*if(bcurve.countpoints()<=1) {
				bcurve=null;
				return;
			}*/
			shapes.add(bcurve);
			curshape=bcurve;
			bcurve=null;
		}
	}
	
	public void savepic() {//保存
		//如果已有文件的url则保存至原文件
		//如果没有则"另存为"
		
	}
	
	public void newfile() {//新建
		//如果当前已经有未保存的文件,询问用户是否要保存,若是则调用savepic
		//清空画布内容
	}
	
	public void saveas() {//另存为
		//打开文件对话框,让用户选择保存在哪里
	}
	
	public void open() {//打开文件
		//打开之前判断是否有未经保存的文件,若有则询问用户是否保存,若是则调用savepic
		//清空画布,打开文件对话框让用户选择文件
	}
}
