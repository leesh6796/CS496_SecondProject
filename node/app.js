var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var session = require('express-session');
var fs = require('fs');

app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);

app.use(express.static('public'));

// Bootstrap Directory Redirect
app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); // redirect bootstrap JS
app.use('/js', express.static(__dirname + '/node_modules/jquery/dist')); // redirect JS jQuery
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css')); // redirect CSS bootstrap

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

var router = require('./router/router');
app.use('/', router);
