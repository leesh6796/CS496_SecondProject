var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Contact = require('./model/contact');

module.exports = {
        getContacts : (req, res) => {
                var email = req.params.email;
        },

        addContact : (req, res) => {
                var accountPhoneNumber = req.params.accountPhoneNumber;
                var name = req.params.name;
                var phoneNumber = req.params.phoneNumber;
                var email = req.params.email;
        }
};
