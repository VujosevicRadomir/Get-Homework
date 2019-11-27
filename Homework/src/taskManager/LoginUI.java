package taskManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

public class LoginUI implements ActionListener {
	JFrame f;
	JLabel usrLabel, pwLabel, errLabel;
	JTextField loginTF;
	JPasswordField passwordPF;
	

	
	LoginUI(){
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		usrLabel = new JLabel("Username");
		usrLabel.setBounds(50, 50, 100, 30);
		f.add(usrLabel);
		
		pwLabel = new JLabel("Password");
		pwLabel.setBounds(50, 120, 100, 30);
		f.add(pwLabel);
		
		errLabel = new JLabel("");
		errLabel.setBounds(50, 160, 400, 30);
		f.add(errLabel);
		
		loginTF = new JTextField();
		loginTF.setBounds(150, 50, 250, 30);
		f.add(loginTF);
		
		passwordPF = new JPasswordField();
		passwordPF.setBounds(150, 120, 250, 30);
		f.add(passwordPF);
		
		JButton b = new JButton("Login");
		b.setBounds(200, 200, 80, 30);
		f.add(b);
		
		b.addActionListener(this);
		
		f.setSize(450, 300);
		f.setLayout(null);;
		f.setVisible(true);
	}



	
	public void actionPerformed(ActionEvent e) {
		if(passwordPF.getPassword().length == 0 || loginTF.getText().length() == 0) {
			errLabel.setText("Enter both username and password");
			return;
		}
		boolean result = false;
		try {
			result = DatabaseHandler.login(loginTF.getText(), new String(passwordPF.getPassword()));
		}catch(SQLException ex) {
			System.err.println(ex);
			return;
		}
		
		if(!result) {
			errLabel.setText("Please check your username and password");
		}else {
			f.setVisible(false);
			f.dispose();
			if(Main.currentUser.status == User.UserStatus.Administrator) {
				new AdminUI();
			}
			else {
				new RegUI();
			}
		}
	}
		
	
}
