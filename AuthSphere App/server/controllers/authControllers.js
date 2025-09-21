import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken'
import cookieParser from 'cookie-parser';

import userModel from '../models/userModel.js';
import transporter from '../config/nodemailer.js';
import { text } from 'express';
import { EMAIL_VERIFY_TEMPLATE, PASSWORD_RESET_TEMPLATE } from '../config/emailTemplates.js';

// Registration
export const register = async (req, res) => {

  // get name, email and password from request in the Frontend
  const { name, email, password } = req.body;

  if (!name || !email || !password) {
    return res.json({ success: false, message: "Missing Details" });
  }

  try {

    // check if user already exists or not
    const existingUser = await userModel.findOne({ email });
    if (existingUser) {
      return res.json({ success: false, message: "User already exists" })
    }

    // Password encrypted
    const hashedPassword = await bcrypt.hash(password, 10);

    // Assigning value from Request to the columns in DB
    const user = new userModel({ name, email, password: hashedPassword });

    // Saving user
    await user.save();

    // generating token
    const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, { expiresIn: '7d' })

    // Sending token in the form of Cookie to the CLIENT
    res.cookie('token', token, {
      // only http request can access this cookie
      httpOnly: true,
      // When we run this project on LIVE SERVER then it will run on HTTPS and will be true
      // When we run this project on LOCAL ENVIRONMENT then it will run on HTTP and will be true
      // if PRODUCTION then truen otherwise false
      secure: process.env.NODE_ENV === 'production',
      // When CLIENT and SERVER run on same ENVIRONMENT then STRICT otherwise when we DEPLOY then Backend will have Different DOMAIN NAME and Frontend will have Different DOMAIN NAME then NONE
      sameSite: process.env.NODE_ENV === 'production' ? 'none' : 'strict',
      // 7 days in milliseconds
      maxAge: 7 * 24 * 60 * 60 * 1000
    });

    // send a Welcome email
    const mailOptions = {
      from: process.env.SENDER_EMAIL,
      to: email,
      subject: 'Welcome to AuthSphere',
      text: `Welcome to AuthSphere website. Your account has been created with an email id: ${email}`
    };

    await transporter.sendMail(mailOptions);

    return res.json({ success: true });

  } catch (error) {
    res.json({ success: false, message: error.message });
  }
}

// Login
export const login = async (req, res) => {

  const { email, password } = req.body;

  if (!email || !password) {
    return res.json({ success: false, message: "Email and password are required" });
  }

  try {

    const user = await userModel.findOne({ email });

    if (!user) {
      return res.json({ success: false, message: `Invalid email` })
    }

    const isMatch = await bcrypt.compare(password, user.password);

    if (!isMatch) {
      return res.json({ success: false, message: `Invalid password` })
    }

    // generating token
    const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, { expiresIn: '7d' })

    // Sending token in the form of Cookie to the CLIENT
    res.cookie('token', token, {
      // only http request can access this cookie
      httpOnly: true,
      // When we run this project on LIVE SERVER then it will run on HTTPS and will be true
      // When we run this project on LOCAL ENVIRONMENT then it will run on HTTP and will be true
      // if PRODUCTION then truen otherwise false
      secure: process.env.NODE_ENV === 'production',
      // When CLIENT and SERVER run on same ENVIRONMENT then STRICT otherwise when we DEPLOY then Backend will have Different DOMAIN NAME and Frontend will have Different DOMAIN NAME then NONE
      sameSite: process.env.NODE_ENV === 'production' ? 'none' : 'strict',
      // 7 days in milliseconds
      maxAge: 7 * 24 * 60 * 60 * 1000
    });

    return res.json({ success: true });

  } catch (error) {
    return res.json({ success: false, message: error.message });
  }

}

// Logout
export const logout = async (req, res) => {

  try {

    // Clearing Cookie
    res.clearCookie('token', {
      // only http request can access this cookie
      httpOnly: true,
      // When we run this project on LIVE SERVER then it will run on HTTPS and will be true
      // When we run this project on LOCAL ENVIRONMENT then it will run on HTTP and will be true
      // if PRODUCTION then truen otherwise false
      secure: process.env.NODE_ENV === 'production',
      // When CLIENT and SERVER run on same ENVIRONMENT then STRICT otherwise when we DEPLOY then Backend will have Different DOMAIN NAME and Frontend will have Different DOMAIN NAME then NONE
      sameSite: process.env.NODE_ENV === 'production' ? 'none' : 'strict',
      // 7 days in milliseconds
      maxAge: 7 * 24 * 60 * 60 * 1000
    })

    return res.json({ success: true, message: "Logged Out" })

  } catch (error) {
    return res.json({ success: false, message: error.message });
  }

}

