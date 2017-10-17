var mysql = require('mysql');

var pool;

exports.connect = function() {
	pool = mysql.createPool({
		connectionLimit: 100,
		host	: 'localhost',
		user	: 'whdauddbs',
		password: 'wjd2011@',
		database: 'pasugoon'
	});
}

exports.get = function() {
	return pool;
}