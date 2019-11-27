package taskManager;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
	Date deadline;
	String description;
	String shortname;
	String project;
	int asignedTo;
	int id;
	
	enum TaskStatus{
		New, ongoing, finished
	}
	TaskStatus status;
	
	Task(String status, Date deadline, String description, String shortname, String project, int asignedTo){
		this.deadline = deadline;
		this.description = description;
		this.shortname = shortname;
		this.project = project;
		this.asignedTo = asignedTo;
		
		if(status.trim().equalsIgnoreCase("new")) {
			this.status = TaskStatus.New;
		}else if(status.equals("ongoing")) {
			this.status = TaskStatus.ongoing;
		}else {
			this.status = TaskStatus.finished;
		}
	}
	
	Task(ResultSet rs){
		try {
			this.deadline = rs.getDate(2);
			this.description = rs.getString(3);
			this.shortname = rs.getString(4);
			this.project = rs.getString(5);
			this.asignedTo = rs.getInt(6);
			this.id = rs.getInt(7);
			
			if(rs.getString(1).trim().equalsIgnoreCase("new")) {
				this.status = TaskStatus.New;
			}else if(rs.getString(1).equals("ongoing")) {
				this.status = TaskStatus.ongoing;
			}else {
				this.status = TaskStatus.finished;
			}
		}catch(SQLException e){
			System.err.println(e);
		}
	}
	
	
	
	public String toString() {
		return shortname + " deadline: " + deadline + " part of project: " + project;
	}
}
