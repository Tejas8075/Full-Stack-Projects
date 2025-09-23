import mongoose from "mongoose";

// 3]

const userSchema = new mongoose.Schema({
  email: {type: String, required: true, unique: true},
  fullName: {type: String, required: true},
  password: {type: String, required: true, minlenght: 6},
  profilePic: {type: String, default: ""},
  profilePic: {type: String},
}, {timestamps: true})

// Creating user model
const User = mongoose.model("User", userSchema);

export default User;