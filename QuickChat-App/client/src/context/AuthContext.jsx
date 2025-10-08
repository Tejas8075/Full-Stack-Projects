import { createContext, useEffect, useState } from "react";
import axios from 'axios';
import toast from "react-hot-toast";
import { io } from "socket.io-client"
import { useNavigate } from "react-router-dom";

const backendUrl = import.meta.env.VITE_BACKEND_URL;

axios.defaults.baseURL = backendUrl;

export const AuthContext = createContext();

// {
//   axios,
//   authUser,
//   onlineUsers,
//   socket,
//   login,
//   logout,
//   updateProfile
// }

export const AuthProvider = ({ children }) => {

  const navigate = useNavigate();

  // If the token is available in the localStorage then it will be stored in 'token' state
  const [token, setToken] = useState(localStorage.getItem("token"));

  // After login the userData will be stored in the authUser
  const [authUser, setAuthUser] = useState(null)

  const [onlineUsers, setOnlineUsers] = useState([])

  const [socket, setSocket] = useState(null);

  // Check if user is authenticated and if so, set the user data and connect the socket
  const checkAuth = async () => {
    try {
      const { data } = await axios.get("/api/auth/check");

      if (data.success) {
        setAuthUser(data.user);
        connectSocket(data.user);
      }
    } catch (error) {
      toast.error("Internal Server Error");
    }
  }

  // User can login and register OR
  // Login function to handle user authentication and socket connection
  const login = async (state, credentials, navigate) => {
    try {
      const { data } = await axios.post(backendUrl + `/api/auth/${state}`, credentials);

      if (data.success) {
        setAuthUser(data.userData);
        connectSocket(data.userData);
        axios.defaults.headers.common["token"] = data.token;
        setToken(data.token);

        // store the token in the localstorage
        localStorage.setItem("token", data.token);

        toast.success(data.message);

        navigate("/");
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      // const errorMsg = error.response?.data?.message || "Something went wrong!";
      toast.error(error.message);
    }
  }

  // Logout function to handle user logout and socket disconnection
  const logout = async () => {
    localStorage.removeItem("token");
    setToken(null);
    setAuthUser(null);
    setOnlineUsers([]);
    axios.defaults.headers.common["token"] = null

    toast.success("Logged out successfully");

    socket.disconnect();
  }

  // Update the user profile
  const updateProfile = async (body) => {
    try {

      const { data } = await axios.put("/api/auth/update-profile", body);

      if (data.success) {
        setAuthUser(data.user);
        toast.success("Profile updated successfully");
      }

    } catch (error) {
        const errorMsg = error.response?.data?.message || "Something went wrong!";
        toast.error(errorMsg);
    }
  }

  // after the user is authenticated we have to connect it to the socket
  // Connect socket function to handle socket connection and online users updates
  const connectSocket = (userData) => {
    if (!userData || socket?.connected) {
      return;
    }

    const newSocket = io(backendUrl, {
      query: {
        userId: userData._id,
      }
    });

    newSocket.connect();

    setSocket(newSocket);

    // helps us to connect with socket
    newSocket.on("getOnlineUsers", (userIds) => {
      setOnlineUsers(userIds);
    })
  }


  // We have to execute the 'checkAuth' function whenever we open the web page, so use useEffect hook
  useEffect(() => {
    if (token) {
      // it will add this token for all API Request made using axios when the token is availablein localstorage
      axios.defaults.headers.common['token'] = token;
      checkAuth();
    }
  }, [token])

  // Whatever state or function we put in the object it will be used by all the components
  const value = {
    axios,
    authUser,
    onlineUsers,
    socket,
    login,
    logout,
    updateProfile
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )

}