import React, { useContext, useState } from 'react'
import "./cartSummary.css"
import { AppContext } from '../../context/AppContext'
import ReceiptPopup from '../ReceiptPopup/ReceiptPopup';
import toast from 'react-hot-toast';
import { createOrder, deleteOrder } from '../../service/OrderService';
import { createRazorpayOrder, verifyPayment } from '../../service/PaymentService';
import { AppConstants } from '../../utils/constants';

const CartSummary = ({ customerName, mobileNumber, setCustomerName, setMobileNumber }) => {

  const { cartItems, clearCart } = useContext(AppContext);

  const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0)
  const tax = totalAmount * 0.01;
  const grandTotal = totalAmount + tax;

  const [isProcessing, setIsProcesing] = useState(false);

  const [orderDetails, setOrderDetails] = useState(null);

  const [showPopup, setShowPopup] = useState(false);

  const clearAll = () => {
    setCustomerName("");
    setMobileNumber("");
    clearCart();
  }

  const placeOrder = () => {
    setShowPopup(true);
    clearAll();
  }

  const handlePrintReceipt = () => {
    window.print();
  }

  const loadRazorpayScript = () => {
    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = "https://checkout.razorpay.com/v1/checkout.js"
      script.onload = () => resolve(true);
      script.onerror = () => resolve(false);
      document.body.appendChild(script);
    })
  }

  const deleteOrderOnFailure = async (orderId) => {

    try {
      await deleteOrder(orderId);
    } catch (error) {
      console.log(error);
      toast.error("Something went wrong")
    }

  }

  const completePayment = async (paymentMode) => {

    if (!customerName || !mobileNumber) {
      toast.error("Enter customer details");
      return;
    }

    if (cartItems.length === 0) {
      toast.error("Your cart is empty");
      return;
    }

    const orderData = {
      customerName,
      phoneNumber: mobileNumber,
      cartItems,
      subTotal: totalAmount,
      tax,
      grandTotal,
      paymentMethod: paymentMode.toUpperCase()
    }

    setIsProcesing(true);

    try {

      const response = await createOrder(orderData);

      const savedData = response.data;

      if (response.status === 201 && paymentMode === "cash") {
        toast.success("Payment successful with Cash");
        setOrderDetails(savedData);
      } else if (response.status === 201 && paymentMode === "upi") {
        const razorpayLoaded = await loadRazorpayScript();

        if (!razorpayLoaded) {
          toast.error("Unable to load razorpay");
          await deleteOrderOnFailure(savedData.orderId);
          return;
        }

        // create razorpay order
        const razorpayResponse = await createRazorpayOrder({
          amount: grandTotal,
          currency: 'INR',
          orderId: savedData.orderId  // ✅ link to backend order
        });

        const options = {
          key: AppConstants.RAZORPAY_KEY_ID,
          amount: razorpayResponse.data.amount,
          currency: razorpayResponse.data.currency,
          order_id: razorpayResponse.data.id,
          name: "My Retail Shop",
          description: "Order payment",
          handler: async function (response) {
            // Verify the payment
            await verifyPaymentHandler(response, savedData);
          },
          prefill: {
            name: customerName,
            contact: mobileNumber,
          },
          theme: {
            color: "#3399cc"
          },
          model: {
            ondismiss: async () => {
              // if user doesn't complete the payment and directly cloe the page
              await deleteOrderOnFailure(savedData.orderId);

              toast.error("Payment cancelled!!!");
            }
          }

        }

        const rzp = new window.Razorpay(options);

        rzp.on("payment.failed", async (response) => {
          await deleteOrderOnFailure(savedData.orderId);

          toast.error("Payment failed")

          console.error(response.error.description);
        })

        rzp.open();

      }

    } catch (error) {
      console.error(error);
      toast.error("Payment processing failed");
    } finally {
      setIsProcesing(false)
    }

  }

  const verifyPaymentHandler = async (response, savedData) => {

    const paymentData = {
      razorpayOrderId: response.razorpay_order_id,
      razorpayPaymentId: response.razorpay_payment_id,
      razorpaySignature: response.razorpay_signature,
      orderId: savedData.orderId
    };

    try {

      const paymentResponse = await verifyPayment(paymentData);

      if (paymentResponse.status === 200) {
        toast.success("Payment successful with UPI");
        setOrderDetails({
          ...savedData,
          paymentDetails: {
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature,
          }
        })
      }
      else {
        toast.error("Payment procesing failed for UPI");
      }

    } catch (error) {
      console.error(error);
      toast.error("Payment failed in UPI");
    }

  }

  return (
    <div className="mt-2">
      <div className="cart-summary-details">
        <div className="d-flex justify-content-between mb-2">
          <span className="text-light">Item: </span>
          <span className="text-light">₹{totalAmount.toFixed(2)}</span>
        </div>

        <div className="d-flex justify-content-between mb-2">
          <span className="text-light">Tax (1%): </span>
          <span className="text-light">₹{tax.toFixed(2)}</span>
        </div>

        <div className="d-flex justify-content-between mb-4">
          <span className="text-light">Total: </span>
          <span className="text-light">₹{grandTotal.toFixed(2)}</span>
        </div>
      </div>

      <div className="d-flex gap-3">
        <button
          onClick={() => completePayment("cash")}
          disabled={isProcessing}
          className="btn btn-success flex-grow-1">
          {isProcessing ? "processing..." : "Cash"}
        </button>

        <button
          onClick={() => completePayment("upi")}
          disabled={isProcessing}
          className="btn btn-primary flex-grow-1">
          {isProcessing ? "processing..." : "UPI"}
        </button>
      </div>

      <div className="d-flex gap-3 mt-3">
        <button
          onClick={placeOrder}
          disabled={isProcessing || !orderDetails}
          className="btn btn-warning flex-grow-1">
          Place Order
        </button>
      </div>

      {/* <ReceiptPopup /> */}
      {
        showPopup && (
          <ReceiptPopup
            orderDetails={{
              ...orderDetails,
              razorpayOrderId: orderDetails.paymentDetails?.razorpayOrderId,
              razorpayPaymentId: orderDetails.paymentDetails?.razorpayPaymentId,
            }}
            onClose={() => setShowPopup(false)}
            onPrint={handlePrintReceipt}
          />
        )
      }
    </div>
  )
}

export default CartSummary