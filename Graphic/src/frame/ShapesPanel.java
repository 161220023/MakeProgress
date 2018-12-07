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
		//设置面板背景色
		this.setBackground(new Color(241,255,255));
		
		//初始化
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
		
		//设置首选大小
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
		
		//设置提示
		pencil.setToolTipText("铅笔");
		eraser.setToolTipText("橡皮擦");
		sampler.setToolTipText("颜色取样器");
		string.setToolTipText("文本");
		
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
		
		//设置默认图形
		line.setSelected(true);
		ContentPanel.shape=line.shape;
		line.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//添加事件侦听器
		actionshape(pencil);
		actionshape(eraser);
		actionshape(sampler);
		
		//要给文本按钮设置选择启用字体面板,未选择禁用字体面板
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
				// TODO 自动生成的方法存根
				sb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				//如果没有被选中,则设置为原来的边框
				//如果被选中,则设置为凹边框
				if(!sb.isSelected())
					sb.setBorder(sb.defaultborder);
				else
					sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				//如果前一个是polygon则让其闭合或者若没有达到两点之间的距离要求则取消
				ContentPanel.curshape=null;  //更换形状要设置当前形状为null
				ContentPanel.polygon=null;   //取消继续画多边形
				ContentPanel.bcurve=null;    //取消继续画B样条曲线
				sb.setSelected(true);
				if(sb.shape instanceof MyPolygon)   //表明要画多边形
					ContentPanel.Polygonflag=true;
				else
					ContentPanel.Polygonflag=false; //画非多边形
				if(sb.shape instanceof MyBSplineCurve)
					ContentPanel.bsplinecurveflag=true;
				else
					ContentPanel.bsplinecurveflag=false;
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
				
				//设置面板的图形
				ContentPanel.shape=sb.shape;
				
				//将同一个组的其他按钮设置为未选中,且重绘默认边框
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
				// TODO 自动生成的方法存根
				sb.setBorder(BorderFactory.createLoweredBevelBorder());
			}
		});
	}
}
