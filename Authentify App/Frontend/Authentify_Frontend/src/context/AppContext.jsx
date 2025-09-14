import { createContext, useEffect, useState } from "react";
import { AppConstants } from "../util/constants";
import axios from "axios";
import { toast } from "react-toastify";

export const AppContext = createContext();

export const AppContextProvider = (props) => {

  axios.defaults.withCredentials = true;

  const backendURL = AppConstants.BACKEND_URL;

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userData, setUserData] = useState(false);

  const getUserData = async() => {
    try {
      const response = await axios.get(backendURL+"/profile")
      if(response.status === 200 ){
        setUserData(response.data)
      } else{
        toast.error("Unable to retrieve profile");
      }
    } catch (err) {
      toast.error(err.messasge);
    }
  }

  const getAuthState = async () => {
    try {
      const response = await axios.get(backendURL + "/is-authenticated");
      if (response.status === 200) {
        setIsLoggedIn(true);
        await getUserData();
      } else {
        setIsLoggedIn(false);
        // setUserData(false);
      }
    } catch (err) {
    if (err.response && err.response.status === 401) {
      // Just unauthenticated → silently set state
      setIsLoggedIn(false);
      // don't toast here
    } else {
      // Other errors → show toast
      const msg = err.response?.data?.message || err.message || "Something went wrong";
      toast.error(msg);
    }
  }
  }

  useEffect(() => {
    getAuthState();
  }, [])

  const contextValue = {
    backendURL,
    isLoggedIn, setIsLoggedIn,
    userData, setUserData,
    getUserData, 
  };

  return (
    <AppContext.Provider value={contextValue}>
      {props.children}
    </AppContext.Provider>
  );
};
