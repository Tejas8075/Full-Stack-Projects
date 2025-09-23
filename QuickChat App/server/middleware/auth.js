import jwt from "jsonwebtoken";
import User from "../models/user.js";
import { responseStatusMsg } from "../lib/response.js";

// Middleware to protect routes / validate token
export const protectRoute = async (req, res, next) => {

  try {

    // Sending token in the header from the frontend
    const token = req.headers.token;

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // select("-password"):from the user we will remove the password
    const user = await User.findById(decoded.userId).select("-password");

    if (!user) {
      return res.status(404).json({
        success: false,
        message: "User Not Found"
      })
    }

    // add the user data in the request so we can access it in controller
    req.user = user;

    next();

  } catch (error) {
    responseStatusMsg(500, error.message, false, "Internal server error", "Error in auth middleware")
  }

}