package frame;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	Image icon;
	final String title="Graphic";
	MainMenuBar mmb;
	ColorToolBar ctb;

	JRootPane menupanel;
	JPanel toolpanel;
	FontPanel fontpanel;
	ContentPanel contentpanel;
	ShapesPanel shapepanel;
	JPanel locationpanel;
	
	Toolkit tk;
	int WIDTH;
	int HEIGHT;
	
	MainFrame(){
		//获取屏幕尺寸
		tk=Toolkit.getDefaultToolkit();
		Dimension d=tk.getScreenSize();
		WIDTH=3*d.width/4;
		HEIGHT=3*d.height/4;
		
		//窗体常规设置
		setTitle(title);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(WIDTH/6, HEIGHT/6);
		icon=Toolkit.getDefaultToolkit().getImage(getClass().getResource("additions/frameicon.png"));
		/*//获取工程路径并设置窗体图标
		File f=new File("");
		String path="\\src\\frame\\additions";
		try {
			path=f.getCanonicalPath()+path;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			icon=ImageIO.read(new File(path+"\\frameicon.png"));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		setIconImage(icon);
		
		
		//设置布局、面板
		setLayout(new BorderLayout());
		JPanel jp=new JPanel();
		jp.setLayout(new BorderLayout());
		menupanel=new JRootPane();
		toolpanel=new JPanel();
		fontpanel=new FontPanel();
		/*String movecursor="\\movecursor.png";
		try {
			Image cimage=ImageIO.read(new File(path+movecursor));
			Cursor cursor = tk.createCustomCursor(cimage, new Point(10, 10), "norm");
			jp.setCursor(cursor);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		
		FlowLayout fl=new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		toolpanel.setLayout(fl);
		shapepanel= new ShapesPanel();
		locationpanel=new JPanel();
		
		//初始化菜单栏和工具条
		mmb=new MainMenuBar();
		ctb=new ColorToolBar();
		
		//初始化内容面板
		contentpanel=new ContentPanel();
		
		//初始化坐标面板
		locationpanel.setPreferredSize(new Dimension(0,25));
		locationpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		locationpanel.add(contentpanel.location);
		
		//添加内容
		menupanel.setJMenuBar(mmb);
		toolpanel.add(ctb);
		toolpanel.add(fontpanel);
		
		jp.add(menupanel,BorderLayout.NORTH);
		jp.add(toolpanel,BorderLayout.CENTER);

		add(jp,BorderLayout.NORTH);
		add(shapepanel,BorderLayout.WEST);
		add(contentpanel,BorderLayout.CENTER);
		add(locationpanel,BorderLayout.SOUTH);
		
		setvisible();
	}
	
	void setvisible() {
		this.setVisible(true);
		contentpanel.initial();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame mf=new MainFrame();
	}
}
