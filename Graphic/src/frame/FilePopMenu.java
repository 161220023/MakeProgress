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
		//初始化
		create=new JMenuItem("新建");
		open=new JMenuItem("打开");
		save=new JMenuItem("保存");
		saveas=new JMenuItem("另存为");
		
		//设置字体
		create.setFont(new Font("Dialog",0,14));
		open.setFont(new Font("Dialog",0,14));
		save.setFont(new Font("Dialog",0,14));
		saveas.setFont(new Font("Dialog",0,14));
		
		//添加到菜单中
		add(create);
		add(open);
		add(save);
		add(saveas);
	}
}
