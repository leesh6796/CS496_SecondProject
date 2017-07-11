var vsprintf = require('sprintf-js').vsprintf;
var replaceAll = require('replaceall');
var mongoose = require('mongoose');

var Account = require('./model/account');

module.exports = {
        getAccountInfo : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                // 해당 phoneNumber에 대한 account info를 가져오돼, contacts와 gallery는 제외한다.
                Account.findOne({'phoneNumber':phoneNumber}, {'contacts':false, 'gallery':false, 'guestbook':false}, (err, account) => {
                        if(err) return res.status(500).json({error:err});
                        if(!account) return res.status(404).json({error:'Account not found'});
                        res.json(account);
                });
        },

        getAllAccount : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.find({}, {'contacts':false, 'gallery':false}, (err, account) => {
                        if(err) return res.status(500).json({error:err});
                        if(!account) return res.status(404).json({error:'Account not found'});
                        console.log("[" + new Date().toLocaleString() + "] " + "누군가 Dev 권한으로 All Account Request");
                        res.json(account);
                })
        },

        addAccount : (req, res) => {
                var body = req.body;

                name = body.name;
                phoneNumber = body.phoneNumber;
                email = body.email;
                profilePictureURL = body.profilePictureURL;

                Account.findOne({'phoneNumber':phoneNumber}, (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // phoneNumber 중복 안되면 새 account를 추가한다.
                        if(account.length == 0)
                        {
                                var newAccount = new Account({
                                        name:name,
                                        phoneNumber:phoneNumber,
                                        email:email,
                                        profilePictureURL:profilePictureURL,
                                        gallery : [],
                                        contacts : [],
                                        guestbook : []
                                });

                                newAccount.save();
                                console.log({'name':name, 'phoneNumber':phoneNumber, 'result':1});
                                return res.json({'result':1});
                        }
                        // 중복되면 만들지 않는다.
                        else return res.json({'success':0, 'msg':'overlapped'});

                        res.send();
                });
        },

        getGallery : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.findOne({'phoneNumber' : phoneNumber}, 'gallery.filename', (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // 해당 phoneNumber 가진 account가 없다면
                        if(account == null) {
                                console.log("NULL 감지");
                        } else {
                                var gallery = account.gallery;
                                var returnValue = "";

                                gallery.forEach((element, index, array) => {
                                        returnValue += "api/picture/get/" + element.filename;
                                        if(index < gallery.length - 1)
                                                returnValue += ",";
                                });

                                console.log("[" + new Date().toLocaleString() + "] " + phoneNumber + "가 Gallery Request");
                                console.log(returnValue);

                                return res.send(returnValue);
                        }

                        return res.send("OK");
                });
        },

        getGuestBook : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.findOne({'phoneNumber' : phoneNumber}, 'guestbook', (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // 해당 phoneNumber 가진 account가 없다면
                        if(account == null) {
                                console.log("NULL 감지");
                        } else {
                                var guestbook = account.guestbook;

                                console.log("[" + new Date().toLocaleString() + "] " + phoneNumber + "가 GuestBook Request");

                                return res.send(guestbook);
                        }

                        return res.send("OK");
                });
        },

        addGuestBook : (req, res) => {
                var phoneNumber = req.params.phoneNumber;
                var body = req.body;

                var visitor = body.visitor;
                var content = body.content;
                var profilePictureURL = body.profilePictureURL;
                var isSecret = body.secret;

                Account.findOne({'phoneNumber' : phoneNumber}, 'guestbook', (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // 해당 phoneNumber 가진 account가 없다면
                        if(account == null) {
                                console.log("NULL 감지");
                        } else {
                                var guestbook = account.guestbook;
                                var article = {
                                        "name" : visitor,
                                        "profilePictureURL" : profilePictureURL,
                                        "content" : content,
                                        "secret" : isSecret
                                };

                                guestbook.push(article);

                                account.guestbook = guestbook;
                                account.save();

                                console.log("[" + new Date().toLocaleString() + "] " + phoneNumber + "가 Guestbook 추가");
                                console.log(article);

                                return res.send(guestbook);
                        }

                        return res.send("OK");
                })
        },

        clearGuestBook : (req, res) => {
                var phoneNumber = req.params.phoneNumber;

                Account.findOne({'phoneNumber' : phoneNumber}, 'guestbook', (err, account) => {
                        if(err) return res.status(500).json({'error':err});

                        // 해당 phoneNumber 가진 account가 없다면
                        if(account == null) {
                                console.log("NULL 감지");
                        } else {
                                account.guestbook = [];
                                account.save();

                                console.log("[" + new Date().toLocaleString() + "] " + phoneNumber + "가 Guestbook clear");

                                return res.send(account.guestbook);
                        }

                        return res.send("OK");
                });
        }
};
