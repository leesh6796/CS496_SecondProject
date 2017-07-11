var fs = require('fs');
var Account = require('./model/account');

module.exports = {
        uploadFile : (req, res) => {
                // multer는 req.file에 업로드된 파일 정보를 저장한다.
                // filename, originalname, path 등의 attribute를 가진다.
                var file = req.file;
                var phoneNumber = req.params.phoneNumber;
                var filename = req.params.filename;

                if(phoneNumber == null || filename == null) {
                        return res.send("need more parameters");
                }

                // MongoDB Account Schema에서 phoneNumber가 일치하는 Document를 찾는다.
                Account.findOne({'phoneNumber':phoneNumber}, {'contacts':false}, (err, account) => {
                        console.log(filename);

                        // account에서 gallery json array만 가져온다.
                        var gallery = account.gallery;

                        // gallery에 새로운 사진을 추가하고 replace.
                        gallery.push({
                                "name" : "",
                                "filename" : filename,
                                "path" : file.path
                        });

                        account.gallery = gallery;
                        account.save(); // 마지막에 꼭 save 해줘야 한다.
                })

                res.send(file);
                console.log("[" + new Date().toLocaleString() + "] File upload");
                console.log(file);
        },

        getImage : (req, res) => {
                var filename = req.params.filename;
                var extension = filename.split('.')[1];

                // 파일 확장자(Extension)에 맞게 content-type을 정해준다.
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

                // 파일을 읽을 때는 동기로 읽는다.
                var img = fs.readFileSync('public/img/' + filename);
                res.writeHead(200, {'Content-Type': mime[extension]});
                res.end(img, 'binary'); // binary로 보낸다.

                console.log("[" + new Date().toLocaleString() + "] Get Image " + filename);
        }
};