// Verification OTP
export const sendVerifyOtp = async (req, res) => {

  try {

    const { userId } = req.body;

    const user = await userModel.findById(userId);

    if (!user) {
      return res.json({ success: false, message: "User not found" });
    }

    if (user.isAccountVerified) {
      return res.json({ success: false, message: "Account already verified" });
    }

    // Generate 6 digit OTP
    const otp = String(Math.floor(100000 + Math.random() * 900000));

    user.verifyOtp = otp;
    user.verifyOtpExpireAt = Date.now() + 24 * 60 * 60 * 1000;

    await user.save();

    const mailOption = {
      from: process.env.SENDER_EMAIL,
      to: user.email,
      subject: 'Account Verification OTP',
      // text: `Your OTP is ${otp}. Verify your account using this OTP.`,
      html: EMAIL_VERIFY_TEMPLATE.replace("{{otp}}", otp).replace("{{email}}", user.email)
    };

    await transporter.sendMail(mailOption);

    res.json({ success: true, message: "Verification OTP sent on Email." })

  } catch (error) {
    res.json({ success: false, message: error.message });
  }

}

// Verify Email
export const verifyEmail = async (req, res) => {

  const { userId, otp } = req.body;

  if (!userId || !otp) {
    return res.json({ success: false, message: "Missing Details" });
  }

  try {

    const user = await userModel.findById(userId);

    if (!user) {
      return res.json({ success: false, message: "User not found" });
    }
    if (user.verifyOtp === '' || user.verifyOtp !== otp) {
      return res.json({ success: false, message: "Invalid OTP" });
    }
    if (user.verifyOtpExpireAt < Date.now()) {
      return res.json({ success: false, message: "OTP expired" })
    }

    user.isAccountVerified = true;
    user.verifyOtp = '';
    user.verifyOtpExpireAt = 0;

    await user.save();

    return res.json({ success: true, message: "Email verified successfully" });

  } catch (error) {
    return res.json({ success: false, message: error.message })
  }

}

// Verify if User Autheticated
export const isAuthenticated = async (req, res) => {

  try {
    return res.json({ success: true });
  } catch (error) {
    res.json({ success: false, message: error.message });
  }

}

// Send Password Reset OTP
export const sendResetOtp = async (req, res) => {

  const { email } = req.body;

  if (!email) {
    return res.json({ success: false, message: "Email is required" });
  }

  try {

    const user = await userModel.findOne({email});

    if (!user) {
      return res.json({ success: false, message: "User not found" });
    }

    // Generate 6 digit OTP
    const otp = String(Math.floor(100000 + Math.random() * 900000));

    user.resetOtp = otp;
    user.resetOtpExpireAt = Date.now() + 15 * 60 * 1000;

    await user.save();

    const mailOption = {
      from: process.env.SENDER_EMAIL,
      to: user.email,
      subject: 'Reset Password OTP',
      // text: `Your OTP is ${otp}. Reset your password using this OTP.`
      html: PASSWORD_RESET_TEMPLATE.replace("{{otp}}", otp).replace("{{email}}", user.email)
    };

    await transporter.sendMail(mailOption);

    res.json({ success: true, message: "Reset Password OTP sent on Email." })

  } catch (error) {
    return res.json({ success: false, message: error.message });
  }

}

// Reset User Password
export const resetPassword = async (req, res) => {

  const { email, otp, newPassword } = req.body;

  if (!email || !otp || !newPassword) {
    return res.json({ success: false, message: "Email, OTP and New Password is required" });
  }

  try {

    const user = await userModel.findOne({email});

    if (!user) {
      return res.json({ success: false, message: "User not found" });
    }

    if (user.resetOtp === '' || user.resetOtp !== otp) {
      return res.json({ success: false, message: "Invalid OTP" });
    }

    if (user.resetOtpExpireAt < Date.now()) {
      return res.json({ success: false, message: "OTP expired" });
    }

    const hashedPassword = await bcrypt.hash(newPassword, 10);

    user.password = hashedPassword;

    user.resetOtp = '';
    user.resetOtpExpireAt = 0;

    await user.save();

    res.json({success: true, message: "Password has been reset successfully"});

  } catch (error) {
    return res.json({ success: false, message: error.message });
  }

}