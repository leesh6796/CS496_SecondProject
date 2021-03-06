var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Account = require('./model/account');

module.exports = {
        getContacts : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.findOne({'phoneNumber':phoneNumber}, 'contacts', (err, account) => {
                        if(err) {
                                res.send("failed");
                        }

                        res.json(account.contacts);
                        console.log("[" + new Date().toLocaleString() + "] " + phoneNumber +"가 Contact Request");
                })
        },

        setContacts : (req, res) => {
                var body = req.body;

                var accountPhoneNumber = req.params.phoneNumber;
                //var length = int(body.length);
                console.log(body.contacts);
                var contacts = body.contacts;
                var i;

                Account.findOne({'phoneNumber':accountPhoneNumber}, 'contacts', (err, account) => {
                        if(err) {
                                res.send("failed");
                        }

                        account.contacts = contacts;
                        account.save();

                        res.send("success");
                        console.log("[" + new Date().toLocaleString() + "] " + accountPhoneNumber +"가 Set Contact");
                });
        }

        //db.accounts.update({name:"이상현"}, {$pushAll : {contacts : ["test1", "test2", "test3"]}})
};
