var mysql = require('mysql');

var pool;

exports.connect = function() {
	pool = mysql.createPool({
		connectionLimit: 100,
		host	: '127.0.0.1',
		user	: 'root',
		password: 'root',
		database: 'OSAM'
	});
}

exports.get = function() {
	return pool;
}