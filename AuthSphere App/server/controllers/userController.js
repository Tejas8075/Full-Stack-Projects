import userModel from "../models/userModel.js";

export const getUserData = async (req, res) => {

  try {
    
    // const {userId} = req.body;

    const user = await userModel.findById(req.user.userId).select("-password");

    if(!user){
      return res.json({succes: false, message: "User Not Found"});
    }

    res.json({
      success: true,
      userData: {
        name: user.name,
        isAccountVerified: user.isAccountVerified
      }
    });

  } catch (error) {
    res.json({succes: false, message: error.message});
  }

}