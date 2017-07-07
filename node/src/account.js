var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Account = require('./model/account');

module.exports = {
        getAccountInfo : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.find({'phoneNumber':phoneNumber}, (err, account) => {
                        if(err) return res.status(500).json({error:err});
                        if(!account) return res.status(404).json({error:'Account not found'});
                        res.json(account);
                });
        },

        addAccount : (req, res) => {
                name = req.params.name;
                phoneNumber = req.params.phoneNumber;
                email = req.params.email;

                Account.find({'phoneNumber':phoneNumber}, (err, account) => {
                        if(err) return res.status(500).json({error:err});

                        // phoneNumber 중복 안되면 새 account를 추가한다.
                        if(!account)
                        {
                                res.json({'success':1});

                                var newAccount = new Account({
                                        name:name,
                                        phoneNumber:phoneNumber,
                                        email:email
                                });

                                newAccount.save((err) => {
                                        if(err) {
                                                console.error(err);
                                                res.json({result: 0});
                                                return;
                                        }

                                        res.json({result:1});
                                });
                        }
                        // 중복되면 만들지 않는다.
                        else res.json({'success':0, 'msg':'overlapped'});
                });
        }
};
