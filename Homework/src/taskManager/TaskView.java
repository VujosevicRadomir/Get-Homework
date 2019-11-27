package taskManager;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TaskView implements ActionListener {
	Task t;
	
	JFrame f;
	JTextField deadlineTF, shortnameTF, projectTF;
	JLabel deadlineLabel, shortnameLabel, projectLabel, descriptionLabel, assignedLabel;
	JTextArea descriptionTA;
	JButton editButton;
	JList<User> assignList;
	JMenuItem editMI, saveMI;
	JMenuBar mb;
	JComboBox<User> userSelectionCB;
	
	TaskView(Task t){
		this.t = t;
		f = new JFrame();
		
		saveMI = new JMenuItem("Save");
		editMI = new JMenuItem("Edit");
		
		saveMI.addActionListener(this);
		editMI.addActionListener(this);
		
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
		descriptionTA.setBounds(20, 200, 660,250);
		f.add(descriptionTA);
		
		assignedLabel = new JLabel("Assigned to:");
		assignedLabel.setBounds(460, 30, 120, 20);
		assignedLabel.setHorizontalAlignment(JLabel.RIGHT);
		f.add(assignedLabel);		
		
		
		try {
			Vector<User> v = new Vector<User>();
			ResultSet rs = DatabaseHandler.getUsersAssignedToTask(t);
			while(rs.next()) {
				v.add(new User(rs));
			}
			
			assignList = new JList<User>(v);
			assignList.setBounds(450, 60, 200, 20);
			f.add(assignList);
			
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				Vector<User> v2 = new Vector<User>();
				v2.add(null);
				rs = DatabaseHandler.getAllUsers();
				while(rs.next()) {
					User u = new User(rs);
					if(!u.equals(Main.currentUser));
					v2.add(u);
				}
				userSelectionCB = new JComboBox<User>(v2);
				userSelectionCB.setBounds(450, 100, 200, 20);
				userSelectionCB.setEditable(false);
				userSelectionCB.setVisible(false);
				f.add(userSelectionCB);
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
			setEditable(true);
			saveMI.setEnabled(true);
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				userSelectionCB.setVisible(true);
			}
		}else if(e.getSource() == saveMI) {
			
			setEditable(false);
			saveMI.setEnabled(false);
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				userSelectionCB.setVisible(false);
				User u = (User)userSelectionCB.getSelectedItem();
				t.asignedTo = u == null ? 0 : u.id;
			}
			updateTask();
		}
	}
	
	
	
}
