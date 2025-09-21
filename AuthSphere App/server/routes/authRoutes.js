import express from 'express';
import { isAuthenticated, login, logout, register, resetPassword, sendResetOtp, sendVerifyOtp, verifyEmail } from '../controllers/authControllers.js';
import userAuth from '../middleware/userAuth.js';

// Creating a Router
const authRouter = express.Router();

// Routes

// Actual endpoints will be /api/auth/login, and all the endpoints in the Routes due to server.js

// Registration
// when we hit the '/register endpoint the register function will be called
authRouter.post("/register", register);

// Login
authRouter.post("/login", login);

// Logout
authRouter.post("/logout", logout);

// Send Verify OTP
authRouter.post("/send-verify-otp", userAuth, sendVerifyOtp);

// Verify Email
authRouter.post("/verify-account", userAuth, verifyEmail);

// Check User is Authenticated
authRouter.get("/is-auth", userAuth, isAuthenticated);

// Send Reset Password OTP
authRouter.post("/send-reset-otp", sendResetOtp);

// Reset Password
authRouter.post("/reset-password", resetPassword);

export default authRouter;