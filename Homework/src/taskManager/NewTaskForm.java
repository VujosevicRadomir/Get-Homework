package taskManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.impl.*;

public class NewTaskForm {
	JFrame f;
	JLabel deadlineLabel, shortnameLabel, projectLabel, descriptionLabel, assignedLabel, errLabel;
	JTextField shortnameTF, projectTF;
	JTextArea descriptionTA;
	JButton confirmBtn, cancelBtn;
	JDatePickerImpl datePicker;
	UtilDateModel model;
	JComboBox<User> users;
	AdminUI parentUI;

	public NewTaskForm(AdminUI ui) {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		parentUI = ui;

		shortnameLabel = new JLabel("Shortname: ");
		shortnameLabel.setBounds(20, 20, 100, 20);

		shortnameTF = new JTextField();
		shortnameTF.setBounds(150, 20, 250, 20);

		projectLabel = new JLabel("Project: ");
		projectLabel.setBounds(20, 60, 100, 20);

		projectTF = new JTextField();
		projectTF.setBounds(150, 60, 250, 20);

		deadlineLabel = new JLabel("Deadline: ");
		deadlineLabel.setBounds(20, 100, 100, 20);

		model = new UtilDateModel();		
		model.setDate(2020, 0, 1);
		model.setSelected(true);
		datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
		datePicker.setBounds(150, 100, 250, 20);

		assignedLabel = new JLabel("Assign To: ");
		assignedLabel.setBounds(20, 140, 100, 20);

		try {
			Vector<User> v = new Vector<User>();
			v.add(null);
			ResultSet rs = DatabaseHandler.getAllUsers();
			while (rs.next()) {
				v.add(new User(rs));
			}
			users = new JComboBox<User>(v);
			users.setBounds(150, 140, 250, 20);
			f.add(users);

		} catch (SQLException e) {
			System.err.println(e);
		}
		descriptionLabel = new JLabel("Description: ");
		descriptionLabel.setBounds(20, 200, 100, 20);

		descriptionTA = new JTextArea();
		descriptionTA.setBounds(20, 220, 460, 350);

		errLabel = new JLabel("");
		errLabel.setBounds(20, 590, 400, 20);

		confirmBtn = new JButton("Confirm");
		confirmBtn.setBounds(50, 640, 100, 20);
		confirmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (shortnameTF.getText().trim().length() == 0 || projectTF.getText().trim().length() == 0
						|| descriptionTA.getText().trim().length() == 0) {
					errLabel.setText("Please fill out all of the fields");
					return;
				}
				User u = (User) users.getSelectedItem();
				int assignedTo = u == null ? 0 : u.id;
				Date deadlineDate = new Date(model.getValue().getTime());

				Task t = new Task("new", deadlineDate, descriptionTA.getText(), shortnameTF.getText(),
						projectTF.getText(), assignedTo);
				try {
					DatabaseHandler.insertTaskIntoDB(t);
					parentUI.tasksModel.addElement(t);
					f.setVisible(false);
					f.dispose();
				} catch (SQLException ex) {
					errLabel.setText("Error while creating the task");
					System.err.println(ex);
					System.exit(1);
				}
			}
		});

		cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(300, 640, 100, 20);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setVisible(false);
				f.dispose();
			}
		});

		f.add(shortnameLabel);
		f.add(projectLabel);
		f.add(deadlineLabel);
		f.add(descriptionLabel);
		f.add(assignedLabel);
		f.add(shortnameTF);
		f.add(projectTF);
		f.add(descriptionTA);
		f.add(datePicker);
		f.add(confirmBtn);
		f.add(cancelBtn);
		f.add(errLabel);

		f.setLayout(null);
		f.setSize(500, 750);
		f.setVisible(true);
	}
}
