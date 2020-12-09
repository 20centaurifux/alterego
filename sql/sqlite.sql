create table protocol (
	id INTEGER PRIMARY KEY,
	scheme VARCHAR(8) NOT NULL UNIQUE);

insert into protocol (scheme) values ('gopher');
insert into protocol (scheme) values ('http');
insert into protocol (scheme) values ('https');

create table request (
	id INTEGER PRIMARY KEY,
	timestamp DATE DEFAULT (DATETIME('now', 'utc')),
	address CHAR(64) NOT NULL,
	protocol_id INTEGER NOT NULL,
	path VARCHAR(128) NOT NULL,
	FOREIGN KEY (protocol_id) REFERENCES protocol(id));
