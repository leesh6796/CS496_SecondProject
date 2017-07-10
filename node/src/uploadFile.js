var fs = require('fs');
var account =

module.exports = {
        uploadFile : (req, res) => {
                var file = req.file;
                var phoneNumber = req.params.phoneNumber;

                res.send(file);
                console.log(file);
        }
};
