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

  const formatItems = () => {
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

  if(loading){
    return <div className='text-center py-4'>Loading orders...</div>
  }

  if(orders.length === 0){
    return <div className="text center py-4">No orders found</div>
  }

  return (
    <div>OrderHistory</div>
  )
}

export default OrderHistory