var fs = require('fs');
var Account = require('./model/account');

module.exports = {
        uploadFile : (req, res) => {
                var file = req.file;
                var phoneNumber = req.params.phoneNumber;
                var filename = req.params.filename;

                Account.findOne({'phoneNumber':phoneNumber}, {'contacts':false}, (err, account) => {
                        console.log("진입");
                        console.log(filename);
                        var gallery = account.gallery;

                        gallery.push({
                                "name" : "",
                                "filename" : filename,
                                "path" : file.path
                        });

                        account.gallery = gallery;
                        account.save();
                })

                res.send(file);
                console.log(file);
        },

        getImage : (req, res) => {
                var filename = req.params.filename;
                var extension = filename.split('.')[1];

                var mime = {
                    html: 'text/html',
                    txt: 'text/plain',
                    css: 'text/css',
                    gif: 'image/gif',
                    jpg: 'image/jpeg',
                    jpeg: 'image/jpeg',
                    png: 'image/png',
                    svg: 'image/svg+xml',
                    js: 'application/javascript'
                };

                var img = fs.readFileSync('public/img/' + filename);
                res.writeHead(200, {'Content-Type': mime[extension]});
                res.end(img, 'binary');
        }
};
