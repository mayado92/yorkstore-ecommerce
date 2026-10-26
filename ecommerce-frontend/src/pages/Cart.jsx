import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom' 

export default function Cart() {
  const [cart, setCart] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()
  
  const fetchCart = () => {
    const currentToken = localStorage.getItem('token');
    
    if (!currentToken) {
        setLoading(false);
        return; // Let the !token early return handle the message
    }

    setLoading(true);
    setError('');

    fetch('http://localhost:8080/api/public/user/cart', {
      headers: { 
        // Use the fresh token
        'Authorization': 'Bearer ' + currentToken,
        'Content-Type': 'application/json' 
      }
    })
    .then(async (res) => {
      if (!res.ok) {
        const errorBody = await res.text();
        throw new Error(`[${res.status}] Failed to fetch cart: ${errorBody || res.statusText}`);
      }
      return res.json();
    })
    .then(data => {
      setCart(data);
      setLoading(false);
    })
    .catch(err => {
      console.error(err);
      // Display the detailed error message for debugging
      setError(`Could not load cart. Details: ${err.message}`); 
      setLoading(false);
    })
  }

  useEffect(() => {
    // Run fetchCart on mount and whenever a new token is detected
    if (localStorage.getItem('token')) {
      fetchCart()
    } else {
      setLoading(false)
    }
  }, [navigate]) 

  const updateQuantity = (productId, operation) => {
    const currentToken = localStorage.getItem('token');
    if (!currentToken || !cart) { alert('Missing login token or cart data.'); return; }
    
    let url;
    let method;

    if (operation === 'delete') {
     
        url = `http://localhost:8080/api/public/carts/${cart.cartId}/product/${productId}`;
        method = 'DELETE';
    } else {
        url = `http://localhost:8080/api/public/cart/products/${productId}/quantity/${operation}`;
        method = 'PUT';
    }
    
    fetch(url, {
      method: method,
      headers: { 'Authorization': 'Bearer ' + currentToken }
    })
    .then(res => {
      if (res.ok) {
        fetchCart(); // Re-fetch cart data after successful update/delete
      } else {
        alert(`Could not ${operation} item. Check backend logs.`);
      }
    })
    .catch(err => alert(`Error processing cart update: ${err.message}`));
  }

  if (!localStorage.getItem('token')) return (
    <div className="text-center py-20">
      <h2 className="text-3xl font-bold text-gray-700">Please login to view your cart</h2>
    </div>
  )

  if (loading) return (
    <div className="flex justify-center py-20">
      <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-purple-600"></div>
    </div>
  )

  if (error) return (
    <div className="text-center py-20 text-red-600 text-xl">{error}</div>
  )

  // SAFEGUARD: Ensure cartItems is an array for mapping
  const cartItems = cart?.cartItems || [] 

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-6xl mx-auto px-4">
        <h1 className="text-4xl font-bold text-gray-800 mb-10">Your Shopping Cart</h1>
        
        {cartItems.length === 0 ? (
          <div className="text-center py-20 bg-white rounded-xl shadow">
            <p className="text-2xl text-gray-600">Your cart is empty</p>
            <button onClick={() => navigate('/')} className="mt-4 text-indigo-600 hover:underline">
              Start Shopping
            </button>
          </div>
        ) : (
          <div className="grid lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2">
              {cartItems.map(item => (
                <div key={item.cartItemId} className="bg-white rounded-xl shadow-md p-6 mb-6 flex items-center gap-6">
                  <img 
                    src={item.product.image || 'https://via.placeholder.com/100'} 
                    alt={item.product.productName} 
                    className="w-32 h-32 object-cover rounded-lg"
                  />
                  <div className="flex-1">
                    <h3 className="text-xl font-bold text-gray-800">{item.product.productName}</h3>
                    <p className="text-2xl font-bold text-purple-600 mt-2">${item.productPrice}</p>
                  </div>
                  
                  <div className="flex items-center gap-3">
                    <button 
                      onClick={() => updateQuantity(item.product.productId, 'decrease')} 
                      // Disable decrease button if quantity is 1 (next click should be delete)
                      disabled={item.quantity <= 1} 
                      className="bg-gray-200 hover:bg-gray-300 w-10 h-10 rounded-full text-xl flex items-center justify-center pb-1 disabled:opacity-50">
                      -
                    </button>
                    <span className="text-xl font-bold w-12 text-center">{item.quantity}</span>
                    <button 
                      onClick={() => updateQuantity(item.product.productId, 'increase')} 
                      className="bg-gray-200 hover:bg-gray-300 w-10 h-10 rounded-full text-xl flex items-center justify-center pb-1">
                      +
                    </button>
                  </div>
                  
                  <button 
                    onClick={() => updateQuantity(item.product.productId, 'delete')} 
                    className="bg-red-600 hover:bg-red-700 text-white px-6 py-3 rounded-lg ml-4"
                  >
                    Remove
                  </button>
                </div>
              ))}
            </div>
            
            <div className="bg-white rounded-xl shadow-lg p-8 h-fit">
              <h2 className="text-2xl font-bold mb-6">Order Summary</h2>
              <div className="border-t pt-4">
                <div className="flex justify-between text-xl mb-4">
                  <span>Total</span>
                  <span className="font-bold text-purple-600">${cart?.totalPrice?.toFixed(2) || '0.00'}</span>
                </div>
                <button 
                  onClick={() => navigate('/checkout')}
                  className="w-full bg-gradient-to-r from-green-600 to-emerald-600 text-white py-4 rounded-lg text-xl font-bold hover:from-green-700 hover:to-emerald-700 transition"
                >
                  Proceed to Checkout
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}