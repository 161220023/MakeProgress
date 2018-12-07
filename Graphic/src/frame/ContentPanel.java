package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ContentPanel extends JPanel implements MouseListener, MouseMotionListener {
	public static boolean Polygonflag=false; //�Ƿ�Ҫ�������
	public static boolean bsplinecurveflag=false;//�Ƿ�Ҫ��B��������
	public static Color forecolor=null;      //��ɫ
	public static MyShape shape=null;        //������״
	//public static Font font=null;            //����
	public static MyShape curshape=null;	 //��ǰ��״
	public static int fontsize=0;            //�ߴ�ϸ
	public static MyPolygon polygon=null;           //��ǰ�Ƿ��ڻ������
	public static MyBSplineCurve bcurve=null;       //��ǰ�Ƿ��ڻ�B��������
	Graphics g;
	
	FilePopMenu fpm;

	public ArrayList<MyShape> shapes;
	int firstx,firsty;
	int minx,miny,maxx,maxy;
	boolean modify=false;
	boolean start=false;
	boolean edit=false;
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
	static Image cimage=tk.getImage("E:\\eclipse_workspace\\Graphic\\bin\\frame\\additions\\rotate.png");
	public static Cursor rotate = tk.createCustomCursor(cimage, new Point(30, 30), "norm");
	
	
	Cursor cursor;
	
	int x1,x2,y1,y2;
	int xpos,ypos;
	JLabel location;
	
	ContentPanel(){
		//����Ҫ��Ӵ�ϸ,����,���ʵȷ���
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
			if(ContentPanel.Polygonflag) {    //��ǰѡ���ͼ���Ƕ����
				if(polygon!=null) {           //��ǰ���ڻ������
					//�жϵ㼯��С�Ƿ񳬹�1
					int len=polygon.countpoints();
					if(len>1) {//�������β�ֹ��һ����ʼ�㣬�����˶����
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
		else {//�������ĵط�����ĳ��ͼ�εķ���(�ڸ���),�����õ�ǰͼ��Ϊcurshape
			if(this.cursor==crisscross&&(polygon==null||start==false)) {//�����״��crisscross�ҵ�ǰû���ڻ�����λ�ǰ�����start==false
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
		// TODO �Զ����ɵķ������
		if(this.getCursor()==crisscross) {
			if(!ContentPanel.Polygonflag) {  //���ǻ������
				x1=e.getX();
				y1=e.getY();
			}
			else {//�������
				if(polygon==null) {  //��û��ʼ��
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
		else if(curshape!=null) { //���ڱ༭״̬���ҵ�ǰͼ�β�Ϊ��
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
			if(x1==x2&&y1==y2)
				return;
			//if(!edit&&this.getCursor()==crisscross) {   //�����ڱ༭״̬,���ڻ�ͼ״̬
			if(!edit) {
				if(!ContentPanel.Polygonflag) {   //���ǻ������
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
				else if(polygon!=null) {    //�Ѿ���ʼ���������
					if(!start) {            //��û�л����κ�һ����
						if(!nearby(firstx,firsty,x2,y2,5)) {//������ʼ��ĸ���
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
					else {    //�Ѿ����ٻ�����һ����
						if(nearby(firstx,firsty,x2,y2,5)) {
							//���ʼ��ܽ�,���ͼ�εĻ���
							MyShape.drawline(g, x1, y1, firstx, firsty, fontsize, forecolor);
							polygon.addminmax();
							shapes.add(polygon);
							curshape=polygon;
							polygon=null;
						}
						else {//����µ�
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
			else if(edit){   //���ڱ༭��״̬
				if(this.getCursor()==move) //�ƶ�
					curshape.move(x2-x1, y2-y1);
				else {
					if(curshape.getClass()==MyPolygon.class&&cursor==hand)
						((MyPolygon)(curshape)).changepoint(index, x2, y2);
					curshape.changeshape(cursor, x2, y2);
				}
				curshape.draw(g);
				edit=false;      //�Ѿ��༭���
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
			cursor=curshape.getCursor(xpos, ypos);
			if(curshape.getClass()==MyPolygon.class&&cursor==hand)
				index=((MyPolygon)(curshape)).nearindex(xpos,ypos);
			this.setCursor(cursor);
		}
		else {
			//��ǰû��ͼ��
			cursor=crisscross;
			this.setCursor(cursor);
		}
	}
	
}
