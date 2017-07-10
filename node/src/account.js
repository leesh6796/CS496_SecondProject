var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Account = require('./model/account');

module.exports = {
        getAccountInfo : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                // 해당 phoneNumber에 대한 account info를 가져오돼, contacts와 gallery는 제외한다.
                Account.find({'phoneNumber':phoneNumber}, {'contacts':false, 'gallery':false}, (err, account) => {
                        if(err) return res.status(500).json({error:err});
                        if(!account) return res.status(404).json({error:'Account not found'});
                        res.json(account);
                });
        },

        addAccount : (req, res) => {
                var body = req.body;

                name = body.name;
                phoneNumber = body.phoneNumber;
                email = body.email;
                profilePictureURL = body.profilePictureURL;

                Account.find({'phoneNumber':phoneNumber}, (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // phoneNumber 중복 안되면 새 account를 추가한다.
                        if(account.length == 0)
                        {
                                var newAccount = new Account({
                                        name:name,
                                        phoneNumber:phoneNumber,
                                        email:email,
                                        profilePictureURL:profilePictureURL
                                });

                                newAccount.save();
                                return res.json({'result':1});
                        }
                        // 중복되면 만들지 않는다.
                        else return res.json({'success':0, 'msg':'overlapped'});

                        res.send();
                });
        }
};
