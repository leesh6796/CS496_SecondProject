var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var session = require('express-session');
var fs = require('fs');
var mongoose = require('mongoose');

// sprintf-js
app.use('/js', express.static(__dirname + '/node_modules/sprintf-js/dist')); // redirect bootstrap JS

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
db.once('open', () => {
        console.log("Connected to mongod server);");
});

mongoose.connect("mongodb://localhost/CS496");
