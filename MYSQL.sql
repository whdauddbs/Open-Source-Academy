create database OSAM;

use OSAM;

CREATE TABLE `YoonJongMyeong_member` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(100) NOT NULL,
  `state` Boolean NOT NULL DEFAULT '0',
  `off_time` date NOT NULL DEFAULT '0001-01-01',
  PRIMARY KEY (`seq`,`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `YoonJongMyeong_check` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `member_seq` int(11) NOT NULL,
  `chk_member_seq` int(11) NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table yoonjongmyeong_member auto_increment = 1;
alter table yoonjongmyeong_check auto_increment = 1;