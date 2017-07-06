var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Contact = require('./model/contact');

module.exports = {
        getContacts : (req, res) => {
                var email = req.params.email;
        },

        addContact : (req, res) => {
        }
};
