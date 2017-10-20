var mysql = require('mysql');

var pool;

exports.connect = function() {
	pool = mysql.createPool({
		connectionLimit: 100,
		host	: 'localhost',
		user	: 'root',
		password: 'root',
		database: 'OSAM',
		port	: 3306
	});
}

exports.get = function() {
	return pool;
}