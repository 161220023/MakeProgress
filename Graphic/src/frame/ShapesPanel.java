package frame;

import shape.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

@SuppressWarnings("serial")
class ShapeButton extends JButton{
	MyShape shape;
	Border defaultborder;
	ShapeButton(MyShape shape){
		this.shape=shape;
		setBorder(null);
		defaultborder=getBorder();
	}
}

@SuppressWarnings("serial")
public class ShapesPanel extends JPanel{
	ButtonGroup bg;

	ShapeButton pencil;
	ShapeButton eraser;
	ShapeButton sampler;
	//ShapeButton paintbucket;
	//JButton paintbucket;
	ShapeButton string;
	
	ShapeButton line;
	ShapeButton rect;
	ShapeButton circle;
	ShapeButton oval;
	ShapeButton polygon;
	ShapeButton bsplinecurve;

	ShapesPanel(){
		//������屳��ɫ
		this.setBackground(new Color(241,255,255));
		
		//��ʼ��
		bg=new ButtonGroup();
		
		pencil=new ShapeButton(new MyPencil());
		eraser=new ShapeButton(new MyEraser());
		sampler=new ShapeButton(new MySampler());
		//paintbucket=new ShapeButto
		string=new ShapeButton(new MyString());
		
		line=new ShapeButton(new MyLine());
		rect=new ShapeButton(new MyRectangle());
		circle=new ShapeButton(new MyCircle());
		oval=new ShapeButton(new MyOval());
		polygon=new ShapeButton(new MyPolygon());
		bsplinecurve=new ShapeButton(new MyBSplineCurve());
		
		//������ѡ��С
		pencil.setPreferredSize(new Dimension(20,20));
		eraser.setPreferredSize(new Dimension(20,20));
		sampler.setPreferredSize(new Dimension(20,20));
		string.setPreferredSize(new Dimension(20,20));
		
		line.setPreferredSize(new Dimension(20,20));
		rect.setPreferredSize(new Dimension(20,20));
		oval.setPreferredSize(new Dimension(20,20));
		circle.setPreferredSize(new Dimension(20,20));
		polygon.setPreferredSize(new Dimension(20,20));
		bsplinecurve.setPreferredSize(new Dimension(20,20));
		
		@SuppressWarnings("rawtypes")
		Class c=getClass();
		ImageIcon pencilicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/pencil.jpg")));
		ImageIcon erasericon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/eraser.jpg")));
		ImageIcon samplericon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/sampler.jpg")));
		ImageIcon stringicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/string.jpg")));
		ImageIcon lineicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/line.jpg")));
		ImageIcon recticon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/rectangle.jpg")));
		ImageIcon ovalicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/oval.jpg")));
		ImageIcon circleicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/circle.jpg")));
		ImageIcon polygonicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/polygon.jpg")));
		ImageIcon bsplinecurveicon=new ImageIcon(Toolkit.getDefaultToolkit().getImage(c.getResource("/shape/additions/bsplinecurve.jpg")));
		
		pencil.setIcon(pencilicon);
		eraser.setIcon(erasericon);
		sampler.setIcon(samplericon);
		string.setIcon(stringicon);
		
		line.setIcon(lineicon);
		rect.setIcon(recticon);
		oval.setIcon(ovalicon);
		circle.setIcon(circleicon);
		polygon.setIcon(polygonicon);
		bsplinecurve.setIcon(bsplinecurveicon);
		
		//������ʾ
		pencil.setToolTipText("Ǧ��");
		eraser.setToolTipText("��Ƥ��");
		sampler.setToolTipText("��ɫȡ����");
		string.setToolTipText("�ı�");
		
		line.setToolTipText("ֱ��");
		rect.setToolTipText("����");
		oval.setToolTipText("��Բ");
		circle.setToolTipText("Բ");
		polygon.setToolTipText("�����");
		bsplinecurve.setToolTipText("B��������");
		
		//���ò���
		setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		setPreferredSize(new Dimension(55,100));
		
		//��Ӱ�ť
		bg.add(pencil);
		bg.add(eraser);
		bg.add(sampler);
		bg.add(string);
		
		bg.add(line);
		bg.add(rect);
		bg.add(circle);
		bg.add(oval);
		bg.add(polygon);
		bg.add(bsplinecurve);
		
		add(pencil);
		add(eraser);
		add(sampler);
		add(string);
		
		add(line);
		add(rect);
		add(circle);
		add(oval);
		add(polygon);
		add(bsplinecurve);
		
		//����Ĭ��ͼ��
		line.setSelected(true);
		ContentPanel.shape=line.shape;
		line.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//����¼�������
		actionshape(pencil);
		actionshape(eraser);
		actionshape(sampler);
		
		//Ҫ���ı���ť����ѡ�������������,δѡ������������
		actionshape(string);
		
		actionshape(line);
		actionshape(rect);
		actionshape(oval);
		actionshape(circle);
		actionshape(polygon);
		actionshape(bsplinecurve);
	}
	
	void actionshape(ShapeButton sb) {
		sb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO �Զ����ɵķ������
				sb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO �Զ����ɵķ������
				//���û�б�ѡ��,������Ϊԭ���ı߿�
				//�����ѡ��,������Ϊ���߿�
				if(!sb.isSelected())
					sb.setBorder(sb.defaultborder);
				else
					sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				//���ǰһ����polygon������պϻ�����û�дﵽ����֮��ľ���Ҫ����ȡ��
				ContentPanel.curshape=null;  //������״Ҫ���õ�ǰ��״Ϊnull
				ContentPanel.polygon=null;   //ȡ�������������
				ContentPanel.bcurve=null;    //ȡ��������B��������
				sb.setSelected(true);
				if(sb.shape instanceof MyPolygon)   //����Ҫ�������
					ContentPanel.Polygonflag=true;
				else
					ContentPanel.Polygonflag=false; //���Ƕ����
				if(sb.shape instanceof MyBSplineCurve)
					ContentPanel.bsplinecurveflag=true;
				else
					ContentPanel.bsplinecurveflag=false;
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
				
				//��������ͼ��
				ContentPanel.shape=sb.shape;
				
				//��ͬһ�����������ť����Ϊδѡ��,���ػ�Ĭ�ϱ߿�
				Enumeration<AbstractButton> shapes=bg.getElements();
				while(shapes.hasMoreElements()) {
					AbstractButton shape=shapes.nextElement();
					if(shape!=sb) {
						shape.setSelected(false);
						shape.setBorder(sb.defaultborder);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO �Զ����ɵķ������
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}
		});
	}
}
