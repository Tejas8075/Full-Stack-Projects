import { createContext, useEffect, useState } from "react";
import axios from "axios"
import { fetchCategories } from "../service/CategoryService";
import { fetchItems } from "../service/ItemService";

export const AppContext = createContext();

export const AppContextProvider = ({children}) => {

  const [categories, setCategories] = useState([]);

  const [auth, setAuth] = useState({
    token: null,
    role: null
  })

  const [itemsData, setItemsData] = useState([]);

  const [cartItems, setCartItems] = useState([]);

  const addToCart = (item) =>{
    const existingItem = cartItems.find(cartItem => cartItem.name === item.name);

    if(existingItem){
      setCartItems(cartItems.map(cartItem => cartItem.name === item.name ? {...cartItem, quantity: cartItem.quantity+1} : cartItem))
    }
    else{
      setCartItems([...cartItems, {...item, quantity: 1}]);
    }
  }

  const removeFromCart = (itemId) => {
    setCartItems(cartItems.filter(item => item.itemId !== itemId))
  }

  const updateQuantity = (itemId, newQuantity) => {
    setCartItems(cartItems.map(item => item.itemId === itemId ? {...item, quantity: newQuantity} : item))
  }

  useEffect(() => {
    // API call to fetch the categories
    async function loadData(){

      if(localStorage.getItem("tokne") && localStorage.getItem("role")){
        setAuthData(
          localStorage.getItem("token"),
          localStorage.getItem("role")
        );
      }

      const response = await fetchCategories();

      const itemResponse = await fetchItems();

      setCategories(response.data);

      setItemsData(itemResponse.data);
    }

    loadData();
  }, [])

  const setAuthData = (token, role) => {
    setAuth({token, role});
  }

  const contextValue = {
    categories, setCategories,
    auth, setAuthData,
    itemsData, setItemsData,
    addToCart,
    cartItems,
    removeFromCart,
    updateQuantity,
  }

  return (
    <AppContext.Provider value={contextValue}>
      {children}
    </AppContext.Provider>
  )

}