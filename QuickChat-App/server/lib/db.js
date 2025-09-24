import mongoose from "mongoose";

// 2]
// Function to connect to the mongodb database
export const connectDB = async () => {
  try {
    mongoose.connection.on('connected', () => console.log("DB Coonnected"));
    await mongoose.connect(`${process.env.MONGODB_URI}/chat-app`)
  } catch (error) {
    console.log(error.msg);
  }
}