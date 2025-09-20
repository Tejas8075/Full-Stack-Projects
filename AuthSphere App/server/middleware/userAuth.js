import jwt from "jsonwebtoken";

const userAuth = async (req, res, next) => {
  const { token } = req.cookies;

  if (!token) {
    return res.json({ success: false, message: "Not Authorized, Login Again" });
  }

  try {
    // Decode the token
    const tokenDecode = jwt.verify(token, process.env.JWT_SECRET);

    if (tokenDecode.id) {
      // ✅ attach decoded userId safely
      req.user = { userId: tokenDecode.id };
      next();
    } else {
      return res.json({ success: false, message: "Not Authorized. Login Again" });
    }

  } catch (error) {
    res.json({ success: false, message: error.message });
  }
};

export default userAuth;
