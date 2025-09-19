import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken'
import cookieParser from 'cookie-parser';

import userModel from '../models/userModel';

export const register = async (req, res) => {

  // get name, email and password from request in the Frontend
  const {name, email, password} = req.body;

  if(!name || !email || !password){
    return res.json({success: false, message: "Missing Details"});
  }

  try {

    // check if user already exists or not
    const existingUser = await userModel.findOne({email});
    if(existingUser){
      return res.json({success: false, message: "User already exists"})
    }

    // Password encrypted
    const hashedPassword = await bcrypt.hash(password, 10);

    // Assigning value from Request to the columns in DB
    const user = new userModel({name, email, password: hashedPassword});

    // Saving user
    await user.save();

    // generating token
    const token = jwt.sign({id: user._id}, process.env.JWT_SECRET, {expiresIn: '7d'})

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

  } catch(error) {
    res.json({success: false, message: error.message});
  }
}

