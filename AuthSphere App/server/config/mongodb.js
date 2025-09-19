import mongoose from 'mongoose';

const connectDB = async () => {

  // when we get connected to the mongoose the function in the mongoose event will run
  mongoose.connection.on('connected', () => {
    console.log("DB Connected")
  })

  await mongoose.connect(`${process.env.MONGODB_URI}/authsphere`)
}

export default connectDB;