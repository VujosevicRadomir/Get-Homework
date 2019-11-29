package taskManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DatabaseHandler {
	
	public static Connection cnt;
	
	public static void ConnectToDB() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			cnt = DriverManager.getConnection("jdbc:mysql://localhost/user_tasks?user=root&password=Root!012");
		}catch(ClassNotFoundException e) {
			System.err.println(e);
		}
	}
	
	public static boolean login(String username, String pw) throws SQLException {
		try {
			ConnectToDB();
			
			
			if(username.equals("root") && pw.equals(".")) {
				Main.currentUser = User.createRootUser();
				return true;
			}
			
			String query = "select * from user_tasks.users where username='" + username + "'";
			ResultSet rs = cnt.createStatement().executeQuery(query);
			
			if(!rs.next()) {
				//User not found in db
				return false;
			}
			String pwInDB = rs.getString("pw");
			
			if(!encryptedPassword(pw).equals(pwInDB) 
					&& !pw.equals(pwInDB)//exists only for testing purposes -
											//it lets us write an sql query to add users with a plain text pw							
					) {
				return false;
			}
			Main.currentUser = new User(rs);
			
			return true;
			
		}catch(SQLException e) {
			System.err.println(e);
			return false;
		}
	}
	
	public static ResultSet getAllTasks() throws SQLException{
		String query = "select * from tasks";
		ResultSet rs = cnt.createStatement().executeQuery(query);
		return rs;
	}
	
	public static ResultSet getAllUsers() throws SQLException{
		String query = "select * from users";
		ResultSet rs = cnt.createStatement().executeQuery(query);
		return rs;
	}
	
	public static ResultSet getTasksAssignedToUser(User u) throws SQLException{
		String query = "select * from user_tasks.tasks where asignedTo=" + u.id;
		ResultSet rs = cnt.createStatement().executeQuery(query);
		return rs;
	}
	public static ResultSet getUnassignedTasks() throws SQLException{
		String query = "select * from user_tasks.tasks where asignedTo is null;";
		ResultSet rs = cnt.createStatement().executeQuery(query);
		return rs;
	}
	
	public static ResultSet getUsersAssignedToTask(Task t) throws SQLException{
		String query = "select * from user_tasks.users where personid=" + t.asignedTo;
		ResultSet rs = cnt.createStatement().executeQuery(query);
		return rs;
	}
	
	public static void updateTaskInDB(Task t)throws SQLException{
		String query = "update user_tasks.tasks"
				+ " set status=?,"
				+ "deadline=?,"
				+ "description=?,"
				+ "shortname=?,"
				+ "project=?,"
				+ "asignedTo=?"
				+ " where taskid=?";

		
		PreparedStatement stmt = cnt.prepareStatement(query);
		stmt.setString(1, t.status.name());
		stmt.setDate(2, t.deadline);
		stmt.setString(3, t.description);
		stmt.setString(4, t.shortname);
		stmt.setString(5, t.project);
		if(t.asignedTo == 0) {
			stmt.setNull(6, java.sql.Types.INTEGER);
		}else {
			stmt.setInt(6, t.asignedTo);
		}
		stmt.setInt(7, t.id);
		
		stmt.executeUpdate();
	}
	
	public static void insertTaskIntoDB(Task t) throws SQLException{
		String query = "insert into user_tasks.tasks"
				+ " (status, deadline, description, shortname, project, asignedTo) "
				+ "values (?, ?, ?, ?, ?, ?);";
		
		PreparedStatement stmt = cnt.prepareStatement(query);
		stmt.setString(1, t.status.name());
		stmt.setDate(2, t.deadline);
		stmt.setString(3, t.description);
		stmt.setString(4, t.shortname);
		stmt.setString(5, t.project);
		if(t.asignedTo == 0) {
			stmt.setNull(6, java.sql.Types.INTEGER);
		}else {
			stmt.setInt(6, t.asignedTo);
		}
		stmt.executeUpdate();
	}
	
	public static void insertUserIntoDB(User u) throws SQLException{
		String query = "insert into users" + 
				" (username, pw, email, name, surname, status)"
				+ " values (?, ?, ?, ?, ?, ?);";
		
		PreparedStatement stmt = cnt.prepareStatement(query);
		stmt.setString(1, u.username);
		stmt.setString(2, encryptedPassword(u.pw));
		stmt.setString(3, u.email);
		stmt.setString(4, u.name);
		stmt.setString(5, u.surname);
		stmt.setString(6, u.status.name());
		
		stmt.executeUpdate();
	}
	
	public static String encryptedPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte [] bytes = md.digest();
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		}catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	public static void deleteTaskInDB(Task t) throws SQLException{
		String query = "delete from tasks where taskid=" + t.id + ";";
		cnt.createStatement().executeUpdate(query);
	}
}
