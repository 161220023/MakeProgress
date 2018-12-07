package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentPanel extends JScrollPane {
	public DrawPanel drawpanel;
	public static boolean Polygonflag=false;
	ContentPanel(JButton forecolorbutton, JButton backcolorbutton, ShapeButton shapebutton, JLabel fontlabel){
		drawpanel=new DrawPanel(forecolorbutton, backcolorbutton, shapebutton, fontlabel);
		setViewportView(drawpanel);
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	public void initial() {
		drawpanel.initial();
	}
	
	@Override
	public void repaint() {
		if(drawpanel!=null)
			drawpanel.initial();
	}
}

@SuppressWarnings("serial")
class DrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	Graphics g;
	
	FilePopMenu fpm;
	
	JButton forecolorbutton;
	JButton backcolorbutton;
	ShapeButton shapebutton;
	JLabel fontlabel;
	
	/*Image movecursorimage;
	Image extendcursorimage;
	Image reversecursorimage;
	
	Cursor movecursor;
	Cursor extendcursor;
	Cursor reversecursor;*/
	
	ArrayList<MyShape> shapes;
	MyPolygon polygon=null;
	MyShape curshape=null;
	int firstx,firsty;
	int minx,miny,maxx,maxy;
	boolean modify=false;
	boolean start=false;
	boolean edit=false;
	int index=0;
	//����Ҫ������...������,��С��
	
	//Toolkit tk;
	Cursor normal=new Cursor(0);            //����������״
	Cursor crisscross=new Cursor(1);  		//ʮ
	Cursor text=new Cursor(2);  			//| �����ı�
	Cursor loading=new Cursor(3);  			//����  o
	Cursor rightupexpansion=new Cursor(4);  //��б�� /
	Cursor rightdownexpansion=new Cursor(5);//��б�� \
	Cursor leftupexpansion=new Cursor(6);  	//��б�� \
	Cursor leftdownexpansion=new Cursor(7); //��б�� /
	Cursor upexpansion=new Cursor(8);  		//��������
	Cursor downexpansion=new Cursor(9); 	//��������
	Cursor leftexpansion=new Cursor(10);	//<-->��������
	Cursor rightexpansion=new Cursor(11);	//<-->��������
	Cursor hand=new Cursor(12);				//����
	Cursor move=new Cursor(13);				//�ƶ�
	
	
	int x1,x2,y1,y2;
	int xpos,ypos;
	JLabel location;
	
	DrawPanel(JButton forecolorbutton, JButton backcolorbutton, ShapeButton shapebutton, JLabel fontlabel){
		//����Ҫ��Ӵ�ϸ,����,���ʵȷ���
		this.forecolorbutton=forecolorbutton;
		this.backcolorbutton=backcolorbutton;
		this.shapebutton=shapebutton;
		this.fontlabel=fontlabel;
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
		// TODO �Զ����ɵķ������
		//��֮��û���ƶ�Ȼ���ͷ���Ϊ���
		//��press,��release,���click
		if(e.getClickCount()==2) {//˫�����ɶ����
			if(ContentPanel.Polygonflag) {
				if(polygon!=null) {
					//�жϵ㼯��С�Ƿ񳬹�1
					@SuppressWarnings("unchecked")
					ArrayList<MyPoint> points=(ArrayList<MyPoint>)polygon.parameter.get(2);
					int len=points.size();
					if(len>1) {//�������β�ֹ��һ����ʼ�㣬�����˶����
						polygon.addminmax(new MyPoint(minx, miny), new MyPoint(maxx, maxy));
						polygon.addscale();
						shapes.add(polygon);
						curshape=polygon;
						MyShape.drawline(g, x1, y1, firstx, firsty, 1, g.getColor());
						edit=true;
					}
					polygon=null;
				}
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO �Զ����ɵķ������
		if(this.getCursor()==crisscross) {
			//������ɫ
			g.setColor(forecolorbutton.getBackground());
			g.setFont(fontlabel.getFont());
			if(!ContentPanel.Polygonflag) {
				x1=e.getX();
				y1=e.getY();
			}
			else {
				if(polygon==null) {
					x1=e.getX();
					y1=e.getY();
					polygon=new MyPolygon(g.getFont().getSize(),g.getColor());
					polygon.addpoint(new MyPoint(x1, y1));
					firstx=minx=maxx=x1;
					firsty=miny=maxy=y1;
					start=false;
				}
			}
		}
		else if(curshape!=null) {
			edit=true;
			x1=e.getX();
			y1=e.getY();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO �Զ����ɵķ������
		x2=e.getX();
		y2=e.getY();
		if(e.isPopupTrigger())
			fpm.show(e.getComponent(), x2, y2);
		else {
			if(!edit) {
				if(x1==x2&&y1==y2)
					return;
				if(!ContentPanel.Polygonflag) {
					@SuppressWarnings("rawtypes")
					Class type=shapebutton.shape.getClass();
					MyShape shape=null;
					int minx=Math.min(x1, x2);
					int miny=Math.min(y1, y2);
					int	maxx=Math.max(x1, x2);
					int maxy=Math.max(y1, y2);
					if(type==MyLine.class)
						shape=new MyLine(new MyPoint(x1, y1), new MyPoint(x2, y2), g.getFont().getSize(), g.getColor());
					else if(type==MyRectangle.class)
						shape=new MyRectangle(new MyPoint(minx, miny), new MyPoint(maxx, maxy), g.getFont().getSize(), g.getColor());
					else if(type==MyCircle.class) {
						int r=(int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
						shape=new MyCircle(new MyPoint(x1, y1), r, g.getFont().getSize(), g.getColor());
					}
					else if(type==MyOval.class)
						shape=new MyOval(new MyPoint(minx, miny), new MyPoint(maxx, maxy), g.getFont().getSize(), g.getColor());
					if(shape!=null) {
						shapes.add(shape);
						shape.draw(g);
						curshape=shape;
					}
				}
				else if(polygon!=null) {
					if(!start) {
						if(!nearby(firstx,firsty,x2,y2,5)) {//������ʼ��ĸ���
							start=true;
							polygon.addpoint(new MyPoint(x2, y2));
							MyShape.drawline(g, x1, y1, x2, y2, 1, g.getColor());
							x1=x2;
							y1=y2;
							if(x2>maxx) maxx=x2;
							else if(x2<minx) minx=x2;
							if(y2>maxy) maxy=y2;
							else if(y2<miny) miny=y2;
						}
					}
					else {
						if(nearby(firstx,firsty,x2,y2,5)) {
							//���ʼ��ܽ�,���ͼ�εĻ���
							MyShape.drawline(g, x1, y1, firstx, firsty, 1, g.getColor());
							polygon.addminmax(new MyPoint(minx, miny), new MyPoint(maxx, maxy));
							shapes.add(polygon);
							curshape=polygon;
							polygon=null;
						}
						else {//����µ�
							MyPoint point=new MyPoint(x2,y2);
							polygon.addpoint(point);
							MyShape.drawline(g, x1, y1, x2, y2, 1, g.getColor());
							x1=x2;
							y1=y2;
							if(x2>maxx) maxx=x2;
							else if(x2<minx) minx=x2;
							if(y2>maxy) maxy=y2;
							else if(y2<miny) miny=y2;
						}
					}
				}
			}
			else {//���ڱ༭��״̬
				if(this.getCursor()==move) //�ƶ�
					curshape.move(x2-x1, y2-y1);
				else if(this.getCursor()==hand)
					((MyPolygon)curshape).changepoint(index, x2, y2);
				else if(this.getCursor()==leftupexpansion) //����������
					curshape.leftupexpansion(x2, y2);
				else if(this.getCursor()==upexpansion) //��������
					curshape.upexpansion(y2);
				else if(this.getCursor()==rightupexpansion) //����������
					curshape.rightupexpansion(x2, y2);
				else if(this.getCursor()==rightexpansion) //��������
					curshape.rightexpansion(x2);
				else if(this.getCursor()==rightdownexpansion) //����������
					curshape.rightdownexpansion(x2, y2);
				else if(this.getCursor()==downexpansion) //��������
					curshape.downexpansion(y2);
				else if(this.getCursor()==leftdownexpansion) //����������
					curshape.leftdownexpansion(x2, y2);
				else if(this.getCursor()==leftexpansion) //��������
					curshape.leftexpansion(x2);
				curshape.draw(g);
				edit=false;
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO �Զ����ɵķ������
		@SuppressWarnings("rawtypes")
		Class shapetype=shapebutton.shape.getClass();
		if(shapetype==MyLine.class||shapetype==MyRectangle.class||shapetype==MyOval.class||shapetype==MyCircle.class||shapetype==MyPolygon.class)
			this.setCursor(crisscross);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO �Զ����ɵķ������
		location.setText("");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO �Զ����ɵķ������
		xpos=e.getX();
		ypos=e.getY();
		location.setText("����:" + xpos + "," + ypos);
		
		//�жϵ�ǰ������״��curshape,�������crisscross,curshape��Ϊ��,���ǶԵ�ǰͼ�ν��б༭
		//��ʱ��ʵ���϶�,������ͷ���
		/*Cursor cursor=this.getCursor();
		if(cursor!=crisscross&&curshape!=null) {
			//�ƶ�ͼ�λ��߱༭ͼ��
			if(curshape.getClass()==MyLine.class) {
				
			}
			else if(curshape.getClass()==MyRectangle.class) {
				
			}
			else if(curshape.getClass()==MyCircle.class) {
				
			}
			else if(curshape.getClass()==MyOval.class) {
				
			}
		}*/
		//repaint();
		//shapebutton.shape.draw(g, x1, y1, x2, y2, 1);
		//g.drawLine(x1, y1, x2, y2);
		//g.dr
		//Graphics g=new Graphics();
		//��ֱ��
		
		
		//������ͼ��
		
		
		//Ǧ�ʻ���
		//g.drawLine(x1, y1, x2, y2);
		//repaint();
		//shapebutton.shape.draw(g, x1, y1, x2, y2);
		/*x1=x2;
		y1=y2;*/
	}

	private boolean nearby(int x1, int y1, int x2, int y2, int dis) {
		//�жϵ�(x1,y1)��(x2,y2)�����̾����Ƿ���dis����
		return Math.abs(x1-x2)<=dis&&Math.abs(y1-y2)<=dis;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO �Զ����ɵķ������
		xpos=e.getX();
		ypos=e.getY();
		location.setText("����:"+xpos+","+ypos);
		if(curshape!=null) {
			//�ж����λ���Ƿ��ڵ�ǰͼ�εĶ˵���Χ
			@SuppressWarnings("rawtypes")
			Class c=curshape.getClass();
			if(c==MyLine.class) {
				MyPoint point1=(MyPoint)curshape.parameter.get(0);
				MyPoint point2=(MyPoint)curshape.parameter.get(1);
				int point1x=point1.x;
				int point1y=point1.y;
				int point2x=point2.x;
				int point2y=point2.y;
				if(nearby(point1x, point1y, xpos, ypos, 5))
					this.setCursor(leftdownexpansion);
				else if(nearby(point2x, point2y, xpos, ypos, 5))
					this.setCursor(rightupexpansion);
				else if(curshape.ininternal(xpos, ypos))
					this.setCursor(move);
				else
					this.setCursor(crisscross);
			}
			else {
				int meanx=0,meany=0,minx=0,miny=0,maxx=0,maxy=0;
				if(c==MyCircle.class) {
					MyPoint point0=(MyPoint)curshape.parameter.get(0);
					int r=(int)curshape.parameter.get(1);
					meanx=point0.x;
					meany=point0.y;
					minx=meanx-r;
					maxx=meanx+r;
					miny=meany-r;
					maxy=meany+r;
					if(((MyCircle)curshape).inleftup(xpos, ypos))
						this.setCursor(leftupexpansion);
					else if(((MyCircle)curshape).inrightup(xpos, ypos))
						this.setCursor(rightupexpansion);
					else if(((MyCircle)curshape).inrightdown(xpos, ypos))
						this.setCursor(rightdownexpansion);
					else if(((MyCircle)curshape).inleftdown(xpos, ypos))
						this.setCursor(leftdownexpansion);
					else if(curshape.ininternal(xpos, ypos))
						this.setCursor(move);
					else
						this.setCursor(crisscross);
					return;
				}
				else if(c==MyOval.class||c==MyRectangle.class){//��������Բ������
					MyPoint pointmin=(MyPoint)curshape.parameter.get(0);
					MyPoint pointmax=(MyPoint)curshape.parameter.get(1);
					minx=pointmin.x;
					miny=pointmin.y;
					maxx=pointmax.x;
					maxy=pointmax.y;
					meanx=(minx+maxx)/2;
					meany=(miny+maxy)/2;
				}
				else if(c==MyPolygon.class) {//�ܹ�4����4����
					MyPoint pointmin=(MyPoint)curshape.parameter.get(3);
					MyPoint pointmax=(MyPoint)curshape.parameter.get(4);
					minx=pointmin.x;
					miny=pointmin.y;
					maxx=pointmax.x;
					maxy=pointmax.y;
					meanx=(minx+maxx)/2;
					meany=(miny+maxy)/2;
					@SuppressWarnings("unchecked")
					ArrayList<MyPoint> points=(ArrayList<MyPoint>)curshape.parameter.get(2);
					int len=points.size();
					for(int i=0;i<len;i++) {
						MyPoint curpoint=points.get(i);
						if(nearby(curpoint.x,curpoint.y,xpos,ypos,5)) {
							this.setCursor(hand);
							index=i;
							return;
						}
					}
				}
				if(nearby(minx,miny,xpos,ypos,5))
					this.setCursor(leftupexpansion);
				else if(nearby(maxx,miny,xpos,ypos,5))
					this.setCursor(rightupexpansion);
				else if(nearby(maxx,maxy,xpos,ypos,5))
					this.setCursor(rightdownexpansion);
				else if(nearby(minx,maxy,xpos,ypos,5))
					this.setCursor(leftdownexpansion);
				else if(nearby(meanx,miny,xpos,ypos,5))
					this.setCursor(upexpansion);
				else if(nearby(maxx,meany,xpos,ypos,5))
					this.setCursor(rightexpansion);
				else if(nearby(meanx,maxy,xpos,ypos,5))
					this.setCursor(downexpansion);
				else if(nearby(minx,meany,xpos,ypos,5))
					this.setCursor(leftexpansion);
				else if(curshape.ininternal(xpos, ypos))
					this.setCursor(move);
				else
					this.setCursor(crisscross);
			}
		}
	}
	
}
