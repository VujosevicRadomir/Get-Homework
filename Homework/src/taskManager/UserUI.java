package taskManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;



public class UserUI extends UI {
	JLabel usrLabel, emailLabel, nameLabel , statusLabel, taskLabel;
	JList<Task> myTasksList, unassignedTasksList;
	JTabbedPane taskTabs;
	JPanel myTasksPanel, unassignedTasksPanel;
	DefaultListModel<Task> myTasksModel, unassignedTasksModel;
	

	
	UserUI(){
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
		
		taskTabs = new JTabbedPane();
		taskTabs.setBounds(20, 200, 560, 280);
		
		myTasksPanel = new JPanel();
		myTasksPanel.setLayout(null);
		
		unassignedTasksPanel = new JPanel();
		unassignedTasksPanel.setLayout(null);
		
		try {
			myTasksModel = new DefaultListModel<Task>();
			ResultSet rs = DatabaseHandler.getTasksAssignedToUser(Main.currentUser);
			while(rs.next()) {
				myTasksModel.addElement(new Task(rs));
			}
			myTasksList = new JList<Task>(myTasksModel);
			Font f = myTasksList.getFont();
			myTasksList.setFont(new Font("monospaced", f.getStyle(), f.getSize() - 1));
			myTasksList.setBounds(0, 0, 560, 280);
			myTasksList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(me.getClickCount() >= 2) {
						new TaskView(myTasksList.getSelectedValue());
					}
				}
			});
			
			myTasksPanel.add(myTasksList);
			taskTabs.add("My tasks", myTasksPanel);
			
			unassignedTasksModel = new DefaultListModel<Task>();
			rs = DatabaseHandler.getUnassignedTasks();
			while(rs.next()) {
				unassignedTasksModel.addElement(new Task(rs));
			}
			unassignedTasksList = new JList<Task>(unassignedTasksModel);
			f = unassignedTasksList.getFont();
			unassignedTasksList.setFont(new Font("monospaced", f.getStyle(), f.getSize() - 1));
			unassignedTasksList.setBounds(0, 0, 560, 280);
			unassignedTasksList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if(me.getClickCount() >= 2) {
						new TaskView(unassignedTasksList.getSelectedValue());
					}
				}
			});
			unassignedTasksPanel.add(unassignedTasksList);
			taskTabs.add("Unassigned Tasks", unassignedTasksPanel);
			
		}catch(SQLException e) {
			System.err.println(e);
			System.exit(0);
		}
		
		
		f.setSize(600, 550);
		f.setLayout(null);
		f.setVisible(true);
	}
	
	public void deleteTaskFromLists(Task t) {
		deleteTaskFromListModel(t, myTasksModel);
		deleteTaskFromListModel(t, unassignedTasksModel);
	}
	
	public void deleteTaskFromListModel(Task t, DefaultListModel<Task> model) {
		for(int i = 0; i < model.getSize(); i++) {
			if(t.equals(model.getElementAt(i))) {
				model.remove(i);
			}
		}
	}
}
