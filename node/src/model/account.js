var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accountSchema = new Schema({
        name : String,
        phoneNumber : String,
        email : String,
        profilePictureURL : String,
        contacts : [{
                name : String,
                phoneNumber : String,
                email : String,
        }],
        gallery : [{
                name : String,
                path : String,
                filename : String,
                comments : [{
                        name : String,
                        content : String
                }]
        }],
        guestbook : [{
                name : String,
                profilePictureURL : String,
                content : String,
                secret : Boolean
        }]
});

module.exports = mongoose.model('Account', accountSchema);

//db.accounts.update({name:"이상현"},{$pull:{contacts:{$exists:true}}})
