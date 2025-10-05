import { createContext, useEffect, useState } from "react";
import axios from "axios"
import { fetchCategories } from "../service/CategoryService";

export const AppContext = createContext();

export const AppContextProvider = ({children}) => {

  const [categories, setCategories] = useState([]);

  useEffect(() => {
    // API call to fetch the categories
    async function loadData(){
      const response = await fetchCategories();
      setCategories(response.data);
    }

    loadData();
  }, [])

  const contextValue = {
    categories, setCategories,
    
  }

  return (
    <AppContext.Provider value={contextValue}>
      {children}
    </AppContext.Provider>
  )

}