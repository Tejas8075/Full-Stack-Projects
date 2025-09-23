import cloudinary from "../lib/cloud.js";
import Message from "../models/Messages.js";
import User from "../models/user.js";

import {io, userSocketMap} from "../server.js";


// Get all users except logged in users
export const getUsersForSideBar = async (req, res) => {

  try {
    const userId = req.user._id;

    const filteredUsers = await User.find({ _id: { $ne: userId } }).select("-password");

    // Count number of unseen messages
    const unseenMessages = {};
    const promises = filteredUsers.map(async (user) => {
      const messages = await Message.find({ senderId: user._id, receiverId: userId, seen: false })

      if (messages.length > 0) {
        unseenMessages[user._id] = messages.length;
      }
    })

    await Promise.all(promises);

    res.status(200).json({ success: true, users: filteredUsers, unseenMessages });
  } catch (error) {

    responseStatusMsg(500, error.message, false, "Internal server error", "Error in Get User for Sidebar")

  }

}

// Get all messages for selected user
export const getMessages = async (req, res) => {

  try {
    const { id: selectedUserId } = req.params;

    // Logged in userId
    const myId = req.user._id;

    const messages = await Message.find({

      // OR operator and messages between two people
      $or: [
        {
          senderId: myId,
          receiverId: selectedUserId
        },
        {
          senderId: selectedUserId,
          receiverId: myId
        }
      ]
    })

    await Message.updateMany({ senderId: selectedUserId, receiverId: myId }, { seen: true })

    res.status(200).json({ messages });

  } catch (error) {
    responseStatusMsg(500, error.message, false, "Internal server error", "Error in Get User ")
  }

};

// Mark message as seen using Message Id
export const markMessageAsSeen = async (req, res) => {

  try {

    const { id } = req.params;

    await Message.findByIdAndUpdate(id, { seen: true });

    res.status(200).json({ success: true });

  } catch (error) {

    responseStatusMsg(500, error.message, false, "Internal server error", "Error in Mark Msg Seen ")
  }

}

// Send message to the selected user
export const sendMessage = async () => {

  try {

    const {text, image} = req.body;
    const receiverId = req.params.Id;
    const senderId = req.user._id;

    let imageUrl;
    if(image){
      const uploadResponse = await cloudinary.uploader.upload(image);

      imageUrl = uploadResponse.secure_url;
    }

    const newMessage = await Message.create({
      senderId,
      receiverId,
      text,
      image: imageUrl
    })

    // Emit the new Message to the receiver's socket
    const receiverSocketId = userSocketMap[receiverId];

    if(receiverId){
      io.to(receiverSocketId).emit("newMessage", newMessage);
    }

    res.status(200).json({
      success: true,
      newMessage
    });

  } catch (error) {
    responseStatusMsg(500, error.message, false, "Internal server error", "Error in Sent Msg ")
  }

}