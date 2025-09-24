import express from "express";
import "dotenv/config";
import cors from "cors";
import http from 'http'
import { connectDB } from "./lib/db.js";
import userRouter from "./routes.js/userRoutes.js";
import messageRouter from "./routes.js/messageRoutes.js";

import { Server } from "socket.io";

// 1]

// Create express app using http server
const app = express();

// We are using http because Socket io supprt this http server
const server = http.createServer(app);

// Initialize socket.io server
export const io = new Server(server, {
  cors: { origin: "*" } // * => allow all origins
})

// Store online users data
export const userSocketMap = {}; // {userId: socketId}

// Socket.io connection handler
io.on("connection", (socket) => {
  const userId = socket.handshake.query.userId;
  console.log("User connected: ", userId);

  // WHen userId is avilable, store the data in userSocketMap
  if (userId) {
    userSocketMap[userId] = socket.id;
  }

  // Emit online users to all connected clients
  io.emit("getOnlineUsers", Object.keys(userSocketMap))

  socket.on("disconnect", () => {
    console.log("User Disconnected:", userId)

    // Below userId will be deleted from userSocketMap
    delete userSocketMap[userId];

    io.emit("getOnlineUsers", Object.keys(userSocketMap));
  })
})

// Middleware setup
// upload upto 4mb
app.use(express.json({ limit: "4mb" }));

// Allows all the url to connect our backend
app.use(cors());

//Routes
app.use("/api/status", (req, res) => res.send("Server is live"));

// User Routes
app.use("/api/auth", userRouter);

// Message Routes
app.use("/api/messages", messageRouter);

// COnnect to MongoDB
await connectDB();

if (process.env.NODE_ENV !== 'production') {
  const PORT = process.env.PORT || 5000;

  server.listen(PORT, () => console.log("Server is running on port: " + PORT));
}

// export server for Vercel
export default server;
