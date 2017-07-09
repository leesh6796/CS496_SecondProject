var express = require('express');

var router = express.Router();
var account = require('./account');
var contact = require('./contact');
var upload = require('./uploadFile');

// File upload를 위한 모듈
var multer = require('multer');
var storage = multer.diskStorage({
        destination: (req, file, cb) => {
                cb(null, 'public/img/');
        },
        filename: (req,file, cb) => {
                cb(null, file.originalname);
        }
});
var multerInstance = multer({storage:storage});

router.route('/api/account/get/:phoneNumber').get(account.getAccountInfo);
router.route('/api/account/add/:name/:phoneNumber/:email').put(account.addAccount);

router.route('/api/contacts/get/:phoneNumber').get(contact.getContacts);
router.route('/api/contacts/add/:accountPhoneNumber/:name/:phoneNumber/:email').put(contact.getContacts);

// atachment는 form의 input file name attribute를 의미한다.
router.route('/api/upload/picture/:phoneNumber').post(multerInstance.single('attachment'), upload.uploadFile);

module.exports = router;
