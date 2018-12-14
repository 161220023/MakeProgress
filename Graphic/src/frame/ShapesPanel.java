package frame;

import shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

@SuppressWarnings("serial")
public class ShapesPanel extends JPanel{
	ButtonGroup bg;

	JButton normal;
	JButton paintbucket;
	
	JButton line;
	JButton rect;
	JButton circle;
	JButton oval;
	JButton polygon;
	JButton bsplinecurve;
	
	Toolkit tk;
	
	ContentPanel contpa;

	ShapesPanel(ContentPanel contpa){
		
		this.contpa=contpa;
		
		tk=Toolkit.getDefaultToolkit();
		//设置面板背景色
		this.setBackground(new Color(241,255,255));
		
		//初始化
		bg=new ButtonGroup();
		normal=new JButton();
		paintbucket=new JButton();
		
		line=new JButton();
		rect=new JButton();
		circle=new JButton();
		oval=new JButton();
		polygon=new JButton();
		bsplinecurve=new JButton();
		
		
		normal.setBorder(null);
		paintbucket.setBorder(null);
		line.setBorder(null);
		rect.setBorder(null);
		circle.setBorder(null);
		oval.setBorder(null);
		polygon.setBorder(null);
		bsplinecurve.setBorder(null);
		
		
		//设置首选大小
		normal.setPreferredSize(new Dimension(20,20));
		paintbucket.setPreferredSize(new Dimension(20,20));
		
		line.setPreferredSize(new Dimension(20,20));
		rect.setPreferredSize(new Dimension(20,20));
		oval.setPreferredSize(new Dimension(20,20));
		circle.setPreferredSize(new Dimension(20,20));
		polygon.setPreferredSize(new Dimension(20,20));
		bsplinecurve.setPreferredSize(new Dimension(20,20));
		
		
		@SuppressWarnings("rawtypes")
		Class c=getClass();
		ImageIcon normalicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/normal.jpg")));
		ImageIcon painticon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/paint.jpg")));
		
		ImageIcon lineicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/line.jpg")));
		ImageIcon recticon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/rectangle.jpg")));
		ImageIcon ovalicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/oval.jpg")));
		ImageIcon circleicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/circle.jpg")));
		ImageIcon polygonicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/polygon.jpg")));
		ImageIcon bsplinecurveicon=new ImageIcon(tk.getImage(c.getResource("/shape/additions/bsplinecurve.jpg")));

		
		normal.setIcon(normalicon);
		paintbucket.setIcon(painticon);
		
		line.setIcon(lineicon);
		rect.setIcon(recticon);
		oval.setIcon(ovalicon);
		circle.setIcon(circleicon);
		polygon.setIcon(polygonicon);
		bsplinecurve.setIcon(bsplinecurveicon);
		
		//设置提示
		normal.setToolTipText("编辑");
		paintbucket.setToolTipText("颜料桶");
		
		line.setToolTipText("直线");
		rect.setToolTipText("矩形");
		oval.setToolTipText("椭圆");
		circle.setToolTipText("圆");
		polygon.setToolTipText("多边形");
		bsplinecurve.setToolTipText("B样条曲线");
		
		
		//设置布局
		setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		setPreferredSize(new Dimension(55,100));
		
		
		//添加按钮
		bg.add(normal);
		bg.add(paintbucket);
		
		bg.add(line);
		bg.add(rect);
		bg.add(circle);
		bg.add(oval);
		bg.add(polygon);
		bg.add(bsplinecurve);


		add(normal);
		add(paintbucket);
		
		add(line);
		add(rect);
		add(circle);
		add(oval);
		add(polygon);
		add(bsplinecurve);
		
		//设置默认图形
		line.setSelected(true);
		contpa.shapetype=ShapeType.LINE;
		line.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//添加事件侦听器
		actionshape(normal);
		actionshape(paintbucket);
		
		actionshape(line);
		actionshape(rect);
		actionshape(oval);
		actionshape(circle);
		actionshape(polygon);
		actionshape(bsplinecurve);
	}
	
	void actionshape(JButton sb) {
		sb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				sb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				//如果没有被选中,则设置为原来的边框
				//如果被选中,则设置为凹边框
				if(!sb.isSelected())
					sb.setBorder(null);
				else
					sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				//如果前面在画多边形或曲线,则polygon或bslinecurve不为null,要将其添加进去
				if(contpa.polygon!=null) contpa.addpolygon();
				if(contpa.bcurve!=null)  contpa.addbsplinecurve();
				contpa.curshape=null;  //更换形状要设置当前形状为null
				sb.setSelected(true);
				if(sb==line) 				contpa.shapetype=ShapeType.LINE;
				else if(sb==rect) 			contpa.shapetype=ShapeType.RECT;
				else if(sb==circle) 		contpa.shapetype=ShapeType.CIRCLE;
				else if(sb==oval)			contpa.shapetype=ShapeType.OVAL;
				else if(sb==polygon) 		contpa.shapetype=ShapeType.POLYGON;
				else if(sb==bsplinecurve) 	contpa.shapetype=ShapeType.BSPLINECURVE;
				else if(sb==normal)			contpa.shapetype=ShapeType.NORMAL;
				else if(sb==paintbucket)	contpa.shapetype=ShapeType.PAINT;
				else						contpa.shapetype=null;
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
				
				//将同一个组的其他按钮设置为未选中,且重绘默认边框
				Enumeration<AbstractButton> shapes=bg.getElements();
				while(shapes.hasMoreElements()) {
					AbstractButton shape=shapes.nextElement();
					if(shape!=sb) {
						shape.setSelected(false);
						shape.setBorder(null);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}
		});
	}
}
