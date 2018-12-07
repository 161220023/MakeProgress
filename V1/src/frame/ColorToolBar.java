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
	
	//������ɫ��ť
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
		//��ʼ��
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
		
		editcolor=new JButton("��ɫ");
		
		foregroundcolorbutton.setBackground(Color.BLACK);
		backgroundcolorbutton.setBackground(Color.WHITE);
		
		//����Ĭ��Ϊǰ��ɫ,������ɫ����Ϊ͸��
		backgroundcolorbutton.setOpaque(false);
		
		//������ʾ
		foregroundcolorbutton.setToolTipText("ǰ��ɫ");
		backgroundcolorbutton.setToolTipText("����ɫ");

		editcolor.setFont(new Font("Dialog",0,8));
		
		//���ð�ť��С
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
		
		//���ò���
		FlowLayout fl=new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		
		//������
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
		
		//���༭��ɫ��ť����¼�
		editcolor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(foregroundcolorbutton.isOpaque()) {//��ǰ��ɫѡȡ��ɫ
					Color forecolor=JColorChooser.showDialog(new ColorToolBar(), "�༭��ɫ", Color.BLACK);
					foregroundcolorbutton.setBackground(forecolor);	
				}
				else {//������ɫѡȡ��ɫ
					Color backcolor=JColorChooser.showDialog(new ColorToolBar(), "�༭��ɫ", Color.BLACK);
					backgroundcolorbutton.setBackground(backcolor);
				}
			}
		});
		
		//��ǰ������ɫ��ť����¼�
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
		
		//����������ɫ��ť������
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
				if(foregroundcolorbutton.isOpaque()) //��ǰ��ɫѡȡ��ɫ
					foregroundcolorbutton.setBackground(cb.color);
				else //������ɫѡȡ��ɫ
					backgroundcolorbutton.setBackground(cb.color);
			}
		});
		cb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO �Զ����ɵķ������
				cb.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO �Զ����ɵķ������
				cb.setBorder(defaultborder);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				cb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			
		});
	}
}
