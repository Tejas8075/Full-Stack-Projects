import React, { useState } from 'react'
import "./orderHistory.css"
import { useEffect } from 'react';
import { latestOrders } from '../../service/OrderService';

const OrderHistory = () => {

  // States
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    const fetchOrders = async () => {
      try {
        const response = await latestOrders();
        setOrders(response.data)
      } catch (error) {
        console.error(error)
      } finally {
        setLoading(false)
      }
    }

    fetchOrders();
  }, [])

  const formatItems = (items) => {
    return items.map((item) => `${item.name} x ${item.quantity}`).join(',');
  }

  const formatDate = (dateString) => {

    const options = {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }

    return new Date(dateString).toLocaleDateString('in-US', options);

  }

  if (loading) {
    return <div className='text-center py-4'>Loading orders...</div>
  }

  if (orders.length === 0) {
    return <div className="text center py-4">No orders found</div>
  }

  return (
    <div className="order-history-container">
      <h2 className="mb-2 text-light">Recent Orders</h2>

      <div className="table-responsive">
        <table className='table table-striped table-hover'>
          <thead className='table-dark'>
            <tr>
              <th>Order Id</th>
              <th>Customer</th>
              <th>Items</th>
              <th>Total</th>
              <th>Payment</th>
              <th>Status</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            {orders.map(order => (
              <tr key={order.orderId}>
                <td data-label="Order Id">{order.orderId}</td>
                <td data-label="Customer">
                  {order.customerName} <br />
                  <small className='text-muted'>{order.phoneNumber}</small>
                </td>
                <td data-label="Items">{formatItems(order.items)}</td>
                <td data-label="Total">₹{order.grandTotal}</td>
                <td data-label="Payment">{order.paymentMethod}</td>
                <td data-label="Status">
                  <span className={`badge ${order.paymentDetails?.status === "COMPLETED" ? "bg-success" : "bg-warning text-dark"}`}>
                    {order.paymentDetails?.status || "PENDING"}
                  </span>
                </td>
                <td data-label="Date">{formatDate(order.createdAt)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default OrderHistory