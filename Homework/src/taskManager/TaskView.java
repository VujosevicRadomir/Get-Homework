package taskManager;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TaskView extends UI implements ActionListener {
	Task t;
	
	JTextField deadlineTF, shortnameTF, projectTF;
	JLabel deadlineLabel, shortnameLabel, projectLabel, descriptionLabel, assignedToLabel, assignUserLabel; 
	JTextArea descriptionTA;
	JButton editButton, deleteButton;
	JList<User> assignList;
	JMenuItem editMI, saveMI;
	JMenuBar mb;
	JComboBox<User> userSelectionCB;
	
	TaskView(Task t){
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.t = t;
		
		saveMI = new JMenuItem("Save");
		editMI = new JMenuItem("Edit");
		
		saveMI.addActionListener(this);
		editMI.addActionListener(this);
		if(Main.currentUser.status == User.UserStatus.Regular && t.id != Main.currentUser.id) {
			editMI.setEnabled(false);
		}
		
		saveMI.setEnabled(false);
		
		mb = new JMenuBar();
		mb.add(saveMI); mb.add(editMI);
		f.setJMenuBar(mb);
		
		deadlineLabel = new JLabel("Deadline:");
		deadlineLabel.setBounds(20, 30, 100, 20);
		f.add(deadlineLabel);
		
		deadlineTF = new JTextField(t.deadline.toString());
		deadlineTF.setBounds(150, 30, 200, 20);
		f.add(deadlineTF);
		
		shortnameLabel = new JLabel("Shortname: ");
		shortnameLabel.setBounds(20, 70, 100, 20);
		f.add(shortnameLabel);
		
		shortnameTF = new JTextField(t.shortname);
		shortnameTF.setBounds(150, 70, 200, 20);
		f.add(shortnameTF);
		
		projectLabel = new JLabel("Project: ");
		projectLabel.setBounds(20, 110, 100, 20);
		f.add(projectLabel);
		
		projectTF = new JTextField(t.project);
		projectTF.setBounds(150, 110, 200, 20);
		f.add(projectTF);
		
		descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(20, 150, 100, 20);
		f.add(descriptionLabel);
		
		descriptionTA = new JTextArea(t.description);
		descriptionTA.setBounds(20, 200, 660,230);
		f.add(descriptionTA);
		
		assignedToLabel = new JLabel("Unassigned");
		assignedToLabel.setBounds(450, 30, 290, 20);
		f.add(assignedToLabel);		
		
		
		try {
			ResultSet rs = DatabaseHandler.getUsersAssignedToTask(t);
			User assignedUser = null;
			if(rs.next()) {
				assignedUser = new User(rs);
				assignedToLabel.setText("Assigned to: " + assignedUser);
			}
			
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				assignUserLabel = new JLabel("Reassign task to:");
				assignUserLabel.setBounds(450, 75, 200, 20);
				assignUserLabel.setVisible(false);
				f.add(assignUserLabel);
				
				Vector<User> v = new Vector<User>();
				if(assignedUser != null) {
					v.add(assignedUser);
				}
				v.add(null);
				rs = DatabaseHandler.getAllUsers();
				while(rs.next()) {
					User u = new User(rs);
					if(!u.equals(assignedUser))
						v.add(u);
				}
				userSelectionCB = new JComboBox<User>(v);
				userSelectionCB.setBounds(450, 100, 200, 20);
				userSelectionCB.setEditable(false);
				userSelectionCB.setVisible(false);
				f.add(userSelectionCB);
				
				deleteButton = new JButton("Delete Task");
				deleteButton.setBounds(450, 150, 200, 30);
				deleteButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							DatabaseHandler.deleteTaskInDB(t);
							Main.currentUI.deleteTaskFromLists(t);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						closeUI();
					}
				});
				f.add(deleteButton);
				
			}
			
		}catch(SQLException e) {
			System.err.println(e);
			System.exit(1);
		}
		
		setEditable(false);
		
		f.setSize(700,500);
		f.setLayout(null);
		f.setVisible(true);
		
	}
	
	public void setEditable(boolean b){
		deadlineTF.setEditable(b);
		shortnameTF.setEditable(b);
		projectTF.setEditable(b);
		descriptionTA.setEditable(b);
	}
	
	public void updateTask() {
		t.description = descriptionTA.getText();
		t.shortname = shortnameTF.getText();
		t.project = projectTF.getText();
		t.deadline = Date.valueOf(deadlineTF.getText());
		try {
			
			DatabaseHandler.updateTaskInDB(t);
		}catch(SQLException e) {
			System.err.println(e);
		}
				
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editMI) {
			editMI.setEnabled(false);
			setEditable(true);
			saveMI.setEnabled(true);
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				assignUserLabel.setVisible(true);
				userSelectionCB.setVisible(true);
			}
		}else if(e.getSource() == saveMI) {
			editMI.setEnabled(true);
			setEditable(false);
			saveMI.setEnabled(false);
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				userSelectionCB.setVisible(false);
				assignUserLabel.setVisible(false);
				User u = (User)userSelectionCB.getSelectedItem();
				if(u ==  null) {
					t.asignedTo = 0;
					assignedToLabel.setText("Unassigned");
				}else {
					t.asignedTo = u.id;
					assignedToLabel.setText("Assigned to: " + u);
				}
				
			}
			updateTask();
		}
	}
	
	
	
}
