package taskManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;

public class UI{
	JLabel usrLabel, emailLabel, nameLabel , statusLabel, taskLabel;
	JList<Task> taskList;
	JFrame f;
	UI(){
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		usrLabel = new JLabel(Main.currentUser.username);
		usrLabel.setBounds(20, 20, 200, 20);
		
		emailLabel = new JLabel(Main.currentUser.email);
		emailLabel.setBounds(20, 50, 200, 20);
		
		nameLabel = new JLabel(Main.currentUser.name + " " + Main.currentUser.surname);
		nameLabel.setBounds(20, 80, 200, 20);
		
		statusLabel = new JLabel(Main.currentUser.status.name(), JLabel.RIGHT);
		statusLabel.setBounds(350, 20, 200, 20);
		
		taskLabel = new JLabel("My tasks");
		taskLabel.setBounds(20, 150, 200, 20);
		
		Vector<Task> tasks = new Vector<Task>();
		try {
			ResultSet rs = DatabaseHandler.getTasksAssignedToUser(Main.currentUser);
			
			while(rs.next()) {
				tasks.add(new Task(rs));
			}
		}catch(SQLException e) {
			System.err.println(e);
			System.exit(0);
		}
		
		taskList = new JList<Task>(tasks);
		taskList.setBounds(20, 200, 560, 280);
		
		taskList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() >= 2) {
					new TaskView(taskList.getSelectedValue());
				}
			}
		});
		
		
		
		f.setSize(600, 550);
		f.setLayout(null);
		f.setVisible(true);
		
	}
}
