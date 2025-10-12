import { createContext, useEffect, useState } from "react";
import axios from "axios"
import { fetchCategories } from "../service/CategoryService";

export const AppContext = createContext();

export const AppContextProvider = ({children}) => {

  const [categories, setCategories] = useState([]);

  const [auth, setAuth] = useState({
    token: null,
    role: null
  })

  useEffect(() => {
    // API call to fetch the categories
    async function loadData(){
      const response = await fetchCategories();
      setCategories(response.data);
    }

    loadData();
  }, [])

  const setAuthData = (token, role) => {
    setAuth({token, role});
  }

  const contextValue = {
    categories, setCategories,
    auth, setAuthData
  }

  return (
    <AppContext.Provider value={contextValue}>
      {children}
    </AppContext.Provider>
  )

}