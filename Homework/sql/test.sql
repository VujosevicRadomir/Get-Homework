use user_tasks;

insert into users
(username, pw, email, name, surname, status)
values
('radomir', 'test', 'rad@gmail.com', 'radomir', 'vujosevic', 'administrator'),
('marko', 'test', 'marko@gmail.com', 'marko', 'markovic', 'regular'),
('aleksa', 'test', 'aleksa@gmail.com', 'aleksa', 'aleksic', 'regular'),
('uros', 'test', 'uros@gmail.com', 'uros', 'domazetovic', 'regular'),
('snezana', 'test', 'sneza@gmail.com', 'snezana', 'vukovic', 'regular'),
('sandra', 'test', 'sandra@gmail.com', 'sandra', 'jankovic', 'regular'),
('ana', 'test', 'ana@gmail.com', 'ana', 'milic', 'regular');

insert into tasks
(status, deadline, description, shortname, project, asignedTo)
values
('ongoing', '2019-12-18', 'do a home project for a job at GET', 'homework', 'homework', 1),
('new', '2019-12-6', 'prepare a PP presentation', 'presentation', 'meeting with client', 2),
('new', '2019-12-8', 'compile a list of previously done projects',' work history', 'meeting with client', 2),
('new', '2019-12-19', 'update database', 'dbudate', 'refactoring', 3),
('new', '2019-12-21', 'interview a new job applicant', 'interview', 'new employee', 4),
('new', '2019-12-17', 'find a memory leak', 'mem leak', 'refactoring', 3),
('finished', '2019-10-6', 'lorem ipsum', 'a finished task', 'finished', null),
('ongoing', '2019-12-6', 'create a new Java team', 'make new team', 'new team', 5),
('ongoing', '2019-12-8', 'post ads looking for  employees for the new Java team', 'adverties', 'new team', 5),
('finished', '2019-5-9', 'filler task description', 'filler', 'filler', null),
('finished', '2019-5-12', 'fake description', 'fake task', 'filler', null);

