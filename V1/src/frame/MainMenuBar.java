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
		//��ʼ��
		file=new JMenu("�ļ�");
		help=new JMenu("����");
		create=new JMenuItem("�½�");
		open=new JMenuItem("��");
		save=new JMenuItem("����");
		saveas=new JMenuItem("���Ϊ");
		
		operation=new JMenuItem("����ָ��");
		helpcontent=new JMenuItem("����Graphic");
		
		//��������
		file.setFont(new Font("Dialog", 0, 14));
		help.setFont(new Font("Dialog", 0, 14));
		create.setFont(new Font("Dialog", 0, 14));
		open.setFont(new Font("Dialog", 0, 14));
		save.setFont(new Font("Dialog", 0, 14));
		saveas.setFont(new Font("Dialog", 0, 14));
		operation.setFont(new Font("Dialog",0,14));
		helpcontent.setFont(new Font("Dialog", 0, 14));
		
		//���ÿ�ݼ�
		file.setMnemonic('F');
		help.setMnemonic('H');
		create.setMnemonic('c');
		open.setMnemonic('o');
		save.setMnemonic('s');
		saveas.setMnemonic('a');
		
		//������ʽ����
		fl=new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		
		//file��������
		file.add(create);
		file.add(open);
		file.add(save);
		file.add(saveas);
		
		//help��������
		help.add(operation);
		help.add(helpcontent);
		
		add(file);
		add(help);
		
	}
}
