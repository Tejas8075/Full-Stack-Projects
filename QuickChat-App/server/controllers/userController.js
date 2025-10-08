import { responseStatusMsg } from "../lib/response.js";
import cloudinary from "../lib/cloud.js";
import { generateToken } from "../lib/utils.js";
import User from "../models/user.js"
import bcrypt from "bcryptjs"


// Signup/Register a new user
export const signup = async (req, res) => {

  const { fullName, email, password, bio } = req.body;

  try {

    if (!fullName || !email || !password || !bio) {
      return res.status(400).json({ success: false, message: "Missing details" });
    }

    const user = await User.findOne({ email });

    if (user) {
      return res.status(409).json({ success: false, message: "Account already exists!" });
    }

    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(password, salt);

    const newUser = await User.create({
      fullName,
      email,
      password: hashedPassword,
      bio
    })

    const token = generateToken(newUser._id);

    res.status(201).json({
      success: true,
      userData: newUser,
      token,
      message: "Account created successfully"
    })

  } catch (error) {
    console.log("Error in sign up controller", error.message)
    res.status(500).json({
      success: false,
      message: "Internal server error"
    })
  }

}

// Login a user
export const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    // Validate input
    if (!email || !password) {
      return responseStatusMsg(
        res,
        400,
        "Missing credentials",
        false,
        "Email and password are required",
        "Login missing details"
      );
    }

    // Find user
    const userData = await User.findOne({ email });

    // Always check if userData is null BEFORE accessing its fields
    if (!userData) {
      return responseStatusMsg(
        res,
        404,
        "User not found",
        false,
        "No account found with this email",
        "Login - user not found"
      );
    }

    // Compare password
    const isPasswordCorrect = await bcrypt.compare(password, userData.password);

    if (!isPasswordCorrect) {
      return responseStatusMsg(
        res,
        401,
        "Invalid password",
        false,
        "Invalid credentials",
        "Login - wrong password"
      );
    }

    // Generate token and respond
    const token = generateToken(userData._id);

    return res.status(200).json({
      success: true,
      userData,
      token,
      message: "Login successful",
    });
  } catch (error) {
    console.error("Error in login controller:", error.message);
    return responseStatusMsg(
      res,
      500,
      error.message,
      false,
      "Internal server error",
      "Error in login controller"
    );
  }
};




// is-Authenticated
export const checkAuth = (req, res) => {

  // returns user data
  try {
    return res.status(200).json({
      success: true,
      user: req.user
    });
  } catch (error) {
    console.error("Error in checkAuth controller:", error.message);
    return responseStatusMsg(res, 500, error.message, false, "Internal server error", "Error in checkAuth controller");
  }


}

// Update User Profile
export const updateProfile = async (req, res) => {
  try {
    const { profilePic, bio, fullName } = req.body;
    const userId = req.user._id;

    let updatedUser;

    if (!profilePic) {
      updatedUser = await User.findByIdAndUpdate(
        userId,
        { bio, fullName },
        { new: true }
      );
    } else {
      const upload = await cloudinary.uploader.upload(profilePic);
      updatedUser = await User.findByIdAndUpdate(
        userId,
        { profilePic: upload.secure_url, bio, fullName },
        { new: true }
      );
    }

    return res.status(200).json({
      success: true,
      user: updatedUser
    });
  } catch (error) {
    console.error("Error in updateProfile controller:", error.message);
    return responseStatusMsg(res, 500, error.message, false, "Internal server error", "Error in update profile controller");
  }
};
