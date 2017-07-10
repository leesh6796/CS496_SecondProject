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
        filename: (req, file, cb) => {
                cb(null, req.params.filename);
        }
});
var multerInstance = multer({storage:storage});

//router.route('/api/account/add/:name/:phoneNumber/:email/:profilePictureURL').put(account.addAccount);
router.route('/api/account/add').put(account.addAccount);
router.route('/api/:phoneNumber/get/account').get(account.getAccountInfo);

router.route('/api/:phoneNumber/get/friends').get(contact.getContacts);
router.route('/api/:phoneNumber/set/friends').put(contact.setContacts);

// atachment는 form의 input file name attribute를 의미한다.
router.route('/api/:phoneNumber/upload/picture/:filename').post(multerInstance.single('attachment'), upload.uploadFile);
router.route('/api/:phoneNumber/get/gallery').get(account.getGallery);
//router.route('/api/:phoneNumber/picture/get').post(multerInstance.single('attachment'), upload.uploadFile);

router.route('/api/picture/get/:filename').get(upload.getImage);


module.exports = router;
