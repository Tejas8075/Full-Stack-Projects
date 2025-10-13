import React, { useContext, useState } from 'react'
import './explore.css'
import { AppContext } from '../../context/AppContext'
import DisplayCategory from '../../components/DisplayCategory/DisplayCategory';
import DisplayItems from '../../components/DisplayItems/DisplayItems';
import CustomerForm from '../../components/CustomerForm/CustomerForm';
import CartItems from '../../components/CartItems/CartItems';
import CartSummary from '../../components/CartSummary/CartSummary';

const Explore = () => {

  const { categories } = useContext(AppContext);
  console.log(categories);

  const [selectedCategory, setSelectedCategory] = useState("");

  const [customerName, setCustomerName] = useState("");

  const [mobileNumber, setMobileNumber] = useState("");

  return (
    <div className='explore-container text-light'>
      <div className="left-column">
        <div className="first-row"
          style={{ overflowY: "auto" }}
        >
          <DisplayCategory
            categories={categories}
            selectedCategory={selectedCategory}
            setSelectedCategory={setSelectedCategory}
          />

        </div>

        <hr className="horizontal-line" />

        <div className="second-row"
          style={{ overflowY: "auto" }}
        >
          <DisplayItems selectedCategory={selectedCategory} />

        </div>
      </div>

      <div className="right-column d-flex flex-column">
        <div className="customer-form-container flex-shrink-0">
          <CustomerForm
            customerName={customerName}
            mobileNumber={mobileNumber}
            setCustomerName={setCustomerName}
            setMobileNumber={setMobileNumber}
          />
        </div>

        <hr className="my-3 text-light" />

        <div className="cart-items-container flex-grow-1 overflow-auto">
          <CartItems />
        </div>

        <div className="cart-summary-container flex-shrink-0 mt-3">
          <CartSummary
            customerName={customerName}
            mobileNumber={mobileNumber}
            setCustomerName={setCustomerName}
            setMobileNumber={setMobileNumber}
          />
        </div>
      </div>
    </div>
  )
}

export default Explore