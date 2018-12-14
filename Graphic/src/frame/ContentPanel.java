package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener {
	public Color forecolor=null;      //��ɫ
	public MyShape curshape=null;	 //��ǰ��״
	public int fontsize=0;            //�ߴ�ϸ
	public MyPolygon polygon=null;           //��ǰ�Ƿ��ڻ������
	public MyBSplineCurve bcurve=null;       //��ǰ�Ƿ��ڻ�B��������
	public Graphics2D g;
	
	FilePopMenu fpm;

	public ArrayList<MyShape> shapes;
	int firstx,firsty;

	boolean modify=false;
	boolean start=false;
	boolean edit=false;
	
	public ShapeType shapetype;
	
	int index=0;
	//����Ҫ������...������,��С��

	public static Cursor normal=new Cursor(0);            //����������״
	
	public static Cursor crisscross=new Cursor(1);  		//ʮ
	
	public static Cursor text=new Cursor(2);  			//| �����ı�
	public static Cursor loading=new Cursor(3);  			//����  o
	public static Cursor rightupexpansion=new Cursor(4);  //��б�� /
	public static Cursor rightdownexpansion=new Cursor(5);//��б�� \
	public static Cursor leftupexpansion=new Cursor(6);  	//��б�� \
	public static Cursor leftdownexpansion=new Cursor(7); //��б�� /
	public static Cursor upexpansion=new Cursor(8);  		//��������
	public static Cursor downexpansion=new Cursor(9); 	//��������
	public static Cursor leftexpansion=new Cursor(10);	//<-->��������
	public static Cursor rightexpansion=new Cursor(11);	//<-->��������
	public static Cursor hand=new Cursor(12);				//����
	public static Cursor move=new Cursor(13);				//�ƶ�
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
		//����Ҫ��Ӵ�ϸ,����,���ʵȷ���
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
		// TODO �Զ����ɵķ������
		//��֮��û���ƶ�Ȼ���ͷ���Ϊ���
		//��press,��release,���click
		if(cursor==normal) {
			int len=shapes.size();
			int tmpx=e.getX();
			int tmpy=e.getY();
			for(int i=0;i<len;i++) {
				MyShape tmpshape=shapes.get(i);
				if(tmpshape.inboarder(tmpx, tmpy)) {
					curshape=tmpshape;
					//�Ƶ�������
					shapes.remove(i);
					shapes.add(curshape);
					curshape.draw(g);
					break;
				}
			}
		}
		else if(cursor==crisscross&&polygon!=null) {//�ڻ������
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
		//���ֵ�ǰ�������״
		if(cursor==normal) {
			curshape=null;
			return;
		}
		else if(cursor==paint) {//���
			x1=e.getX();
			y1=e.getY();
			int len=shapes.size();
			for(int i=0;i<len;i++) {
				MyShape tmpshape=shapes.get(i);
				if(tmpshape.ininternal(x1, y1)) {
					tmpshape.fillup(forecolor, g);
					shapes.remove(i);
					shapes.add(tmpshape);
					//��䵱ǰͼ�Σ��˳�
					return;
				}
			}
			//û�е㵽�κ�ͼ���ڲ�
			return;
		}
		else if(cursor!=crisscross) {//���ǻ�ͼ��,�ƶ������š���ת
			//���ڱ༭״̬,��¼�´˿̵�����
			x1=e.getX();
			y1=e.getY();
		}
		//crisscross�ǻ�ͼ
		//���ֶ���Ρ����ߺ�������״
		else {//��ͼ
			switch(shapetype) {
			case LINE:
			case RECT:
			case CIRCLE:
			case OVAL:
				x1=e.getX();
				y1=e.getY();
				break;
			case POLYGON://�������
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
				System.out.println("δ֪����");
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO �Զ����ɵķ������
		repaint();
		x2=e.getX();
		y2=e.getY();
		if(x1==x2&&y1==y2) return;
		//���ͷ�ʱ�����ʲô��״(Ҳ�����ʱ�����ʲô��״)
		if(cursor==normal) return;
		else if(cursor==paint) return;
		else if(cursor!=crisscross) {//�༭״̬
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
				//���и������
				else
					curshape.changeshape(cursor, x2, y2);
			}
			curshape.draw(g);
		}
		else {//��ͼ
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
					if(!start) {//��û�л����κ�һ����
						if(!MyShape.aroundchesspoint(firstx, firsty, x2, y2, 5)) {//������ʼ�㸽��
							start=true;
							polygon.addpoint(new MyPoint(x2, y2));
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
							x1=x2;
							y1=y2;
						}
					}
					else {    //�Ѿ����ٻ�����һ����
						if(MyShape.aroundchesspoint(firstx, firsty, x2, y2, 5)) {//����ʼ�㸽��
							//���ʼ��ܽ�,���ͼ�εĻ���
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
							polygon.addscale();
							shapes.add(polygon);
							polygon.draw(g);
							polygon=null;
						}
						else {//����µ�
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
		// TODO �Զ����ɵķ������
		//@SuppressWarnings("rawtypes")
		//Class shapetype=shape.getClass();
		//if(shapetype==MyLine.class||shapetype==MyRectangle.class||shapetype==MyOval.class||shapetype==MyCircle.class||shapetype==MyPolygon.class)
		//	this.setCursor(crisscross);
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
		//repaint();
		
		/*repaint();
		x2=e.getX();
		y2=e.getY();
		if(e.isPopupTrigger())
			fpm.show(e.getComponent(), x2, y2);
		else {
			if(!edit&&this.getCursor()==crisscross) {   //�����ڱ༭״̬,���ڻ�ͼ״̬
				if(!ContentPanel.Polygonflag) {   //���ǻ������
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
				else if(polygon!=null) {    //�Ѿ���ʼ���������
					if(!start) {            //��û�л����κ�һ����
						if(!nearby(firstx,firsty,x2,y2,5)) {//������ʼ��ĸ���
							start=true;
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
						}
					}
					else {    //�Ѿ����ٻ�����һ����
						//��֮ǰ�ı�
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
							//���ʼ��ܽ�,���ͼ�εĻ���
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
						}
						else {//����µ�
							MyShape.drawline(g, x1, y1, x2, y2, fontsize, forecolor);
						}
					}
				}
			}
			else if(edit){   //���ڱ༭��״̬
				if(this.getCursor()==move) {//�ƶ�
					curshape.move(x2-x1, y2-y1);
					x1=x2;
					y1=y2;
				}
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
			}
		}*/
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO �Զ����ɵķ������
		xpos=e.getX();
		ypos=e.getY();
		location.setText("����:"+xpos+","+ypos);
		
		//�ж�ͼ��button
		//�õ������״
		switch(shapetype) {
		case NORMAL://�༭״̬
			if(curshape!=null) {//��ǰѡ����ĳ��ͼ�Σ��ж����λ������Ĺ�ϵ
				cursor=curshape.getCursor(xpos, ypos);
				if(curshape instanceof MyPolygon && cursor==hand)//�ڶ����ĳ�����㸽��
					index=((MyPolygon)(curshape)).nearindex(xpos, ypos);
				//�������
			}
			else
				cursor=normal;
			break;
		case PAINT://���״̬
			cursor=paint;
			break;
		default:
			cursor=crisscross;
		}
		this.setCursor(cursor);
	}

	public void addpolygon() {
		//����ǰδ�����polygon��ӵ�shapes�в�������curshapeΪpolygon
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
	
	public void savepic() {//����
		//��������ļ���url�򱣴���ԭ�ļ�
		//���û����"���Ϊ"
		
	}
	
	public void newfile() {//�½�
		//�����ǰ�Ѿ���δ������ļ�,ѯ���û��Ƿ�Ҫ����,���������savepic
		//��ջ�������
	}
	
	public void saveas() {//���Ϊ
		//���ļ��Ի���,���û�ѡ�񱣴�������
	}
	
	public void open() {//���ļ�
		//��֮ǰ�ж��Ƿ���δ��������ļ�,������ѯ���û��Ƿ񱣴�,���������savepic
		//��ջ���,���ļ��Ի������û�ѡ���ļ�
	}
}
