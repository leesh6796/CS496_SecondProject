var express = require('express');

var router = express.Router();
var account = require('./account');
var contact = require('./contact');

router.route('/api/account/get/:phoneNumber').get(account.getAccountInfo);
router.route('/api/account/add/:name/:phoneNumber/:email').get(account.addAccount);

router.route('/api/contacts/:email').get(contact.getContacts);

module.exports = router;
