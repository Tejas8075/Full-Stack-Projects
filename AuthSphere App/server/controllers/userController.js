import userModel from "../models/userModel.js";

export const getUserData = async (req, res) => {
  try {
    const user = await userModel.findById(req.user.userId).select("-password");

    if (!user) {
      return res.json({ success: false, message: "User Not Found" });
    }

    res.json({
      success: true,
      userData: {
        _id: user._id,                 // ✅ Add this
        name: user.name,
        email: user.email,             // optional
        isAccountVerified: user.isAccountVerified
      }
    });

  } catch (error) {
    res.json({ success: false, message: error.message });
  }
};
