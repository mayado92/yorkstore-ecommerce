import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Checkout() {
  const [addresses, setAddresses] = useState([])
  const [selectedAddressId, setSelectedAddressId] = useState('')
  const [paymentMethod, setPaymentMethod] = useState('CASH')
  const navigate = useNavigate()
  const token = localStorage.getItem('token')

  useEffect(() => {
    if (!token) return
    // ENDPOINT: GET /api/users/addresses
    fetch('http://localhost:8080/api/users/addresses', {
      headers: { 'Authorization': 'Bearer ' + token }
    })
    .then(res => res.json())
    .then(data => {
      setAddresses(data || [])
      if (data && data.length > 0) setSelectedAddressId(data[0].addressId)
    })
  }, [token])

  const placeOrder = () => {
    if (!selectedAddressId) { alert('Please select or add an address'); return }

    // ENDPOINT: POST /api/order/users/payments/{paymentMethod}
    fetch(`http://localhost:8080/api/order/users/payments/${paymentMethod}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      // Backend expects OrderRequestDTO
      body: JSON.stringify({ 
         addressId: selectedAddressId,
         pgName: "Stripe", // Dummy data
         pgPaymentId: "pay_" + Math.floor(Math.random() * 10000),
         pgStatus: "SUCCESS",
         pgResponseMessage: "Payment Approved"
      })
    })
    .then(async (res) => {
      if (res.ok) {
        alert('Order Placed Successfully!')
        navigate('/profile')
      } else {
        alert('Payment Authorization Failed')
      }
    })
  }

  if (!token) return <div className="text-center py-20">Please login</div>

  return (
    <div className="max-w-3xl mx-auto px-4 py-12">
      <h1 className="text-4xl font-bold mb-8">Checkout</h1>
      
      <div className="bg-white p-6 rounded-xl shadow mb-6">
        <h2 className="text-xl font-bold mb-4">1. Select Shipping Address</h2>
        {addresses.length === 0 ? (
          <p className="text-red-500">No addresses found. Please add one in your profile/backend.</p>
        ) : (
          <select 
            value={selectedAddressId} 
            onChange={e => setSelectedAddressId(e.target.value)}
            className="w-full p-3 border rounded bg-gray-50"
          >
            {addresses.map(addr => (
              <option key={addr.addressId} value={addr.addressId}>
                {addr.street}, {addr.city}
              </option>
            ))}
          </select>
        )}
      </div>

      <div className="bg-white p-6 rounded-xl shadow mb-6">
        <h2 className="text-xl font-bold mb-4">2. Payment Method</h2>
        <select 
          value={paymentMethod} 
          onChange={e => setPaymentMethod(e.target.value)}
          className="w-full p-3 border rounded bg-gray-50"
        >
          <option value="CASH">Cash on Delivery</option>
          <option value="CARD">Credit Card</option>
        </select>
      </div>

      <button 
        onClick={placeOrder}
        className="w-full bg-indigo-600 text-white py-4 rounded-xl font-bold text-xl hover:bg-indigo-700"
      >
        Confirm Order
      </button>
    </div>
  )
}