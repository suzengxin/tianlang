DROP TABLE IF EXISTS USER_INFO;
 
CREATE TABLE USER_INFO (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	username VARCHAR(250) NOT NULL,
	password VARCHAR(250) NOT NULL
);
