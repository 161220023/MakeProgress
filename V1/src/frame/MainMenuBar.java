package frame;
import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

@SuppressWarnings("serial")
public class MainMenuBar extends JMenuBar{
	JMenu file;
	JMenu help;
	JMenuItem create;
	JMenuItem open;
	JMenuItem save;
	JMenuItem saveas;
	JMenuItem operation;
	JMenuItem helpcontent;
	
	FlowLayout fl;
	MainMenuBar(){
		//初始化
		file=new JMenu("文件");
		help=new JMenu("帮助");
		create=new JMenuItem("新建");
		open=new JMenuItem("打开");
		save=new JMenuItem("保存");
		saveas=new JMenuItem("另存为");
		
		operation=new JMenuItem("操作指南");
		helpcontent=new JMenuItem("关于Graphic");
		
		//设置字体
		file.setFont(new Font("Dialog", 0, 14));
		help.setFont(new Font("Dialog", 0, 14));
		create.setFont(new Font("Dialog", 0, 14));
		open.setFont(new Font("Dialog", 0, 14));
		save.setFont(new Font("Dialog", 0, 14));
		saveas.setFont(new Font("Dialog", 0, 14));
		operation.setFont(new Font("Dialog",0,14));
		helpcontent.setFont(new Font("Dialog", 0, 14));
		
		//设置快捷键
		file.setMnemonic('F');
		help.setMnemonic('H');
		create.setMnemonic('c');
		open.setMnemonic('o');
		save.setMnemonic('s');
		saveas.setMnemonic('a');
		
		//设置流式布局
		fl=new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		
		//file添加面板条
		file.add(create);
		file.add(open);
		file.add(save);
		file.add(saveas);
		
		//help添加面板条
		help.add(operation);
		help.add(helpcontent);
		
		add(file);
		add(help);
		
	}
}
