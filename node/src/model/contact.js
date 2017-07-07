var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var contactSchema = new Schema({
        name : String,
        accountId : Schema.Types.ObjectId,
        phoneNumber : String,
        email : String,
        profilePicture : String,
});

module.exports = mongoose.model('Contact', contactSchema);
