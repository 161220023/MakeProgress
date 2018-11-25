package frame;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
class ColorButton extends JButton{
	Color color;
	ColorButton(int r,int g,int b){
		color=new Color(r,g,b);
		setBackground(color);
	}
	ColorButton(Color color){
		this.color=color;
		setBackground(color);
	}
}

@SuppressWarnings("serial")
public class ColorToolBar extends JToolBar{
	JButton foregroundcolorbutton;
	JButton backgroundcolorbutton;
	JLabel space;
	
	//常用颜色按钮
	ColorButton black;
	ColorButton white;
	
	ColorButton red;
	ColorButton orange;
	ColorButton yellow;
	ColorButton green;
	ColorButton cyan;
	ColorButton blue;
	ColorButton purple;
	Border defaultborder;

	JButton editcolor;
	
	ColorToolBar(){
		//初始化
		foregroundcolorbutton=new JButton();
		backgroundcolorbutton=new JButton();
		space=new JLabel();
		
		black=new ColorButton(Color.BLACK);
		white=new ColorButton(Color.WHITE);
		red=new ColorButton(Color.RED);
		orange=new ColorButton(Color.ORANGE);
		yellow=new ColorButton(Color.YELLOW);
		green=new ColorButton(Color.GREEN);
		cyan=new ColorButton(Color.CYAN);
		blue=new ColorButton(Color.BLUE);
		purple=new ColorButton(160,32,240);
		defaultborder=black.getBorder();
		
		editcolor=new JButton("调色");
		
		foregroundcolorbutton.setBackground(Color.BLACK);
		backgroundcolorbutton.setBackground(Color.WHITE);
		
		//设置默认为前景色,将背景色设置为透明
		backgroundcolorbutton.setOpaque(false);
		
		//设置提示
		foregroundcolorbutton.setToolTipText("前景色");
		backgroundcolorbutton.setToolTipText("背景色");

		editcolor.setFont(new Font("Dialog",0,8));
		
		//设置按钮大小
		Dimension d=editcolor.getPreferredSize();
		d.height=(int)(d.getHeight()<d.getWidth()?d.getHeight():d.getWidth());
		d.width=d.height;

		foregroundcolorbutton.setPreferredSize(d);
		backgroundcolorbutton.setPreferredSize(d);
		space.setPreferredSize(d);
		black.setPreferredSize(d);
		white.setPreferredSize(d);
		red.setPreferredSize(d);
		orange.setPreferredSize(d);
		yellow.setPreferredSize(d);
		green.setPreferredSize(d);
		cyan.setPreferredSize(d);
		blue.setPreferredSize(d);
		purple.setPreferredSize(d);
		
		//设置布局
		FlowLayout fl=new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		
		//添加组件
		add(foregroundcolorbutton);
		add(backgroundcolorbutton);
		add(space);
		add(black);
		add(white);
		add(red);
		add(orange);
		add(yellow);
		add(green);
		add(cyan);
		add(blue);
		add(purple);
		
		add(editcolor);
		
		//给编辑颜色按钮添加事件
		editcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(foregroundcolorbutton.isOpaque()) {//给前景色选取颜色
					Color forecolor=JColorChooser.showDialog(new ColorToolBar(), "编辑颜色", Color.BLACK);
					foregroundcolorbutton.setBackground(forecolor);	
				}
				else {//给背景色选取颜色
					Color backcolor=JColorChooser.showDialog(new ColorToolBar(), "编辑颜色", Color.BLACK);
					backgroundcolorbutton.setBackground(backcolor);
				}
			}
		});
		
		//给前景背景色按钮添加事件
		foregroundcolorbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				foregroundcolorbutton.setOpaque(true);
				backgroundcolorbutton.setOpaque(false);
				backgroundcolorbutton.repaint();
			}
		});
		
		backgroundcolorbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundcolorbutton.setOpaque(true);
				foregroundcolorbutton.setOpaque(false);
				foregroundcolorbutton.repaint();
			}
		});
		
		//给各常用颜色按钮添加组件
		addButtonListener(black);
		addButtonListener(white);
		addButtonListener(red);
		addButtonListener(orange);
		addButtonListener(yellow);
		addButtonListener(green);
		addButtonListener(cyan);
		addButtonListener(blue);
		addButtonListener(purple);
	}
	
	void addButtonListener(ColorButton cb) {
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(foregroundcolorbutton.isOpaque()) //给前景色选取颜色
					foregroundcolorbutton.setBackground(cb.color);
				else //给背景色选取颜色
					backgroundcolorbutton.setBackground(cb.color);
			}
		});
		cb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO 自动生成的方法存根
				cb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO 自动生成的方法存根
				cb.setBorder(defaultborder);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				cb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			
		});
	}
}
