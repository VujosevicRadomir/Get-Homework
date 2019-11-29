package taskManager;

import java.sql.ResultSet;
import java.sql.SQLException;



public class User {
	String username;
	String pw;
	String email;
	String name;
	String surname;
	int id;
	
	enum UserStatus{
		Regular, Administrator;
	}
	UserStatus status;
	
	User(String username, String pw, String email ,String name, String surname, String status){
		this.username = username;
		this.pw = pw;
		this.email = email;
		this.name = name;
		this.surname = surname;
		
		if(status.trim().equalsIgnoreCase("administrator")) {
			this.status = UserStatus.Administrator;
		}else {
			this.status = UserStatus.Regular;
		}
	}
	
	User(ResultSet rs){
		try {
			this.username = rs.getString(1);
			this.pw = rs.getString(2);
			this.email = rs.getString(3);
			this.name = rs.getString(4);
			this.surname = rs.getString(5);
			this.id = rs.getInt(7);
			
			if(rs.getString(6).trim().equalsIgnoreCase("administrator")) {
				this.status = UserStatus.Administrator;
			}else {
				this.status = UserStatus.Regular;
			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public boolean equals(User u) {
		if(u == null) return false;
		return id == u.id;
	}
	
	public String toString() {
		return name + " " + surname;
	}
	
	public static User createRootUser() {
		User u = new User("root", ".", "root", "root", "root", "administrator");
		u.id = 0;
		return u;
	}
}
