package taskManager;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUI extends UI implements ActionListener{
	JPanel defaultPanel, tasksPanel, usersPanel;
	JTabbedPane tp;
	JList<Task> allTasksList;
	JList<User> allUsersList;
	JButton newUserBtn, newTaskBtn;
	DefaultListModel<Task> tasksModel;
	DefaultListModel<User> usersModel;
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newTaskBtn) {
			new NewTaskForm(this);
		}else if(e.getSource() == newUserBtn) {
			new NewUserForm(this);
		}
	}
	
	AdminUI(){
		super();
		
		
		defaultPanel = new JPanel();
		defaultPanel.setLayout(null);
		defaultPanel.add(nameLabel); defaultPanel.add(emailLabel); defaultPanel.add(usrLabel);
		defaultPanel.add(taskLabel); defaultPanel.add(statusLabel);
		defaultPanel.add(taskList);
		
		tasksPanel = new JPanel();
		tasksPanel.setLayout(null);
		
		newTaskBtn = new JButton("New Task");
		newTaskBtn.setBounds(20, 450, 150, 20);
		newTaskBtn.addActionListener(this);
		tasksPanel.add(newTaskBtn);
		
		usersPanel = new JPanel();
		usersPanel.setLayout(null);
		
		newUserBtn = new JButton("New User");
		newUserBtn.setBounds(20, 450, 150, 20);
		newUserBtn.addActionListener(this);
		usersPanel.add(newUserBtn);
		
		try {
			tasksModel = new DefaultListModel<Task>();
			allTasksList = new JList<Task>(tasksModel);
			
			ResultSet rs = DatabaseHandler.getAllTasks();
			while(rs.next()) {
				tasksModel.addElement(new Task(rs));
			}
			allTasksList.setBounds(20, 20, 560, 400);
			allTasksList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(me.getClickCount() >= 2) {
						new TaskView(allTasksList.getSelectedValue());
					}
				}
			});
			tasksPanel.add(allTasksList);
			
			usersModel = new DefaultListModel<User>();
			allUsersList = new JList<User>(usersModel);
			
			rs = DatabaseHandler.getAllUsers();
			while(rs.next()) {
				usersModel.addElement(new User(rs));
			}
			
			allUsersList.setBounds(20, 20, 560, 400);
			usersPanel.add(allUsersList);
			
		}catch(SQLException e) {
			System.err.println(e);
			System.exit(1);
		}
	
		tp = new JTabbedPane();
		tp.setBounds(0, 0, 600, 550);
		tp.add("My tasks", defaultPanel);
		tp.add("All tasks", tasksPanel);
		tp.add("All users", usersPanel);
		
		f.add(tp);
	}
	
}