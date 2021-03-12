DROP TABLE IF EXISTS USER_INFO;
CREATE TABLE USER_INFO (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	username VARCHAR(250) NOT NULL,
	password VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS `poetry`;
CREATE TABLE `poetry` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `poetry_content`;
CREATE TABLE `poetry_content` (
  `id` int(11) NOT NULL,
  `poetry_id` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
