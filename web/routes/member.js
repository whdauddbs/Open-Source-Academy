var express = require('express');
var db = require('../db');
var router = express.Router();

// member /:phone

router.get('/:phone', function(req, res, next) {
	var phone = req.params.phone;

	var sql = "select * " + "from YoonJongMyeong_member " + "where phone = ? limit 1;";
	console.log("sql : " + sql);

	db.get().query(sql, phone, function (err, rows) {
		console.log("phone : " + phone);
		console.log("error : " + err);
		console.log("rows : " + JSON.stringify(rows));
		console.log("row.length : " + rows.length);
		if (rows.length > 0) {
			res.json(rows[0]);
		} else {
			res.sendStatus(400);
		}
	});
});

//member/phone
router.post('/phone', function(req, res) {
  var phone = req.body.phone;

  var sql_count = "select count(*) as cnt " +
            "from YoonJongMyeong_member " + 
            "where phone = ?;";  
  console.log("sql_count : " + sql_count);

  var sql_insert = "insert into YoonJongMyeong_member (phone) values(?);";
    
  db.get().query(sql_count, phone, function (err, rows) {
    console.log(rows);
    console.log(rows[0].cnt);

    if (rows[0].cnt > 0) {
      return res.sendStatus(400);
    }

    db.get().query(sql_insert, phone, function (err, result) {
      console.log(err);
      if (err) return res.sendStatus(400);
      res.status(200).send('' + result.insertId);
    });
  });
});

//member/info
router.post('/info', function(req, res) {
  var phone = req.body.phone;
  var state = req.body.state;
  var off_time = req.body.off_time;

  console.log({phone, state, off_time});

  var sql_count = "select count(*) as cnt " +
            "from YoonJongMyeong_member " + 
            "where phone = ?;";

  var sql_insert = "insert into YoonJongMyeong_member (phone, state, off_time) values(?, ?, ?, ?);";
  var sql_update = "update YoonJongMyeong_member set state = ?, off_time = ? where phone = ?; ";
  var sql_select = "select seq from YoonJongMyeong_member where phone = ?; ";
  
  db.get().query(sql_count, phone, function (err, rows) {
    if (rows[0].cnt > 0) {
      console.log("sql_update : " + sql_update);

      db.get().query(sql_update, [state, off_time, phone], function (err, result) {
        if (err) return res.sendStatus(400);
        console.log(result);

        db.get().query(sql_select, phone, function (err, rows) {
          if (err) return res.sendStatus(400);

          res.status(200).send('' + rows[0].seq);
        });
      });
    } else {
      console.log("sql_insert : " + sql_insert);

      db.get().query(sql_insert, [phone, state, off_time], function (err, result) {
        if (err) return res.sendStatus(400);

        res.status(200).send('' + result.insertId);
      });
    }
  });
});

module.exports = router;