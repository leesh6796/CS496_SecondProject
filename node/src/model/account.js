var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accountSchema = new Schema({
        name : String,
        phoneNumber : String,
        email : String,
        contacts : [{
                name : String,
                phoneNumber : String,
                email : String,
                profilePicture : String,
        }],
        photos : [{
                name : String,
                path : String
        }]
});

module.exports = mongoose.model('Account', accountSchema);
