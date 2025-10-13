import React, { useContext } from 'react'
import './explore.css'
import { AppContext } from '../../context/AppContext'
import DisplayCategory from '../../components/DisplayCategory/DisplayCategory';
import DisplayItems from '../../components/DisplayItems/DisplayItems';
import CustomerForm from '../../components/CustomerForm/CustomerForm';
import CartItems from '../../components/CartItems/CartItems';
import CartSummary from '../../components/CartSummary/CartSummary';

const Explore = () => {

  const {categories} = useContext(AppContext);
  console.log(categories);

  return (
    <div className='explore-container text-light'>
      <div className="left-column">
        <div className="first-row"
          style={{ overflowY: "auto" }}
        >
          <DisplayCategory categories={categories} />

        </div>

        <hr className="horizontal-line" />

        <div className="second-row"
          style={{ overflowY: "auto" }}
        >
          <DisplayItems />

        </div>
      </div>

      <div className="right-column d-flex flex-cloumn">
        <div className="customer-form-container"
          style={{ height: "15%" }}
        >
          <CustomerForm />

        </div>

        <hr className="my-3 text-light" />

        <div className="cart-items-container"
          style={{ height: "55%", overflowY: "auto" }}
        >
          <CartItems />

        </div>

        <div className="cart-summary-container"
          style={{ height: "30%" }}
        >
          <CartSummary />

        </div>
      </div>
    </div>
  )
}

export default Explore