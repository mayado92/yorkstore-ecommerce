import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'

export default function Profile() {
  const navigate = useNavigate()
  const [userInfo, setUserInfo] = useState(null) // Full profile data
  const [addresses, setAddresses] = useState([]) // User's addresses
  const [orders, setOrders] = useState([])     // Purchase history/Orders
  const [loading, setLoading] = useState(true)
  const token = localStorage.getItem('token')

  useEffect(() => {
    if (!token) {
      navigate('/login')
      return
    }

    // --- 1. Fetch User Details (Name, Email, Phone, etc.) ---
    const fetchUserInfo = fetch('http://localhost:8080/api/auth/user', {
        headers: { 'Authorization': 'Bearer ' + token }
    }).then(res => res.json())

    // --- 2. Fetch User Addresses ---
    const fetchAddresses = fetch('http://localhost:8080/api/users/addresses', {
        headers: { 'Authorization': 'Bearer ' + token }
    }).then(res => res.json())

    // --- 3. Fetch Purchase History ---
    const fetchOrders = fetch('http://localhost:8080/api/public/user/cart', {
        headers: { 'Authorization': 'Bearer ' + token }
    }).then(res => res.json())


    Promise.all([fetchUserInfo, fetchAddresses, fetchOrders])
      .then(([userData, addrData, orderData]) => {
        setUserInfo(userData)
        // Ensure addresses is an array, not a single object or error message
        setAddresses(Array.isArray(addrData) ? addrData : []) 
        // NOTE: Temporarily showing cart data as 'orders' until proper order history API is used.
        setOrders(orderData?.cartItems || []) 
      })
      .catch(err => {
        console.error("Error fetching profile data:", err)
        // If one fetch fails, still show the data that succeeded
      })
      .finally(() => setLoading(false))

  }, [token, navigate])

  if (loading) return <div className="p-12 text-center">Loading Profile...</div>

  const primaryAddress = addresses[0]; // Show the first address as primary

  return (
    <div className="min-h-screen bg-gray-50 py-20">
      <div className="max-w-4xl mx-auto bg-white p-12 rounded-3xl shadow-2xl">
        <button onClick={() => navigate(-1)} className="mb-6 text-indigo-600 hover:underline">
          &larr; Back
        </button>
        <h1 className="text-5xl font-bold mb-10 text-gray-800">My Profile</h1>
        
        {/* --- Personal Information --- */}
        <h2 className="text-3xl font-semibold mb-4 text-indigo-700">Personal Information</h2>
        <div className="space-y-3 p-4 border rounded-xl bg-indigo-50/50 mb-8">
            <p><strong>Username:</strong> {userInfo?.username || 'N/A'}</p>
            <p><strong>Full Name:</strong> {userInfo?.firstName || 'N/A'} {userInfo?.lastName || ''}</p>
            <p><strong>Email:</strong> {userInfo?.email || 'N/A'}</p>
            <p><strong>Phone:</strong> {userInfo?.phone || 'N/A'}</p>
        </div>

        {/* --- Address Information --- */}
        <h2 className="text-3xl font-semibold mb-4 text-indigo-700">My Address</h2>
        {primaryAddress ? (
            <div className="space-y-2 p-4 border rounded-xl bg-white shadow-sm mb-8">
                <p>{primaryAddress.street}, {primaryAddress.city}</p>
                <p>{primaryAddress.state}, {primaryAddress.country} {primaryAddress.pincode}</p>
                <Link to="/addresses" className="text-sm text-indigo-500 hover:underline">Manage Addresses &rarr;</Link>
            </div>
        ) : (
            <div className="p-4 text-gray-500 border rounded-xl bg-gray-50 mb-8">
                No address found. <Link to="/create-address" className="text-indigo-600 font-bold">Add one now.</Link>
            </div>
        )}

        {/* --- Purchase History --- */}
        <h2 className="text-3xl font-semibold mt-10 mb-4 text-indigo-700">Purchase History</h2>
        
        {orders.length === 0 ? (
          <p className="text-gray-500 p-4 border rounded-xl bg-gray-50">No orders placed yet.</p>
        ) : (
          <div className="space-y-4">
            {orders.map((item, index) => (
              <div key={item.cartItemId || index} className="p-4 border rounded-xl bg-white shadow-sm flex justify-between items-center">
                <div>
                    <p className="font-bold text-lg">{item.product.productName}</p>
                    <p className="text-sm text-gray-500">Price: ${item.productPrice} x {item.quantity}</p>
                </div>
                <span className="text-md font-bold text-green-600">
                    Total: ${(item.productPrice * item.quantity).toFixed(2)}
                </span>
              </div>
            ))}
            <p className="text-sm text-red-500">Note: This section currently shows cart items as a placeholder for order history.</p>
          </div>
        )}
      </div>
    </div>
  )
}