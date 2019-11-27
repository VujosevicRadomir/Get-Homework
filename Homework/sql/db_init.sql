drop database user_tasks;
create database user_tasks;

use user_tasks;

create table users(
	username varchar(20) not null,
	pw varchar(100) not null,
	email varchar(50),
	name varchar (50) not null,
	surname varchar (20) not null,
	status varchar (20),
	
	personid int not null auto_increment,
	primary key (personid)
	
);



create table tasks(
    status varchar (30),
    deadline date,
    description varchar(10000),
    shortname varchar(30),
    project varchar(30),

	asignedTo int,
    
    taskid int not null auto_increment,
    primary key (taskid)
);

