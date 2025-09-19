import mongoose from "mongoose";

// Creating Schema
const userSchema = new mongoose.Schema({
  name: {type: String, required: true},
  email: {type: String, required: true, unique: true},
  password: {type: String, required: true},
  verifyOtp: {type: String, default: ''},
  verifyOtpExpireAt: {type: Number, default: 0},
  isAccountVerified: {type: Boolean, default: false},
  resetOtp: {type: String, default: ''},
  resetOtpExpireAt: {type: Number, default: 0},
})

// Creating userModel
// Create the model again and again without mongoose.models.user ||
const userModel = mongoose.models.user || mongoose.model('user', userSchema);

export default userModel;