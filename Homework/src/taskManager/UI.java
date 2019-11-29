package taskManager;

import javax.swing.JFrame;

public abstract class UI{
	JFrame f;
	
	public UI(){
		f = new JFrame();
	}
	
	void closeUI(){
		f.setVisible(false);
		f.dispose();
	}
	
	
}
