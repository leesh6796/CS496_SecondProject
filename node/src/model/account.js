var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var accountSchema = new Schema({
        name : String,
        phoneNumber : String,
        email : String,
});

module.exports = mongoose.model('Account', accountSchema);
