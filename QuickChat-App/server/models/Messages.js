import mongoose from "mongoose";

// 3]

const messageSchema = new mongoose.Schema({
  senderId: {type: mongoose.Schema.Types.ObjectId, ref: "User", required: true},
  receiverId: {type: mongoose.Schema.Types.ObjectId, ref: "User", required: true},
  text: {type: String},
  image: {type: String},
  seen: {type: Boolean, default: false}
}, {timestamps: true});

// Creating user model
const Messages = mongoose.model("Messages", messageSchema);

export default Messages;