package taskManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NewUserForm {
	JFrame f;
	JLabel usrLabel, emailLabel, nameLabel, surnameLabel, statusLabel, errLabel, pwLabel;
	JTextField usrTF, emailTF, nameTF, surnameTF;
	JPasswordField pwField;
	JRadioButton admRB, regRB;
	ButtonGroup statusBG;
	JButton confirmBtn, cancelBtn;
	AdminUI parentUI;
	
	public NewUserForm(AdminUI ui) {
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		parentUI = ui;
		
		usrLabel = new JLabel("Username: ");
		usrLabel.setBounds(20, 20, 100, 20);
		
		pwLabel = new JLabel("Password");
		pwLabel.setBounds(20, 70, 100, 20);
		
		emailLabel = new JLabel("Email: ");
		emailLabel.setBounds(20, 120, 100, 20);
		
		nameLabel = new JLabel("Name: ");
		nameLabel.setBounds(20, 170, 100, 20);
		
		surnameLabel = new JLabel("Surname: ");
		surnameLabel.setBounds(20, 220, 100, 20);
		
		statusLabel = new JLabel("User status: ");
		statusLabel.setBounds(20, 270, 100, 20);
		
		usrTF = new JTextField();
		usrTF.setBounds(150, 20, 300, 30);
		
		pwField = new JPasswordField();
		pwField.setBounds(150, 70, 300, 30);
		
		emailTF = new JTextField();
		emailTF.setBounds(150, 120, 300, 30);
		
		nameTF = new JTextField();
		nameTF.setBounds(150, 170, 300, 30);
		
		surnameTF = new JTextField();
		surnameTF.setBounds(150, 220, 300, 30);
		
		admRB = new JRadioButton("Administrator");
		admRB.setBounds(150, 270, 150, 30);
		
		regRB = new JRadioButton("Regular");
		regRB.setBounds(320, 270, 150, 30);
		
		statusBG = new ButtonGroup();
		statusBG.add(regRB);
		statusBG.add(admRB);
		
		errLabel = new JLabel();
		errLabel.setBounds(50, 300, 400, 30);
	
		
		confirmBtn = new JButton("Confirm");
		confirmBtn.setBounds(50, 350, 150, 30);
		confirmBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(nameTF.getText().trim().length() == 0 ||
					emailTF.getText().trim().length() == 0 ||
					surnameTF.getText().trim().length() == 0 ||
					usrTF.getText().trim().length() == 0) {
					errLabel.setText("Please fill in all of the fields");
					return;
				}
				String status;
				if(admRB.isSelected()) status = "Administrator";
				else status = "regular";
				
				User u = new User(usrTF.getText().trim(),
								  new String(pwField.getPassword()),
								  emailTF.getText(),
								  nameTF.getText(),
								  surnameTF.getText(),
								  status
								  );
				
				try {
					DatabaseHandler.insertUserIntoDB(u);
				}catch(SQLException ex) {
					System.err.println(ex);
					errLabel.setText("Error while inserting user in db");
					return;
				}
				
				parentUI.usersModel.addElement(u);
				f.setVisible(false);
				f.dispose();
			}
		});
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(250, 350, 150, 30);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setVisible(false);
				f.dispose();
			}
		});
		

		f.add(usrLabel); f.add(nameLabel);f.add(statusLabel);f.add(surnameLabel);f.add(emailLabel);f.add(errLabel);f.add(pwLabel);
		f.add(usrTF); f.add(nameTF);f.add(surnameTF);f.add(emailTF);
		f.add(admRB); f.add(regRB);
		f.add(confirmBtn); f.add(cancelBtn);
		f.add(pwField);
		
		f.setLayout(null);
		f.setSize(500, 450);
		f.setVisible(true);		
	}
}
