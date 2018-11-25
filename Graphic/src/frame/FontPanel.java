package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({ "serial", "rawtypes" })
class ListModel extends DefaultComboBoxModel{
	@SuppressWarnings("unchecked")
	ListModel(String[] str){
		for(String s:str) {
			addElement(s);
		}
	}
}

@SuppressWarnings({ "serial", "rawtypes" })
class FontNameList extends JComboBox {
	static String[] name= {"Arial","sans-serif","Helvetica","Tahoma","Verdana"};
	static ListModel listmodel=new ListModel(name);
	String fontname;
	JLabel encased;
	@SuppressWarnings("unchecked")
	FontNameList(JLabel encased){
		super(listmodel);
		fontname="Arial";//默认的字体
		this.encased=encased;
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO 自动生成的方法存根
				fontname=(String)e.getItem();
				Font font=encased.getFont();
				encased.setFont(new Font(fontname,font.getStyle(),font.getSize()));
			}
		});
	}
}

@SuppressWarnings({ "serial", "rawtypes" })
class FontStyleList extends JComboBox {
	static String[] style= {"PLAIN","BOLD","ITALIC"};
	static ListModel listmodel=new ListModel(style);
	int fontstyle;
	JLabel encased;
	@SuppressWarnings("unchecked")
	FontStyleList(JLabel encased){
		super(listmodel);
		fontstyle=Font.PLAIN;//默认的字体风格
		this.encased=encased;
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO 自动生成的方法存根
				String str=(String)e.getItem();
				Font font=encased.getFont();
				switch(str) {
				case "PLAIN":fontstyle=Font.PLAIN;
				case "BOLD":fontstyle=Font.BOLD;
				case "ITALIC":fontstyle=Font.ITALIC;
				}
				encased.setFont(new Font(font.getName(),fontstyle,font.getSize()));
			}
		});
	}
}

@SuppressWarnings({ "serial", "rawtypes" })
class FontSizeList extends JComboBox {
	static String[] size= {"1","2","3","4","5","6","7","8","9","10"};
	static ListModel listmodel=new ListModel(size);
	int fontsize;
	JLabel encased;
	@SuppressWarnings("unchecked")
	FontSizeList(JLabel encased){
		super(listmodel);
		fontsize=1;//默认的字体大小是1
		this.encased=encased;
		this.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO 自动生成的方法存根
				fontsize=Integer.parseInt((String)e.getItem());
				Font font=encased.getFont();
				encased.setFont(new Font(font.getName(),font.getStyle(),fontsize));
			}
			
		});
	}
}

@SuppressWarnings("serial")
public class FontPanel extends JPanel{
	FontNameList fontnamelist;
	FontStyleList fontstylelist;
	FontSizeList fontsizelist;
	
	JLabel encased;
	
	FontPanel(){
		//初始化
		encased=new JLabel();
		encased.setFont(new Font("Arial",Font.PLAIN,1));
		fontnamelist=new FontNameList(encased);
		fontstylelist=new FontStyleList(encased);
		fontsizelist=new FontSizeList(encased);
		
		//添加内容
		add(fontnamelist);
		add(fontstylelist);
		add(fontsizelist);
	
	}
}
