package frame;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class FilePopMenu extends JPopupMenu{
	JMenuItem create;
	JMenuItem open;
	JMenuItem save;
	JMenuItem saveas;
	FilePopMenu(){
		//��ʼ��
		create=new JMenuItem("�½�");
		open=new JMenuItem("��");
		save=new JMenuItem("����");
		saveas=new JMenuItem("���Ϊ");
		
		//��������
		create.setFont(new Font("Dialog",0,14));
		open.setFont(new Font("Dialog",0,14));
		save.setFont(new Font("Dialog",0,14));
		saveas.setFont(new Font("Dialog",0,14));
		
		//��ӵ��˵���
		add(create);
		add(open);
		add(save);
		add(saveas);
	}
}
