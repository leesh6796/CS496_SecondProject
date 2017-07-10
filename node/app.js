var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var session = require('express-session');
var fs = require('fs');
var mongoose = require('mongoose');
var serve_static = require('serve-static');

// sprintf-js
app.use('/js', serve_static(__dirname + '/node_modules/sprintf-js/dist')); // redirect bootstrap JS

app.use('/uploads', express.static('public/img'));

// 파일 업로드 용량 제한 & 경로 설정
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));
app.use(session({
 secret: '@#$%^&*($)',
 resave: false,
 saveUninitialized: true
}));

app.listen(80, function() {
        console.log('Connected at 80');
});

var router = require('./src/router');
app.use('/', router);

// Mongoose라는 ODM을 사용한다.
var db = mongoose.connection;
db.on('error', console.error);
db.once('openUri', () => {
        console.log("Connected to mongod server;");
});

mongoose.connect("mongodb://localhost/CS496");
